package me.opkarol.opforts.forts.models;

import me.opkarol.opforts.forts.database.FortsDatabase;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oporm.SerializableFieldOrm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.Serializable;
import java.util.UUID;

public record FortId(UUID fortId) implements Serializable, SerializableFieldOrm {

    public Fort getFort() {
        return Plugin.getDependency().get(FortsDatabase.class).getFort(this);
    }

    @Override
    public String serialize() {
        return fortId.toString();
    }

    @Override
    public @NotNull @Unmodifiable Object deserialize(String s) {
        return UUID.fromString(s);
    }
}
