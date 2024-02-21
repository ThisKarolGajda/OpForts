package me.opkarol.opforts.forts.models.buildings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.opforts.forts.models.chunks.ChunkLocation;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oporm.SerializableFieldOrm;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FortBuildings implements SerializableFieldOrm, Serializable {
    private final List<FortBuilding> buildings;

    public FortBuildings(List<FortBuilding> buildings) {
        this.buildings = buildings;
    }

    public FortBuildings() {
        this.buildings = new ArrayList<>();
    }

    @Override
    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(buildings);
    }

    @Override
    public Object deserialize(String s) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<FortBuilding>>(){}.getType();
        return gson.fromJson(s, type);
    }

    public List<FortBuilding> getBuildings() {
        return buildings;
    }

    public Optional<FortBuilding> getBuildingByLocation(ChunkLocation location){
        return buildings.stream()
                .filter(building -> building.getLocation().equals(location))
                .findFirst();
    }

    public void addBuilding(FortBuilding building, @NotNull Fort fort) {
        getBuildings().removeAll(buildings.stream().filter(building1 -> building1.getLocation().equals(building.getLocation())).toList());
        getBuildings().add(building);
        fort.save();

        // Remove existing heart
        Arrays.stream(building.getLocation().getChunk(fort.basicInformation.getCenter().getWorld()).getEntities()).forEach(entity -> {
            if (entity instanceof ArmorStand armorStand) {
                NamespacedKey key = new NamespacedKey(Plugin.getInstance(), "isBuildingHeart");
                if (armorStand.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    String value = armorStand.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                    if ("true".equals(value)) {
                        armorStand.remove();
                    }
                }
            }
        });

        if (building.getLevel() == 0) {
            BuildingHeartManager.createBuildingHeart(building.getLocation().getCenter(fort.basicInformation.getCenter().getWorld()));
        } else {
            //TODO
        }
    }
}
