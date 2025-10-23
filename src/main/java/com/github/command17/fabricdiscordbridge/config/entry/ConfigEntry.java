package com.github.command17.fabricdiscordbridge.config.entry;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;
import java.util.function.Supplier;

public abstract class ConfigEntry<T> implements Supplier<T> {
    @NotNull
    private final String key;

    private final T defaultValue;
    private T value;

    public ConfigEntry(@NotNull String key, T value) {
        this.key = key;
        this.defaultValue = value;
        this.value = value;
    }

    public void resetToDefault() {
        this.setValue(this.getDefaultValue());
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public T get() {
        return this.getValue();
    }

    public T getValue() {
        return value;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public abstract void readFromProperties(Properties properties);
    public abstract void writeToProperties(Properties properties);
}
