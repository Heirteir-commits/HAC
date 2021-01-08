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

package com.heretere.hac.core;

import com.heretere.hac.api.HACAPI;
import com.heretere.hac.api.config.collection.ConfigList;
import com.heretere.hac.api.config.structure.annotation.Comment;
import com.heretere.hac.api.config.structure.annotation.ConfigFile;
import com.heretere.hac.api.config.structure.annotation.Key;
import com.heretere.hac.api.config.structure.annotation.Section;
import com.heretere.hac.api.config.structure.builder.ConfigBuilder;
import com.heretere.hac.api.config.structure.builder.ConfigPathBuilder;
import org.bukkit.Bukkit;

/**
 * You can make configs with a builder or with annotations this class shows both ways.
 */
@ConfigFile("test/test_annotations.toml")
@Comment("Comment 1")
@Comment("Comment 2")
@Comment("Comment 3")
@Section("section.to.add.comments")
public class ConfigExample {
    /* Type 1 Annotations */
    @Key("section.to.add.comments.value")
    @Comment("Add comment to say what's up")
    private Boolean value = false;

    @Key("section.to.add.comments.name")
    @Comment("Yo that's a name bro")
    private String name = "Squad";

    @Key("section.to.add.comments.enabled")
    @Comment("Ayo bro enable this")
    private Boolean enabled = true;

    @Key("section.to.add.comments.enum")
    @Comment("Can define enums")
    private Test enumTest = Test.TESTING_1;

    @Key("section.to.add.comments.enumlist")
    @Comment("Collections are supported")
    private ConfigList<Test> listTest = ConfigList.newInstance(Test.class, Test.TESTING_1, Test.TESTING_2);

    /* Type 2 Builder Example */
    private Boolean valueBuilder = false;
    private String nameBuilder = "Squad";
    private Boolean enabledBuilder = false;
    private Test enumTestBuilder = Test.TESTING_2;

    public ConfigExample() {
        /* Usually I would use DI to get the HACAPI instance but since this is just a one off class i'll use the
        service manager*/

        Bukkit.getServicesManager().load(HACAPI.class).getConfigHandler().loadConfigClass(this);

        System.out.println(this.listTest);

        this.createBuilderStuffs();
    }

    /* Builder Type */
    private void createBuilderStuffs() {
        ConfigBuilder.builder(Bukkit.getServicesManager().load(HACAPI.class))
                     .setRelativePath("test/test_builder.toml")
                     .addConfigPath(
                         ConfigPathBuilder
                             .sectionBuilder()
                             .setKey("section.to.add.comments")
                             .addComment("Comment 1")
                             .addComment("Comment 2")
                             .addComment("Comment 3")
                             .build()
                     )
                     .addConfigPath(
                         ConfigPathBuilder
                             .fieldBuilder(Boolean.class)
                             .setKey("section.to.add.comments.value")
                             .addComment("Add comment to say what's up")
                             .setGetterSupplier(() -> this.valueBuilder)
                             .setSetterConsumer(bool -> this.valueBuilder = bool)
                             .build()
                     )
                     .addConfigPath(
                         ConfigPathBuilder
                             .fieldBuilder(String.class)
                             .setKey("section.to.add.comments.name")
                             .addComment("Yo that's a name bro")
                             .setGetterSupplier(() -> this.nameBuilder)
                             .setSetterConsumer(name -> this.nameBuilder = name)
                             .build()
                     )
                     .addConfigPath(
                         ConfigPathBuilder
                             .fieldBuilder(Boolean.class)
                             .setKey("section.to.add.comments.enabled")
                             .addComment("Ayo bro enable this")
                             .setGetterSupplier(() -> this.enabledBuilder)
                             .setSetterConsumer(bool -> this.enabledBuilder = bool)
                             .build()
                     )
                     .addConfigPath(
                         ConfigPathBuilder
                             .fieldBuilder(Test.class)
                             .setKey("section.to.add.comments.enum")
                             .addComment("Can define enums")
                             .setGetterSupplier(() -> this.enumTestBuilder)
                             .setSetterConsumer(test -> this.enumTestBuilder = test)
                             .build()
                     )
                     .build();
    }

    private enum Test {
        TESTING_1,
        TESTING_2
    }
}
