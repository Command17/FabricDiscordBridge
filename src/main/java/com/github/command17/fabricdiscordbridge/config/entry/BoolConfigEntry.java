package com.github.command17.fabricdiscordbridge.config.entry;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class BoolConfigEntry extends ConfigEntry<Boolean> {
    public BoolConfigEntry(@NotNull String key, boolean value) {
        super(key, value);
    }

    @Override
    public void readFromProperties(Properties properties) {
        String rawValue = properties.getProperty(this.getKey(), String.valueOf(this.get()));
        boolean value = Boolean.parseBoolean(rawValue);
        this.setValue(value);
    }

    @Override
    public void writeToProperties(Properties properties) {
        properties.setProperty(this.getKey(), String.valueOf(this.get()));
    }
}
