package me.opkarol.opforts.forts.models.information;

import me.opkarol.oplibrary.location.OpSerializableLocation;
import me.opkarol.oporm.SerializableFieldOrm;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class FortBasicInformation implements SerializableFieldOrm, Serializable {
    private final OpSerializableLocation center;
    private String name;
    @Nullable
    private FortNameColor color;

    public FortBasicInformation(OpSerializableLocation center, String name, @Nullable FortNameColor color) {
        this.center = center;
        this.name = name;
        this.color = color;
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public Object deserialize(String s) {
        return null;
    }

    public OpSerializableLocation getCenter() {
        return center;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FortNameColor getColor() {
        return color;
    }

    public void setColor(FortNameColor color) {
        this.color = color;
    }
}
