package com.heirteir.hac.util.dependency.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.heirteir.hac.util.dependency.types.annotation.Github;
import com.heirteir.hac.util.files.FilePaths;
import com.heirteir.hac.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
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
    private final String fileName;
    private final String pluginName;
    private final String githubRepoRelativeURL;
    private final int spigotId;
    private final boolean checkUpdate;
    private final boolean useBeta;

    /* dynamic vars */
    private Boolean needsUpdate = null;
    private boolean updating;

    public GithubDependency(@NotNull String fileName, @NotNull String pluginMame, @NotNull String githubRepoRelativeURL, int spigotId, boolean checkUpdate, boolean useBeta) {
        this.fileName = fileName;
        this.pluginName = pluginMame;
        this.githubRepoRelativeURL = githubRepoRelativeURL;
        this.spigotId = spigotId;
        this.checkUpdate = checkUpdate;
        this.useBeta = useBeta;
    }

    public GithubDependency(@NotNull Github github) {
        this(github.fileName(), github.pluginName(), github.githubRepoRelativeURL(), github.spigotId(), false, false);
    }

    private static int versionStringToInt(String input) {
        return Integer.parseInt(input.replaceAll("[^\\d]", ""));
    }

    @Override
    public boolean needsUpdate() {
        if (this.needsUpdate != null) {
            return this.needsUpdate;
        }

        boolean needsUpdate = false;

        if (!Files.exists(this.getDownloadLocation()) && Bukkit.getPluginManager().getPlugin(this.pluginName) == null) {
            needsUpdate = true;
        }

        if (this.checkUpdate && !needsUpdate) {
            JsonObject latest = this.getLatestVersion();

            if (latest != null) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin(this.pluginName);

                if (plugin == null) {
                    needsUpdate = true;
                } else {
                    int currentVersion = GithubDependency.versionStringToInt(plugin.getDescription().getVersion());
                    int latestVersion = GithubDependency.versionStringToInt(latest.get("tag_name").getAsString());

                    if (currentVersion < latestVersion) {
                        this.updating = true;
                        needsUpdate = true;
                    }
                }
            }
        }

        return this.needsUpdate = needsUpdate;
    }

    @Override
    public Path getDownloadLocation() {
        return FilePaths.INSTANCE.getPluginFolder().getParent().resolve(this.fileName + ".jar");
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
            output = new URL(latest.getAsJsonArray("assets").get(0)
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
        try {
            String apiURL = "https://api.github.com/repos/" + this.githubRepoRelativeURL + "/releases";

            InputStream is = new URL(apiURL).openStream();


            JsonArray json = new JsonParser()
                    .parse(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n")))
                    .getAsJsonArray();

            latest = StreamSupport.stream(json.spliterator(), true)
                    .map(JsonElement::getAsJsonObject)
                    .filter(element -> element.has("tag_name"))
                    .filter(element -> element.get("tag_name").getAsString().contains("STABLE") || (this.useBeta && element.get("tag_name").getAsString().contains("BETA")))
                    .filter(element -> element.has("assets"))
                    .filter(element -> element.getAsJsonArray("assets").size() > 0)
                    .max(Comparator.comparingInt(element -> GithubDependency.versionStringToInt(element.get("tag_name").getAsString())))
                    .orElse(null);
        } catch (IOException e) {
            latest = null;
            Log.INSTANCE.severe(e);
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
        boolean success = true;
        Path downloadLocation = this.getDownloadLocation();

        if (this.updating) {
            Log.INSTANCE.info(String.format("Downloading update for '%s'.", this.pluginName));
            downloadLocation = this.getDownloadLocation().getParent().resolve("update").resolve(this.fileName + ".jar");
        } else {
            Log.INSTANCE.info(String.format("Downloading plugin dependency '%s'.", this.pluginName));
        }

        try {
            URL url = this.getUrl();

            Files.createDirectories(downloadLocation.getParent());
            InputStream is = url.openStream();
            Files.copy(is, downloadLocation);

            if (this.updating) {
                Log.INSTANCE.info(String.format("Successfully downloaded update for '%s' to '%s'. It will be active on next reload.", this.getName(), downloadLocation.toAbsolutePath()));
            } else {
                Log.INSTANCE.info(String.format("Dependency '%s' successfully downloaded.", this.getName()));
            }
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public boolean load() {
        boolean success = true;
        try {
            if (this.needsUpdate() && !this.updating) {
                Log.INSTANCE.info(String.format("Attempting to Load dependency '%s'.", this.getName()));

                Bukkit.getPluginManager().loadPlugin(this.getDownloadLocation().toFile());

                Plugin plugin = Bukkit.getPluginManager().getPlugin(this.getName());

                if (plugin != null) {
                    plugin.onLoad();
                } else {
                    success = false;
                }
            }
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            success = false;
            Log.INSTANCE.severe(e);
        }
        return success;
    }
}
