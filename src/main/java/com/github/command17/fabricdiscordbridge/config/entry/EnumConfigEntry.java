package com.github.command17.fabricdiscordbridge.config.entry;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Properties;

public class EnumConfigEntry<E extends Enum<E> & StringRepresentable> extends ConfigEntry<E> {
    private final Class<E> enumClass;
    private final List<E> enumValues;
    private final ImmutableMap<String, E> nameToEnumMap;

    public EnumConfigEntry(@NotNull String key, E value, Class<E> enumClass, List<E> enumValues) {
        super(key, value);
        this.enumClass = enumClass;
        this.enumValues = enumValues;
        if (this.enumValues.isEmpty()) {
            throw new IllegalArgumentException("Trying to make empty EnumConfigEntry '" + key + "'");
        }

        ImmutableMap.Builder<String, E> builder = new ImmutableMap.Builder<>();
        for (E enumVal: this.enumValues) {
            builder.put(enumVal.getSerializedName(), enumVal);
        }

        this.nameToEnumMap = builder.build();
    }

    public Class<E> getEnumClass() {
        return enumClass;
    }

    public List<E> getEnumValues() {
        return enumValues;
    }

    public E getEnumFromName(String name) {
        return this.nameToEnumMap.get(name);
    }

    @Override
    public void readFromProperties(Properties properties) {
        String rawValue = properties.getProperty(this.getKey(), this.getDefaultValue().getSerializedName());
        E value = this.getEnumFromName(rawValue);
        this.setValue(value);
    }

    @Override
    public void writeToProperties(Properties properties) {
        properties.setProperty(this.getKey(), this.get().getSerializedName());
    }
}
