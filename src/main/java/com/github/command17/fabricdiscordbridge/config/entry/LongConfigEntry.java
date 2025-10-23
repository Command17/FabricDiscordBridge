package com.github.command17.fabricdiscordbridge.config.entry;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class LongConfigEntry extends ConfigEntry<Long> {
    private final long min;
    private final long max;

    public LongConfigEntry(@NotNull String key, long value, long min, long max) {
        super(key, value);
        this.min = min;
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    @Override
    public void setValue(@NotNull Long value) {
        super.setValue(Math.clamp(value, this.min, this.max));
    }

    @Override
    public void readFromProperties(Properties properties) {
        this.setValue(Long.parseLong(properties.getProperty(this.getKey(), String.valueOf(this.get()))));
    }

    @Override
    public void writeToProperties(Properties properties) {
        properties.setProperty(this.getKey(), String.valueOf(this.get()));
    }
}
