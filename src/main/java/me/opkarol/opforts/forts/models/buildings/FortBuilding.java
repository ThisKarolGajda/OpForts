package me.opkarol.opforts.forts.models.buildings;

import me.opkarol.opforts.forts.models.chunks.ChunkLocation;

import java.io.Serializable;

public class FortBuilding implements Serializable {
    private FortBuildingProgress buildingProgress;
    private final FortBuildingType type;
    private int level;
    private final ChunkLocation location;

    public FortBuilding(FortBuildingType type, int level, ChunkLocation location) {
        this.buildingProgress = new FortBuildingProgress();
        this.type = type;
        this.level = level;
        this.location = location;
    }

    public FortBuildingProgress getBuildingProgress() {
        return buildingProgress;
    }

    public void setBuildingProgress(FortBuildingProgress buildingProgress) {
        this.buildingProgress = buildingProgress;
    }

    public FortBuildingType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ChunkLocation getLocation() {
        return location;
    }
}
