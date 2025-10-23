package com.github.command17.fabricdiscordbridge.config;

import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import com.github.command17.fabricdiscordbridge.config.entry.ConfigEntry;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public record ConfigDefinition(ImmutableMap<String, ConfigEntry<?>> entries, Path configPath, @Nullable String comments) {
    public ConfigEntry<?> getEntry(@NotNull String key) {
        return this.entries.get(key);
    }

    public void loadFromProperties(Properties properties) {
        this.entries.values().forEach((entry) -> entry.readFromProperties(properties));
    }

    public void saveIntoProperties(Properties properties) {
        this.entries.values().forEach((entry) -> entry.writeToProperties(properties));
    }

    public void resetToDefault() {
        this.entries.values().forEach(ConfigEntry::resetToDefault);
    }

    public void createOrLoad() {
        if (!load()) {
            save();
        }
    }

    public boolean load() {
        if (Files.exists(this.configPath)) {
            try (BufferedReader reader = Files.newBufferedReader(this.configPath)) {
                Properties properties = new Properties();
                properties.load(reader);
                this.loadFromProperties(properties);
                return true;
            } catch (IOException e) {
                FabricDiscordBridge.LOGGER.error("An error occurred while reading config {}! Exception:", this.configPath, e);
            }
        }

        return false;
    }

    public void save() {
        if (!Files.exists(this.configPath)) {
            try {
                Files.createDirectories(this.configPath.getParent());
                Files.createFile(this.configPath);
            } catch (IOException e) {
                FabricDiscordBridge.LOGGER.error("An error occurred while creating file {}! Exception:", this.configPath, e);
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(this.configPath)) {
            Properties properties = new Properties();
            this.saveIntoProperties(properties);
            properties.store(writer, this.comments);
        } catch (IOException e) {
            FabricDiscordBridge.LOGGER.error("An error occurred while saving config {}! Exception:", this.configPath, e);
        }
    }
}
