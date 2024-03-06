package me.opkarol.opforts.forts.commands;

import me.opkarol.opforts.forts.FortHandler;
import me.opkarol.opforts.forts.FortHighlighter;
import me.opkarol.opforts.forts.building.BuildingPreviewManager;
import me.opkarol.opforts.forts.building.BuildingSchematic;
import me.opkarol.opforts.forts.building.BuildingsPreviewSchematicsOrderType;
import me.opkarol.opforts.forts.database.FortsDatabase;
import me.opkarol.opforts.forts.inventories.*;
import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.opforts.forts.models.buildings.FortBuilding;
import me.opkarol.opforts.forts.models.buildings.FortBuildingType;
import me.opkarol.opforts.forts.models.chunks.ChunkLocation;
import me.opkarol.opforts.forts.npc.NPCPlayerSpawner;
import me.opkarol.opforts.safeinventory.SafeInventoryManager;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.commands.annotations.Command;
import me.opkarol.oplibrary.commands.annotations.Subcommand;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Command("fort")
public class FortCommand {

    @Subcommand("create")
    public void createFort(@NotNull Player player) {
        if (FortHandler.isOwnerOfAnyFort(player.getUniqueId())) {
            player.sendMessage("You already have a fort!");
            return;
        }

        new Fort(player, "Fort " + player.getName());
        player.sendMessage("Fort created!");
    }

    @Subcommand("delete")
    public void deleteFort(@NotNull Player player) {
        Optional<Fort> optional = FortHandler.getOwnerFort(player.getUniqueId());
        if (optional.isEmpty()) {
            player.sendMessage("You don't have a fort!");
            return;
        }

        FortHandler.delete(optional.get());
        player.sendMessage("Fort deleted!");
    }

    @Subcommand("display-all")
    public void displayAllForts(@NotNull Player player) {
        player.sendMessage("All forts: ");
        for (Fort fort : Plugin.getDependency().get(FortsDatabase.class).getAllForts()) {
            player.sendMessage(fort.basicInformation.getName());
        }
    }

    @Subcommand("highlight")
    public void highlight(@NotNull Player player) {
        Optional<Fort> optional = FortHandler.getFortAtLocation(player.getLocation());
        if (optional.isEmpty()) {
            player.sendMessage("You are not inside any fort!");
            return;
        }

        Fort fort = optional.get();
        FortHighlighter.highlightFort(fort, player.getLocation().getY());
    }

    @Subcommand("tp")
    public void tp(@NotNull Player player) {
        Optional<Fort> optional = FortHandler.getOwnerFort(player.getUniqueId());
        if (optional.isEmpty()) {
            player.sendMessage("Fort not found!");
            return;
        }

        Fort fort = optional.get();
        player.teleport(fort.basicInformation.getCenter().getLocation());
        player.sendMessage("Teleported to the fort!");
    }

    @Subcommand("manage")
    public void manage(@NotNull Player player) {
        Optional<Fort> optional = FortHandler.getOwnerFort(player.getUniqueId());
        if (optional.isEmpty()) {
            player.sendMessage("Fort not found!");
            return;
        }

        Fort fort = optional.get();
        new FortManageInventory(player, fort);
    }

    @Subcommand("manage-chunk")
    public void manageChunk(@NotNull Player player) {
        Optional<Fort> optional = FortHandler.getFortAtLocation(player.getLocation());
        if (optional.isEmpty()) {
            player.sendMessage("Fort not found!");
            return;
        }

        Fort fort = optional.get();
        if (FortHandler.isChunkTaken(player.getLocation().getChunk(), fort)) {
            new FortChunkManageInventory(player, fort, player.getLocation().getChunk());
            return;
        }

        player.sendMessage("You are not inside any fort chunk!");
    }

    @Subcommand("set-building")
    public void setBuilding(@NotNull Player player, String type) {
        Optional<Fort> optional = FortHandler.getFortAtLocation(player.getLocation());
        if (optional.isEmpty()) {
            player.sendMessage("Fort not found!");
            return;
        }

        Fort fort = optional.get();
        fort.buildings.addBuilding(new FortBuilding(FortBuildingType.valueOf(type), 1, ChunkLocation.from(player.getLocation())), fort);
        player.sendMessage("Building set!");
    }

    @Subcommand("buildings-preview")
    public void buildingsPreview(Player player) {
        new FortBuildingsPreviewInventory(player);
    }

    @Subcommand("test-npc-with-player-spawn")
    public void spawnNpc(@NotNull Player player) {
        NPCPlayerSpawner.spawn(player, player.getLocation());
        player.teleport(new Location(player.getWorld(), 0, 100, 0));
    }

    @Subcommand("test-safe-inventory-save")
    public void testSafeInventorySave(Player sender) {
        SafeInventoryManager.saveAndClearHotbar(sender);
    }

    @Subcommand("test-safe-inventory-retrieve")
    public void testSafeInventoryRetrieve(Player sender) {
        SafeInventoryManager.restoreHotbar(sender);
    }

    @Subcommand("display-preview")
    public void displayPreview(@NotNull Player player) {
        Chunk chunk = player.getLocation().getChunk();

        BuildingPreviewManager.startPreview(player, chunk, BuildingsPreviewSchematicsOrderType.HOUSE, 10);
    }

    @Subcommand("required-blocks-for-building")
    public void requiredBlocksForBuilding(@NotNull Player player, String buildingType) {
        BuildingSchematic schematic = BuildingSchematic.fromSerializedFile(buildingType);
        new FortBuildingBlocksRequired(player, schematic);
    }

    @Subcommand("vault")
    public void vault(@NotNull Player player) {
        Optional<Fort> optional = FortHandler.getOwnerFort(player.getUniqueId());
        if (optional.isEmpty()) {
            player.sendMessage("Fort not found!");
            return;
        }

        Fort fort = optional.get();
        new FortVaultInventory(player, fort);
    }
}
