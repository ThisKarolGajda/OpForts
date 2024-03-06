package me.opkarol.opforts.forts.models;

import me.opkarol.opforts.forts.database.FortsDatabase;
import me.opkarol.opforts.forts.models.buildings.FortBuilding;
import me.opkarol.opforts.forts.models.buildings.FortBuildingType;
import me.opkarol.opforts.forts.models.buildings.FortBuildings;
import me.opkarol.opforts.forts.models.chunks.ChunkLocation;
import me.opkarol.opforts.forts.models.chunks.FortChunks;
import me.opkarol.opforts.forts.models.information.FortBasicInformation;
import me.opkarol.opforts.forts.models.members.FortMembers;
import me.opkarol.opforts.forts.models.politics.FortPolitics;
import me.opkarol.opforts.forts.models.vault.FortVault;
import me.opkarol.opforts.utils.ChunkUtils;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.location.OpSerializableLocation;
import me.opkarol.oporm.DatabaseEntity;
import me.opkarol.oporm.PrimaryKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Fort implements DatabaseEntity<FortId>, Serializable {
    @PrimaryKey
    public FortId fortId;
    public FortBasicInformation basicInformation;
    public FortBuildings buildings;
    public FortMembers members;
    public FortPolitics politics;
    public FortChunks chunks;
    public FortVault vault;

    public Fort(@NotNull Player owner, String name) {
        this.basicInformation = new FortBasicInformation(new OpSerializableLocation(ChunkUtils.getChunkCenter(owner.getLocation())), name, null);
        this.buildings = new FortBuildings();
        this.members = new FortMembers(owner.getUniqueId(), new ArrayList<>(), new ArrayList<>());
        this.politics = new FortPolitics();
        this.chunks = new FortChunks();
        this.fortId = Plugin.getDependency().get(FortsDatabase.class).getNotTakenFortId();
        this.vault = new FortVault();

        buildings.addBuilding(new FortBuilding(FortBuildingType.FORT, 0, ChunkLocation.from(basicInformation.getCenter())), this);
        save();
    }

    public Fort() {

    }

    @Override
    public FortId getId() {
        return fortId;
    }

    public void save() {
        Plugin.getDependency().get(FortsDatabase.class).saveFort(this);
    }
}
