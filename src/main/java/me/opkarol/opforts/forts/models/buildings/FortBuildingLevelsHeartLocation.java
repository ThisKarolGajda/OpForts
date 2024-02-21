package me.opkarol.opforts.forts.models.buildings;

import java.util.HashMap;
import java.util.Map;

public class FortBuildingLevelsHeartLocation {
    private final Map<Integer, Object> levelsMap;

    public FortBuildingLevelsHeartLocation(Map<Integer, Object> levelsMap) {
        this.levelsMap = levelsMap;
    }

    public FortBuildingLevelsHeartLocation() {
        this.levelsMap = new HashMap<>();
    }

    public Object get(int level) {
        return levelsMap.getOrDefault(level, null);
    }
    //TODO rework this class
}
