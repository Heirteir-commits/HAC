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

package com.heretere.hac.api.event.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation is used to define the priority for a {@link com.heretere.hac.api.event.executor.EventExecutor}.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Priority {
    /**
     * An integer that represents the priority of an event executor.
     * {@link Priority.Level} should be used over a magic number.
     * <p>
     * 0 - first priority
     * 1 -> second priority
     * 2... so on
     *
     * @return The level of this priority.
     */
    int value() default Level.CHECK;

    /**
     * Some defined priorities to reduce magic number usage.
     */
    final class Level {
        private Level() {
            throw new IllegalStateException("Utility Class.");
        }

        /**
         * Has a value of 0, indicates that an event executor should before most executors.
         * This priority should only be used for logging purposes. Use PRE_PROCESS for packet validation.
         */
        public static final int PRE_PROCESS = 0;
        /**
         * Has a value of 100, indicates that an event executor should be ran before any updaters are ran.
         * Should only use to make sure that a packet is valid before it being passed to an updater.
         */
        public static final int PRE_UPDATER = 100;
        /**
         * Has a value of 200, indicates that an event executor is an updater class.
         * Updater classes are used to update information that is later used by a check.
         */
        public static final int UPDATER = 200;
        /**
         * Has a value of 300, indicates that an event executor should be ran after an updater.
         * This should be used if as another updater level in case an event executor depends on a value by a
         * previously ran updater.
         */
        public static final int POST_UPDATER = 300;
        /**
         * Has a value of 400, indicates that an event executor should be ran last.
         * This should be used for check classes that depend on output from previously ran event executors.
         */
        public static final int CHECK = 400;
    }
}
