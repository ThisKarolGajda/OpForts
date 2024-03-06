package me.opkarol.opforts.forts.inventories;

import me.opkarol.opforts.forts.FortHandler;
import me.opkarol.opforts.forts.building.BuildingSchematic;
import me.opkarol.opforts.utils.StringIconUtil;
import me.opkarol.oplibrary.inventories.ChestInventory;
import me.opkarol.oplibrary.inventories.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FortBuildingBlocksRequired extends ChestInventory {

    public FortBuildingBlocksRequired(Player player, @NotNull BuildingSchematic schematic) {
        super("fort_building_blocks_required", 4);

        setItemPreviousPage(34, player);
        setItemNextPage(35, player);
        setGlobalRowEmptyIfNotTaken(4);

        for (Map.Entry<Material, Integer> entry : schematic.getRequiredBlocksSortedByAmount()) {
            Material material = entry.getKey();
            int amount = entry.getValue();
            boolean ownedInVault = FortHandler.getOwnerFort(player).get().vault.hasEnoughItems(material, amount);
            boolean ownedInEnderChest = false;
            boolean ownedInInventory = false;

            setNextFree("block", new ItemBuilder(!material.isItem() ? Material.BARRIER : material, amount), event -> event.setCancelled(true), Map.of(
                    "%type%", material.name(),
                    "%amount%", String.valueOf(amount),
                    "%vault%", StringIconUtil.getReturnedEmojiFromBoolean(ownedInVault),
                    "%ender_chest%", StringIconUtil.getReturnedEmojiFromBoolean(ownedInEnderChest),
                    "%inventory%", StringIconUtil.getReturnedEmojiFromBoolean(ownedInInventory)
            ));
        }

        fillEmptyWithBlank();
        open(player);
    }
}
    