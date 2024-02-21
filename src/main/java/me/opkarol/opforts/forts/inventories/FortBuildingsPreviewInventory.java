package me.opkarol.opforts.forts.inventories;

import me.opkarol.opforts.forts.models.buildings.FortBuildingType;
import me.opkarol.oplibrary.inventories.ChestInventory;
import me.opkarol.oplibrary.inventories.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class FortBuildingsPreviewInventory extends ChestInventory {

    public FortBuildingsPreviewInventory(Player player) {
        super("fort_buildings_preview", 3);

        for (FortBuildingType type : FortBuildingType.values()) {
            setNextFree("building", new ItemBuilder(Material.HEART_OF_THE_SEA), event -> event.setCancelled(true), Map.of(
                    "%type%", type.toString(),
                    "%description%", type.getDescription()
            ));
        }

        fillEmptyWithBlank();
        open(player);
    }
}
    