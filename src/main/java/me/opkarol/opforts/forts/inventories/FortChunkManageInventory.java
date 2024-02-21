package me.opkarol.opforts.forts.inventories;

import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.opforts.forts.models.buildings.FortBuilding;
import me.opkarol.opforts.forts.models.chunks.ChunkLocation;
import me.opkarol.opforts.utils.ChunkUtils;
import me.opkarol.opforts.utils.StringIconUtil;
import me.opkarol.oplibrary.inventories.ChestInventory;
import me.opkarol.oplibrary.inventories.ItemBuilder;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class FortChunkManageInventory extends ChestInventory {

    public FortChunkManageInventory(Player player, Fort fort, Chunk chunk) {
        super("fort_chunk_manage", 3);

        setItem("information", 11, new ItemBuilder(Material.STONE), event -> event.setCancelled(true), Map.of(
                "%chunk%", ChunkUtils.toString(chunk)
        ));

        Optional<FortBuilding> optional = fort.buildings.getBuildingByLocation(ChunkLocation.from(chunk));
        setItem("building", 15, new ItemBuilder(Material.BRICK), event -> {
            event.setCancelled(true);
            optional.ifPresent(building -> new FortBuildingManageInventory(player, building));
        }, Map.of(
                "%exists%", StringIconUtil.getReturnedEmojiFromBoolean(optional.isPresent()),
                "%type%", optional.isEmpty() ? "---" : optional.get().getType().toString(),
                "%level%", optional.map(fortBuilding -> String.valueOf(fortBuilding.getLevel())).orElse("---")

        ));

        fillEmptyWithBlank();
        open(player);
    }
}
    