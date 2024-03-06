package me.opkarol.opforts.forts.building;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.opkarol.opforts.safeinventory.SafeInventoryManager;
import me.opkarol.opforts.schematics.Schematic;
import me.opkarol.opforts.schematics.SchematicFakeBlockTemporaryStorage;
import me.opkarol.opforts.schematics.SchematicsCache;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.inventories.ItemBuilder;
import me.opkarol.oplibrary.listeners.BasicListener;
import me.opkarol.oplibrary.location.OpSerializableLocation;
import me.opkarol.oplibrary.tools.PDCTools;
import me.opkarol.oplibrary.wrappers.OpBlockHighlighter;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class BuildingPreviewManager extends BasicListener {
    private static final ItemStack leavePreview = new ItemBuilder(Material.BARRIER).setName("&cLeave preview").applyPDC("forts", "leavePreview");
    private static final ItemStack nextPreview = new ItemBuilder(Material.ARROW).setName("&aNext preview").applyPDC("forts", "nextPreview");
    private static final ItemStack previousPreview = new ItemBuilder(Material.ARROW).setName("&aPrevious preview").applyPDC("forts", "previousPreview");
    private static final ItemStack selectPreview = new ItemBuilder(Material.BOOK).setName("&aSelect preview").applyPDC("forts", "selectPreview");

    private static final Map<UUID, PlayerPreviewData> playerPreviews = new HashMap<>();

    public BuildingPreviewManager() {
        runListener();
    }

    /**
     * Start preview of the building for the player
     *
     * @param player              Player to start the preview for
     * @param chunk               Chunk to preview the building in
     * @param orderType           Type of the order of the schematics
     * @param speedBlocksPerSecond Speed of the preview in blocks per second
     */
    public static void startPreview(Player player, Chunk chunk, BuildingsPreviewSchematicsOrderType orderType, double speedBlocksPerSecond) {
        BuildingPreviewManager.displayPreview(player, chunk, orderType, schematicName -> {
            Plugin.getDependency().get(SchematicFakeBlockTemporaryStorage.class).removeFakeBlocks(player.getUniqueId());
            BuildingSchematic schematic = Schematic.fromSerializedFile(schematicName);

            List<Block> blocks = schematic.getAllBlocksThatNeedToBeEmpty(chunk);
            if (blocks.size() > 0) {
                player.sendMessage("There are blocks in the way of the schematic! Clear them!");
                for (Block block : blocks) {
                    OpBlockHighlighter highlighter = new OpBlockHighlighter(block, Particle.FLAME);
                    highlighter.highlightFor(player, 30);
                }

                return;
            }

            Hologram hologram = DHAPI.createHologram(player.getName() + chunk.getX() + "-" + chunk.getZ(), schematic.getSpecialBlockLocation(chunk), List.of(""));
            schematic.build(chunk, 1 / speedBlocksPerSecond, (progress, block) -> DHAPI.setHologramLines(hologram, List.of(
                    "Progress: " + progress + " of max " + schematic.getBlockCountWithoutAir(),
                    "Current block at " + new OpSerializableLocation(block.getLocation()).toFamilyString(),
                    "Schematic: " + schematicName,
                    "Chunk: " + chunk.getX() + ", " + chunk.getZ(),
                    "Player: " + player.getName(),
                    "Speed: " + speedBlocksPerSecond + " blocks per second",
                    "Time left: " + Math.round((schematic.getBlockCountWithoutAir() - progress) / speedBlocksPerSecond) + " seconds"
            )), hologram::delete);
        });
    }

    private static void displayPreview(Player player, Chunk chunk, BuildingsPreviewSchematicsOrderType orderType, Consumer<String> schematicSelectedCallback) {
        SafeInventoryManager.saveAndClearHotbar(player);
        player.getInventory().setHeldItemSlot(3);
        player.getInventory().setItem(3, nextPreview);
        player.getInventory().setItem(4, selectPreview);
        player.getInventory().setItem(5, previousPreview);
        player.getInventory().setItem(7, leavePreview);

        playerPreviews.put(player.getUniqueId(), new PlayerPreviewData(chunk, 0, orderType, schematicSelectedCallback));
        displaySchematic(player);
    }

    private static void leavePreview(Player player) {
        SafeInventoryManager.restoreHotbar(player);
        playerPreviews.remove(player.getUniqueId());
        Plugin.getDependency().get(SchematicFakeBlockTemporaryStorage.class).removeFakeBlocks(player.getUniqueId());
    }

    private static void displaySchematic(@NotNull Player player) {
        PlayerPreviewData previewData = playerPreviews.get(player.getUniqueId());
        String schematicName = previewData.getOrderType().getSchematics().get(previewData.getCurrentSchematicIndex());
        Schematic schematic = SchematicsCache.get(schematicName);
        schematic.displayPreview(player, previewData.getChunk());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerUseItems(PlayerInteractEvent event) {
        if (event.getItem() == null || !SafeInventoryManager.isHotbarSaved(event.getPlayer()) || !playerPreviews.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        Player player = event.getPlayer();
        PlayerPreviewData previewData = playerPreviews.get(player.getUniqueId());
        if (previewData == null) return;

        String key = PDCTools.getNBT(event.getItem(), new NamespacedKey(Plugin.getInstance(), "forts"));
        if (key == null) {
            return;
        }

        switch (key) {
            case "leavePreview" -> {
                event.setCancelled(true);
                leavePreview(player);
            }
            case "nextPreview" -> {
                event.setCancelled(true);
                previewData.nextSchematic();
                displaySchematic(player);
            }
            case "previousPreview" -> {
                event.setCancelled(true);
                previewData.previousSchematic();
                displaySchematic(player);
            }
            case "selectPreview" -> {
                event.setCancelled(true);
                previewData.getSchematicSelectedCallback().accept(previewData.getOrderType().getSchematics().get(previewData.getCurrentSchematicIndex()));
                leavePreview(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!playerPreviews.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        leavePreview(event.getPlayer());
    }
}

class PlayerPreviewData {
    private final Chunk chunk;
    private int currentSchematicIndex;
    private final BuildingsPreviewSchematicsOrderType orderType;
    private final Consumer<String> schematicSelectedCallback;

    public PlayerPreviewData(Chunk chunk, int currentSchematicIndex, BuildingsPreviewSchematicsOrderType orderType, Consumer<String> schematicSelectedCallback) {
        this.chunk = chunk;
        this.currentSchematicIndex = currentSchematicIndex;
        this.orderType = orderType;
        this.schematicSelectedCallback = schematicSelectedCallback;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public int getCurrentSchematicIndex() {
        return currentSchematicIndex;
    }


    public void nextSchematic() {
        currentSchematicIndex = (currentSchematicIndex + 1) % getOrderType().getSchematics().size();
    }

    public void previousSchematic() {
        currentSchematicIndex = (currentSchematicIndex - 1 + getOrderType().getSchematics().size()) % getOrderType().getSchematics().size();
    }

    public Consumer<String> getSchematicSelectedCallback() {
        return schematicSelectedCallback;
    }

    public BuildingsPreviewSchematicsOrderType getOrderType() {
        return orderType;
    }
}