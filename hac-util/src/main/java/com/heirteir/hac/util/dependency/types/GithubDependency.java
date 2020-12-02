package com.heirteir.hac.util.dependency.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.heirteir.hac.util.dependency.plugin.DependencyPlugin;
import com.heirteir.hac.util.dependency.types.annotation.Github;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GithubDependency extends AbstractDependency {
    private static final String TAG_NAME = "tag_name";
    private static final String ASSETS = "assets";

    private final String fileName;
    private final String pluginName;
    private final String githubRepoRelativeURL;
    private final int spigotId;
    private final boolean checkUpdate;
    private final boolean useBeta;

    /* dynamic vars */
    private boolean checkedUpdate;
    private boolean needsUpdate;
    private boolean updating;

    public GithubDependency(DependencyPlugin parent, @NotNull String fileName, @NotNull String pluginMame, @NotNull String githubRepoRelativeURL, int spigotId, boolean checkUpdate, boolean useBeta) {
        super(parent);
        this.fileName = fileName;
        this.pluginName = pluginMame;
        this.githubRepoRelativeURL = githubRepoRelativeURL;
        this.spigotId = spigotId;
        this.checkUpdate = checkUpdate;
        this.useBeta = useBeta;
    }

    public GithubDependency(DependencyPlugin parent, @NotNull Github github) {
        this(parent, github.fileName(), github.pluginName(), github.githubRepoRelativeURL(), github.spigotId(), false, false);
    }

    private static int versionStringToInt(String input) {
        return Integer.parseInt(input.replaceAll("[^\\d]", ""));
    }

    @Override
    public boolean needsUpdate() {
        if (this.checkedUpdate) {
            return this.needsUpdate;
        }

        this.needsUpdate = !Files.exists(this.getDownloadLocation()) && Bukkit.getPluginManager().getPlugin(this.pluginName) == null;

        if (this.checkUpdate && !this.needsUpdate) {
            JsonObject latest = this.getLatestVersion();

            if (latest != null) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin(this.pluginName);

                if (plugin == null) {
                    this.needsUpdate = true;
                } else {
                    int currentVersion = GithubDependency.versionStringToInt(plugin.getDescription().getVersion());
                    int latestVersion = GithubDependency.versionStringToInt(latest.get(GithubDependency.TAG_NAME).getAsString());

                    if (currentVersion < latestVersion) {
                        this.updating = true;
                        this.needsUpdate = true;
                    }
                }
            }
        }

        this.checkedUpdate = true;
        return this.needsUpdate;
    }

    @Override
    public Path getDownloadLocation() {
        if (updating) {
            return super.getDependencyPlugin().getPluginFolder().getParent().resolve("upload").resolve(this.fileName + ".jar");
        } else {
            return super.getDependencyPlugin().getPluginFolder().getParent().resolve(this.fileName + ".jar");
        }
    }

    @Override
    public URL getManualURL() throws MalformedURLException {
        return new URL(String.format("https://www.spigotmc.org/resources/%d", this.spigotId));
    }

    @Override
    public URL getUrl() throws MalformedURLException {
        JsonObject latest = this.getLatestVersion();
        URL output;

        if (latest != null) {
            output = new URL(
                    StreamSupport.stream(latest.getAsJsonArray(GithubDependency.ASSETS).spliterator(), true)
                            .filter(element -> element.getAsJsonObject().get("name").getAsString().contains(this.fileName))
                            .findAny()
                            .orElseThrow(MalformedURLException::new)
                            .getAsJsonObject()
                            .get("browser_download_url")
                            .getAsString());
        } else {
            output = null;
        }
        return output;
    }

    @Nullable
    private JsonObject getLatestVersion() {
        JsonObject latest;

        String apiURL = "https://api.github.com/repos/" + this.githubRepoRelativeURL + "/releases";

        InputStream is = null;
        try {
            is = new URL(apiURL).openStream();
        } catch (IOException e) {
            super.getDependencyPlugin().getLog().severe(e);
        }

        if (is == null) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            JsonArray json = new JsonParser()
                    .parse(reader.lines()
                            .collect(Collectors.joining("\n")))
                    .getAsJsonArray();

            latest = StreamSupport.stream(json.spliterator(), true)
                    .map(JsonElement::getAsJsonObject)
                    .filter(element -> element.has(GithubDependency.TAG_NAME))
                    .filter(element -> element.get(GithubDependency.TAG_NAME).getAsString().contains("STABLE") || (this.useBeta && element.get(GithubDependency.TAG_NAME).getAsString().contains("BETA")))
                    .filter(element -> element.has(GithubDependency.ASSETS))
                    .filter(element -> element.getAsJsonArray(GithubDependency.ASSETS).size() > 0)
                    .max(Comparator.comparingInt(element -> GithubDependency.versionStringToInt(element.get(GithubDependency.TAG_NAME).getAsString())))
                    .orElse(null);
        } catch (IOException e) {
            super.getDependencyPlugin().getLog().severe(e);
            latest = null;
        }

        return latest;
    }

    @Override
    public String getName() {
        return this.pluginName;
    }

    @Override
    public String toString() {
        return this.pluginName;
    }

    @Override
    public boolean download() {
        boolean success;

        if (this.updating) {
            super.getDependencyPlugin().getLog().info(String.format("Downloading update for '%s'.", this.pluginName));
        } else {
            super.getDependencyPlugin().getLog().info(String.format("Downloading plugin dependency '%s'.", this.pluginName));
        }

        success = super.download();

        if (success) {
            if (this.updating) {
                super.getDependencyPlugin().getLog().info(String.format("Successfully downloaded update for '%s' to '%s'. It will be active on next reload.", this.getName(), this.getDownloadLocation().toAbsolutePath()));
            } else {
                super.getDependencyPlugin().getLog().info(String.format("Dependency '%s' successfully downloaded.", this.getName()));
            }
        }

        return success;
    }

    @Override
    public boolean load() {
        boolean success = true;
        try {
            if (this.needsUpdate() && !this.updating) {
                super.getDependencyPlugin().getLog().info(String.format("Attempting to Load dependency '%s'.", this.getName()));

                Bukkit.getPluginManager().loadPlugin(this.getDownloadLocation().toFile());

                Plugin plugin = Bukkit.getPluginManager().getPlugin(this.getName());

                if (plugin != null) {
                    plugin.onLoad();
                } else {
                    success = false;
                }
            }
        } catch (Exception e) {
            success = false;
            super.getDependencyPlugin().getLog().severe(e);
        }
        return success;
    }
}
