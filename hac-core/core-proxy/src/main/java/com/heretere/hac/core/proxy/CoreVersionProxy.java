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

package com.heretere.hac.core.proxy;

import com.heretere.hac.core.proxy.packets.channel.ChannelInjector;
import com.heretere.hac.util.proxy.VersionProxy;
import org.jetbrains.annotations.NotNull;

/**
 * The type Core version proxy.
 */
public abstract class CoreVersionProxy implements VersionProxy {

    /**
     * Instantiates a new Core version proxy.
     */
    protected CoreVersionProxy() { }

    /**
     * This method registers the packet factories for each core-nms version.
     */
    protected abstract void registerPackets();

    /**
     * Gets channel injector.
     *
     * @return the channel injector
     */
    public abstract @NotNull ChannelInjector getChannelInjector();

    @Override public final void preLoad() {
        this.registerPackets();
        this.load();
    }

    @Override public final void preUnload() {
        this.unload();
    }

    @Override public abstract void load();

    @Override public abstract void unload();
}
