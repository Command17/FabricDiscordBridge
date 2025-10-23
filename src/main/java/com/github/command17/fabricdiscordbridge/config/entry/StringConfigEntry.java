package com.github.command17.fabricdiscordbridge.config.entry;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class StringConfigEntry extends ConfigEntry<String> {
    private Component component;

    public StringConfigEntry(@NotNull String key, String value) {
        super(key, value);
        this.component = Component.literal(value);
    }

    @Override
    public void readFromProperties(Properties properties) {
        this.setValue(properties.getProperty(this.getKey(), this.getDefaultValue()));
        this.component = Component.literal(this.get());
    }

    @Override
    public void writeToProperties(Properties properties) {
        properties.setProperty(this.getKey(), this.get());
    }

    public Component asLiteralComponent() {
        return component;
    }
}
