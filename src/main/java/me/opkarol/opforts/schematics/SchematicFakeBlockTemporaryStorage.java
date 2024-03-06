package me.opkarol.opforts.schematics;

import me.opkarol.opforts.schematics.vectors.BlockVector;
import me.opkarol.oplibrary.listeners.BasicListener;
import me.opkarol.oplibrary.misc.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class SchematicFakeBlockTemporaryStorage extends BasicListener {
    private final Map<UUID, Tuple<World, List<BlockVector>>> fakeBlocks = new HashMap<>();

    public SchematicFakeBlockTemporaryStorage() {
        runListener();
    }

    public void addFakeBlocks(UUID uuid, Tuple<World, List<BlockVector>> blockVectorList) {
        fakeBlocks.put(uuid, blockVectorList);
    }

    public void addFakeBlock(UUID uuid, BlockVector vector, World world) {
        List<BlockVector> blockVectors = new ArrayList<>(getFakeBlocks(uuid));
        blockVectors.add(vector);
        fakeBlocks.put(uuid, new Tuple<>(world, blockVectors));
    }

    public List<BlockVector> getFakeBlocks(UUID uuid) {
        Tuple<World, List<BlockVector>> tuple = fakeBlocks.get(uuid);
        if (tuple == null) {
            return new ArrayList<>();
        }

        return tuple.second();
    }

    public void removeFakeBlocks(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            fakeBlocks.remove(uuid);
            return;
        }

        Tuple<World, List<BlockVector>> tuple = fakeBlocks.get(uuid);
        if (tuple == null || tuple.first() == null || tuple.second() == null) {
            return;
        }

        fakeBlocks.remove(uuid);
        for (BlockVector blockVector : tuple.second()) {
            Block block = tuple.first().getBlockAt(blockVector.x(), blockVector.y(), blockVector.z());
            player.sendBlockChange(block.getLocation(), block.getBlockData());
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        removeFakeBlocks(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerChangedWorldEvent event) {
        removeFakeBlocks(event.getPlayer().getUniqueId());
    }
}
