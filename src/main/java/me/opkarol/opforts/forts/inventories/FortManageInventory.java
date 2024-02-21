package me.opkarol.opforts.forts.inventories;

import me.opkarol.opforts.forts.FortHandler;
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
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class FortManageInventory extends ChestInventory {

    public FortManageInventory(Player player, @NotNull Fort fort) {
        super("fort_manage_inventory", 5);

        setItemForChunk(player, fort, -2, -2, 2);
        setItemForChunk(player, fort, -1, -2, 3);
        setItemForChunk(player, fort, 0, -2, 4);
        setItemForChunk(player, fort, 1, -2, 5);
        setItemForChunk(player, fort, 2, -2, 6);
        setItemForChunk(player, fort, -2, -1, 11);
        setItemForChunk(player, fort, -1, -1, 12);
        setItemForChunk(player, fort, 0, -1, 13);
        setItemForChunk(player, fort, 1, -1, 14);
        setItemForChunk(player, fort, 2, -1, 15);
        setItemForChunk(player, fort, -2, 0, 20);
        setItemForChunk(player, fort, -1, 0, 21);
        setItemForChunk(player, fort, 0, 0, 22);
        setItemForChunk(player, fort, 1, 0, 23);
        setItemForChunk(player, fort, 2, 0, 24);
        setItemForChunk(player, fort, -2, 1, 29);
        setItemForChunk(player, fort, -1, 1, 30);
        setItemForChunk(player, fort, 0, 1, 31);
        setItemForChunk(player, fort, 1, 1, 32);
        setItemForChunk(player, fort, 2, 1, 33);
        setItemForChunk(player, fort, -2, 2, 38);
        setItemForChunk(player, fort, -1, 2, 39);
        setItemForChunk(player, fort, 0, 2, 40);
        setItemForChunk(player, fort, 1, 2, 41);
        setItemForChunk(player, fort, 2, 2, 42);

        fillEmptyWithBlank();
        open(player);
    }

    private void setItemForChunk(Player player, @NotNull Fort fort, int xOffset, int zOffset, int slot) {
        int x = fort.basicInformation.getCenter().getLocation().getChunk().getX();
        int z = fort.basicInformation.getCenter().getLocation().getChunk().getZ();
        Chunk chunk = ChunkUtils.getChunkAtChunkCoords(fort.basicInformation.getCenter().getWorld(), x + xOffset, z + zOffset);
        Optional<FortBuilding> optionalFortBuilding = fort.buildings.getBuildingByLocation(ChunkLocation.from(chunk));
        boolean isChunkTaken = FortHandler.isChunkTaken(chunk, fort);
        boolean isChunkCenter = isChunkTaken && FortHandler.isChunkCenter(chunk, fort);
        setItem("chunk", slot, new ItemBuilder(isChunkCenter ? Material.GREEN_WOOL : isChunkTaken ? Material.BLUE_WOOL : Material.RED_WOOL), event -> {
            event.setCancelled(true);
            if (FortHandler.isChunkTaken(chunk, fort)) {
                new FortChunkManageInventory(player, fort, chunk);
                return;
            }

            if (!FortHandler.isChunkNextToFort(chunk, fort)) {
                player.sendMessage("Chunk is not next to the fort!");
                return;
            }

            fort.chunks.addChunk(chunk);
            fort.save();
            player.sendMessage("Chunk taken!");
            new FortManageInventory(player, fort);
        }, Map.of(
                "%owned%", StringIconUtil.getReturnedEmojiFromBoolean(isChunkTaken),
                "%chunk%", ChunkUtils.toString(chunk),
                "%cost%", "TODO",
                "%building%", optionalFortBuilding.isEmpty() ? "---" : optionalFortBuilding.get().getType().toString()
        ));
    }
}
    