package me.opkarol.opforts.forts.models.politics;

import me.opkarol.opforts.forts.models.politics.wars.FortWar;
import me.opkarol.oporm.SerializableFieldOrm;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class FortPolitics implements SerializableFieldOrm, Serializable {
    private List<UUID> allies;
    private List<UUID> enemies;
    private List<FortWar> wars;

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public Object deserialize(String s) {
        return null;
    }
}
