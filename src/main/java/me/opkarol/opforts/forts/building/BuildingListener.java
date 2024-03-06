package me.opkarol.opforts.forts.building;

import com.jeff_media.customblockdata.CustomBlockData;
import me.opkarol.opforts.NamespacedKeyType;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.listeners.Listener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BuildingListener extends Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDestroyBlock(BlockBreakEvent event) {
        Block block = event.getBlock();

        PersistentDataContainer customBlockData = new CustomBlockData(block, Plugin.getInstance());
        if (customBlockData.has(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You can't destroy this!");
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onIgniteBlock(BlockIgniteEvent event) {
        Block block = event.getBlock();

        PersistentDataContainer customBlockData = new CustomBlockData(block, Plugin.getInstance());
        if (customBlockData.has(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFireSpreadBlock(BlockSpreadEvent event) {
        Block block = event.getBlock();

        PersistentDataContainer customBlockData = new CustomBlockData(block, Plugin.getInstance());
        if (customBlockData.has(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }

        customBlockData = new CustomBlockData(event.getSource(), Plugin.getInstance());
        if (customBlockData.has(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDestroyBlockByWaterOrLava(BlockFromToEvent event) {
        Block block = event.getBlock();

        PersistentDataContainer customBlockData = new CustomBlockData(block, Plugin.getInstance());
        if (customBlockData.has(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }

        customBlockData = new CustomBlockData(event.getToBlock(), Plugin.getInstance());
        if (customBlockData.has(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityExplodeEvent(@NotNull EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            PersistentDataContainer customBlockData = new CustomBlockData(block, Plugin.getInstance());
            if (customBlockData.has(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING)) {
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        Block block = event.getBlock();

        PersistentDataContainer customBlockData = new CustomBlockData(block, Plugin.getInstance());
        if (customBlockData.has(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }

        if (event.getIgnitingBlock() == null) {
            return;
        }

        customBlockData = new CustomBlockData(event.getIgnitingBlock(), Plugin.getInstance());
        if (customBlockData.has(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING)) {
            event.setCancelled(true);
            event.getIgnitingBlock().setType(Material.AIR);
        }
    }
}
