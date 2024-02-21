package me.opkarol.opforts.forts.listeners;

import me.opkarol.opforts.forts.FortHandler;
import me.opkarol.opforts.forts.inventories.FortBuildingManageInventory;
import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.opforts.forts.models.buildings.FortBuilding;
import me.opkarol.opforts.forts.models.chunks.ChunkLocation;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.listeners.BasicListener;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.persistence.PersistentDataType;

public class BuildingHeartInteractListener extends BasicListener {

    public BuildingHeartInteractListener() {
       runListener();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(PlayerArmorStandManipulateEvent event) {
        ArmorStand clickedEntity = event.getRightClicked();
        NamespacedKey key = new NamespacedKey(Plugin.getInstance(), "isBuildingHeart");
        if (clickedEntity.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String value = clickedEntity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if ("true".equals(value)) {
                event.setCancelled(true);

                Location location = clickedEntity.getLocation();
                Fort fort = FortHandler.getFortAtLocation(location).orElse(null);
                if (fort == null) {
                    // Should never happen
                    return;
                }

                FortBuilding building = fort.buildings.getBuildingByLocation(ChunkLocation.from(location)).orElse(null);
                if (building == null) {
                    // Should never happen
                    return;
                }

                new FortBuildingManageInventory(event.getPlayer(), building);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        if (event.getDamager() instanceof Player player && damagedEntity instanceof ArmorStand clickedEntity) {
            NamespacedKey key = new NamespacedKey(Plugin.getInstance(), "isBuildingHeart");
            if (clickedEntity.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                String value = clickedEntity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                if ("true".equals(value)) {
                    event.setCancelled(true);

                    Location location = damagedEntity.getLocation();
                    Fort fort = FortHandler.getFortAtLocation(location).orElse(null);
                    if (fort == null) {
                        // Should never happen
                        return;
                    }

                    FortBuilding building = fort.buildings.getBuildingByLocation(ChunkLocation.from(location)).orElse(null);
                    if (building == null) {
                        // Should never happen
                        return;
                    }

                    new FortBuildingManageInventory(player, building);
                }
            }}
    }
}
