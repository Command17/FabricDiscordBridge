package com.github.command17.fabricdiscordbridge.config;

import com.github.command17.fabricdiscordbridge.config.entry.*;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ConfigBuilder {
    private final ImmutableMap.Builder<String, ConfigEntry<?>> entries = new ImmutableMap.Builder<>();

    @Nullable
    private String comments = null;

    @NotNull
    private String name;

    @NotNull
    private String fileFormat = "properties";

    @NotNull
    private Path parentPath = FabricLoader.getInstance().getConfigDir();

    public ConfigBuilder(String name) {
        this.name = name;
    }

    public<T extends ConfigEntry<?>> T defineEntry(@NotNull String key, T entry) {
        this.entries.put(key, entry);
        return entry;
    }

    public<E extends Enum<E> & StringRepresentable> EnumConfigEntry<E> defineEnum(@NotNull String key, E value, Class<E> enumClass, List<E> enumValues) {
        return defineEntry(key, new EnumConfigEntry<>(key, value, enumClass, enumValues));
    }

    public<E extends Enum<E> & StringRepresentable> EnumConfigEntry<E> defineEnum(@NotNull String key, E value, Class<E> enumClass) {
        return defineEntry(key, new EnumConfigEntry<>(key, value, enumClass, List.of(enumClass.getEnumConstants())));
    }

    @SuppressWarnings("unchecked")
    public<E extends Enum<E> & StringRepresentable> EnumConfigEntry<E> defineEnum(@NotNull String key, E value) {
        return defineEntry(key, new EnumConfigEntry<>(key, value, (Class<E>) value.getClass(), (List<E>) List.of(value.getClass().getEnumConstants())));
    }

    public IntConfigEntry defineInt(@NotNull String key, int value, int min, int max) {
        return defineEntry(key, new IntConfigEntry(key, value, min, max));
    }

    public IntConfigEntry defineInt(@NotNull String key, int value) {
        return defineInt(key, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public LongConfigEntry defineLong(@NotNull String key, long value, long min, long max) {
        return defineEntry(key, new LongConfigEntry(key, value, min, max));
    }

    public LongConfigEntry defineLong(@NotNull String key, long value) {
        return defineLong(key, value, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public BoolConfigEntry defineBool(@NotNull String key, boolean value) {
        return defineEntry(key, new BoolConfigEntry(key, value));
    }

    public StringConfigEntry defineString(@NotNull String key, String value) {
        return this.defineEntry(key, new StringConfigEntry(key, value));
    }

    public StringConfigEntry defineString(@NotNull String key) {
        return this.defineEntry(key, new StringConfigEntry(key, ""));
    }

    public ConfigBuilder setComments(@Nullable String comments) {
        this.comments = comments;
        return this;
    }

    public ConfigBuilder setFileFormat(@NotNull String fileFormat) {
        this.fileFormat = fileFormat;
        return this;
    }

    public ConfigBuilder setParentPath(@NotNull Path parentPath) {
        this.parentPath = parentPath;
        return this;
    }

    public ConfigBuilder setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public ConfigDefinition build() {
        return new ConfigDefinition(
                this.entries.build(),
                this.parentPath.resolve(this.name + "." + this.fileFormat).toAbsolutePath(),
                this.comments
        );
    }
}
