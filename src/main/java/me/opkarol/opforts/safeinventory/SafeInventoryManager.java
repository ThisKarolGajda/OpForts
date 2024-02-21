package me.opkarol.opforts.safeinventory;

import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.autostart.IDisable;
import me.opkarol.oplibrary.autostart.OpAutoDisable;
import me.opkarol.oplibrary.listeners.BasicListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SafeInventoryManager extends BasicListener implements IDisable {
    private static final Map<Player, ItemStack[]> savedHotbarSlots = new HashMap<>();

    public SafeInventoryManager() {
        runListener();
        OpAutoDisable.add(this);
    }

    public static void saveAndClearHotbar(Player player) {
        ItemStack[] hotbar = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            hotbar[i] = player.getInventory().getItem(i);
            player.getInventory().setItem(i, null);
        }

        savedHotbarSlots.put(player, hotbar);
    }

    public static void restoreHotbar(Player player) {
        File schematicsFolder = new File(Plugin.getInstance().getDataFolder(), "inventories");
        File playerInventoryFile = new File(schematicsFolder, player.getUniqueId() + ".opinv");
        if (playerInventoryFile.exists()) {
            try (FileInputStream fileIn = new FileInputStream(playerInventoryFile);
                 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                ItemStack[] hotbar = InventorySerializer.stacksFromBase64((String) objectIn.readObject());
                savedHotbarSlots.put(player, hotbar);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            playerInventoryFile.delete();
        }

        ItemStack[] hotbar = savedHotbarSlots.get(player);
        if (hotbar != null) {
            for (int i = 0; i < 9; i++) {
                player.getInventory().setItem(i, hotbar[i]);
            }

            savedHotbarSlots.remove(player);
        }
    }

    @Override
    public void onDisable() {
        File inventoriesFolder = new File(Plugin.getInstance().getDataFolder(), "inventories");
        if (!inventoriesFolder.exists()) {
            inventoriesFolder.mkdirs();
        }

        for (Map.Entry<Player, ItemStack[]> entry : savedHotbarSlots.entrySet()) {
            Player player = entry.getKey();
            ItemStack[] hotbar = entry.getValue();
            File playerInventoryFile = new File(inventoriesFolder, player.getUniqueId() + ".opinv");

            try (FileOutputStream fileOut = new FileOutputStream(playerInventoryFile);
                 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
                objectOut.writeObject(InventorySerializer.toBase64(hotbar));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        restoreHotbar(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!savedHotbarSlots.containsKey(player)) {
            return;
        }

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE ||
                 event.getClickedInventory() != null &&
                         (event.getClickedInventory().equals(player.getInventory()) ||
                event.getClickedInventory().getType() == InventoryType.CRAFTING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (savedHotbarSlots.containsKey(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!savedHotbarSlots.containsKey(player)) {
                return;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (savedHotbarSlots.containsKey((Player) event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (savedHotbarSlots.containsKey(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!savedHotbarSlots.containsKey(player)) {
                return;
            }

            event.setCancelled(true);
        }
    }
}
