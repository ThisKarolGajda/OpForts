package me.opkarol.opforts.forts.models.buildings;

import java.io.Serializable;

public class FortBuildingProgress implements Serializable {
    private int allBlocks;
    private int blocksBuilt;

    public FortBuildingProgress(int allBlocks, int blocksBuilt) {
        this.allBlocks = allBlocks;
        this.blocksBuilt = blocksBuilt;
    }

    public FortBuildingProgress() {
        this(0, 0);
    }

    public void isBuildingCompleted() {
        if (blocksBuilt >= allBlocks) {
            blocksBuilt = allBlocks;
        }
    }

    public boolean isBuilding() {
        return allBlocks > 0;
    }

    public int getBlocksBuilt() {
        return blocksBuilt;
    }

    public void setBlocksBuilt(int blocksBuilt) {
        this.blocksBuilt = blocksBuilt;
    }

    public int getAllBlocks() {
        return allBlocks;
    }

    public void setAllBlocks(int allBlocks) {
        this.allBlocks = allBlocks;
    }
}
