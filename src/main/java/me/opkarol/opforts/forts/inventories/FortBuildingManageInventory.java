package me.opkarol.opforts.forts.inventories;

import me.opkarol.opforts.forts.models.buildings.FortBuilding;
import me.opkarol.oplibrary.inventories.ChestInventory;
import me.opkarol.oplibrary.inventories.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FortBuildingManageInventory extends ChestInventory {

    public FortBuildingManageInventory(Player player, @NotNull FortBuilding building) {
        super("fort_building_manage", 3);

        setItem("information", 12, new ItemBuilder(Material.STONE), event -> event.setCancelled(true), Map.of(
                "%level%", String.valueOf(building.getLevel()),
                "%type%", building.getType().toString()
        ));

        setItem("upgrade", 14, new ItemBuilder(Material.STONE), event -> event.setCancelled(true), Map.of(
                "%level%", String.valueOf(building.getLevel())
        ));

        fillEmptyWithBlank();
        open(player);
    }
}
    