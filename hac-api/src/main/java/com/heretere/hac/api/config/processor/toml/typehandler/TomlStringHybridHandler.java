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

package com.heretere.hac.api.config.processor.toml.typehandler;

import com.google.common.collect.Lists;
import com.heretere.hac.api.config.processor.HybridHandler;
import com.heretere.hac.api.config.processor.exception.InvalidTypeException;
import org.jetbrains.annotations.NotNull;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.util.List;

public final class TomlStringHybridHandler implements HybridHandler<TomlParseResult, String> {
    @Override public @NotNull String deserialize(
        final @NotNull TomlParseResult parser,
        final @NotNull Class<?> exactType,
        final @NotNull String key
    ) throws InvalidTypeException {
        if (!parser.isString(key)) {
            throw new InvalidTypeException();
        }

        String output = parser.getString(key);

        if (output == null) {
            throw new InvalidTypeException();
        }

        return output;
    }

    @Override public @NotNull List<String> serialize(final @NotNull Object value) {
        return Lists.newArrayList("\"" + Toml.tomlEscape(this.getGenericType().cast(value)) + "\"");
    }

    @Override public @NotNull Class<String> getGenericType() {
        return String.class;
    }
}
