package com.heirteir.hac.api.util.reflections;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.heirteir.hac.api.util.reflections.helper.HelperManager;
import com.heirteir.hac.api.util.reflections.helper.StringHelper;
import com.heirteir.hac.api.util.reflections.types.WrappedClass;
import com.heirteir.hac.api.util.reflections.version.ServerVersion;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

@Getter
public final class Reflections {
    private final ServerVersion version;
    private final HelperManager helpers;

    public Reflections() {
        ServerVersion version1;
        try {
            version1 = ServerVersion.fromPackage(Iterables.get(Splitter.on('v').split(Bukkit.getServer().getClass().getPackage().getName()), 1));
        } catch (Exception e) {
            version1 = ServerVersion.MAX;
        }
        this.version = version1;
        this.helpers = new HelperManager();

        this.registerBaseHelpers();
    }

    private void registerBaseHelpers() {
        this.helpers.registerHelper(StringHelper.class, new StringHelper(this));
    }

    @Nullable
    public Class<?> getClassOrNull(String name) {
        Class<?> clazz;

        try {
            clazz = Class.forName(this.helpers.getHelper(StringHelper.class).replaceString(name));
        } catch (ClassNotFoundException ignored) {
            clazz = null;
        }

        return clazz;
    }

    public WrappedClass getNMSClass(String name) {
        return this.getClass("%nms%." + name);
    }

    public WrappedClass getCBClass(String name) {
        return this.getClass("%cb%." + name);
    }

    public WrappedClass getClass(Class<?> clazz) {
        return new WrappedClass(clazz);
    }

    public WrappedClass getClass(String name) {
        Class<?> clazz = this.getClassOrNull(name);

        Preconditions.checkNotNull(clazz, String.format("Class '%s' doesn't exist.", this.helpers.getHelper(StringHelper.class).replaceString(name)));

        return this.getClass(clazz);
    }
}
