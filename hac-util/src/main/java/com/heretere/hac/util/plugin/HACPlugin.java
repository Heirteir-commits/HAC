/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.heretere.hac.util.plugin;

import com.heretere.hac.util.plugin.logging.Log;
import com.heretere.hdl.DependencyPlugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public abstract class HACPlugin extends DependencyPlugin {
    /**
     * The base directory all files should be passed to. This is similar to
     * {@link org.bukkit.plugin.java.JavaPlugin#getDataFolder()}
     * except it points to a directory that could be used by multiple plugins.
     */
    private final @NotNull Path baseDirectory;

    /**
     * The prefix that the logger uses for console.
     */
    private final @NotNull String prefix;

    /**
     * This is a logger delegator that just adds some custom functionality to the Bukkit logger.
     */
    private final @NotNull Log log;

    protected HACPlugin(
        final @NotNull String baseDirectory,
        final @NotNull String prefix
    ) {
        super();
        this.baseDirectory = this.getDataFolder().toPath().getParent().resolve(baseDirectory);
        this.prefix = "[HAC] [" + prefix + "] ";

        this.log = new Log(this);
        this.log.open();
    }

    /**
     * Similar to {@link org.bukkit.plugin.java.JavaPlugin#onLoad()} except it's ran after all dependencies are loaded.
     */
    public abstract void load();

    /**
     * Similar to {@link org.bukkit.plugin.java.JavaPlugin#onEnable()} except it's ran after all dependencies are
     * loaded.
     */
    public abstract void enable();

    /**
     * Similar to {@link org.bukkit.plugin.java.JavaPlugin#onDisable()} except it's ran after all dependencies are
     * loaded.
     */
    public abstract void disable();

    /**
     * The base directory for files in this plugin.
     *
     * @return The base directory
     */
    public @NotNull Path getBaseDirectory() {
        return this.baseDirectory;
    }

    /**
     * The logger prefix of this plugin.
     *
     * @return The logger prefix of this plugin
     */
    public @NotNull String getPrefix() {
        return this.prefix;
    }

    /**
     * The logger delegator for this dependency plugin.
     *
     * @return The logger delegator.
     */
    public @NotNull Log getLog() {
        return this.log;
    }
}
