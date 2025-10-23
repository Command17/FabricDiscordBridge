package com.github.command17.fabricdiscordbridge.config.entry;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class IntConfigEntry extends ConfigEntry<Integer> {
    private final int min;
    private final int max;

    public IntConfigEntry(@NotNull String key, int value, int min, int max) {
        super(key, value);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public void setValue(@NotNull Integer value) {
        super.setValue(Math.clamp(value, this.min, this.max));
    }

    @Override
    public void readFromProperties(Properties properties) {
        try {
            String rawValue = properties.getProperty(this.getKey(), String.valueOf(this.get()));
            int value = Integer.parseInt(rawValue);
            this.setValue(value);
        } catch (NumberFormatException e) {
            this.setValue(this.getDefaultValue());
        }
    }

    @Override
    public void writeToProperties(Properties properties) {
        properties.setProperty(this.getKey(), String.valueOf(this.get()));
    }
}
