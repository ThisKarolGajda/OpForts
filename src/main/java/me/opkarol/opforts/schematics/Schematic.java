package me.opkarol.opforts.schematics;

import me.opkarol.opforts.forts.building.BuildingSchematic;
import me.opkarol.opforts.schematics.vectors.BlockChunkVector;
import me.opkarol.opforts.schematics.vectors.BlockVector;
import me.opkarol.opforts.utils.ChunkUtils;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.runnable.OpRunnable;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Schematic {
    private final String name;
    private Map<BlockChunkVector, BlockData> blocks;

    public Schematic(String name, Map<BlockChunkVector, BlockData> blocks) {
        this.name = name;
        this.blocks = blocks;
    }

    public Schematic(String name) {
        this.name = name;
        this.blocks = new HashMap<>();
    }

    public static @NotNull BuildingSchematic fromSerialized(String serializedData) {
        BuildingSchematic schematic = new BuildingSchematic(null);
        schematic.deserialize(serializedData);
        return schematic;
    }

    public static @NotNull BuildingSchematic fromSerialized(String name, String serializedData) {
        BuildingSchematic schematic = new BuildingSchematic(name);
        schematic.deserialize(serializedData);
        return schematic;
    }

    public static @NotNull BuildingSchematic fromSerializedFile(String fileName) {
        BuildingSchematic schematic = new BuildingSchematic(null);
        schematic.deserializeFile(fileName);
        return schematic;
    }

    public static @NotNull BuildingSchematic fromSerializedFile(String name, String fileName) {
        BuildingSchematic schematic = new BuildingSchematic(name);
        schematic.deserializeFile(fileName);
        return schematic;
    }

    public static @NotNull BuildingSchematic fromChunk(Chunk chunk, int yStartLocation, int yEndLocation) {
        BuildingSchematic schematic = new BuildingSchematic(null);
        schematic.loadChunk(chunk, yStartLocation, yEndLocation);
        return schematic;
    }

    public static @NotNull BuildingSchematic fromChunk(String name, Chunk chunk, int yStartLocation, int yEndLocation) {
        BuildingSchematic schematic = new BuildingSchematic(name);
        schematic.loadChunk(chunk, yStartLocation, yEndLocation);
        return schematic;
    }

    public String getName() {
        return name;
    }

    public Map<BlockChunkVector, BlockData> getBlocks() {
        return blocks;
    }

    public void setBlocks(Map<BlockChunkVector, BlockData> blocks) {
        this.blocks = blocks;
    }

    public String serialize() {
        return SchematicHandler.serialize(blocks);
    }

    public void deserialize(String serializedData) {
        blocks = SchematicHandler.deserialize(serializedData);
    }

    public void serializeFile(String fileName) {
        SchematicFileHandler.serialize(fileName, serialize());
    }

    public void deserializeFile(String fileName) {
        deserialize(SchematicFileHandler.deserialize(fileName));
    }

    public void loadChunk(Chunk chunk, int yStartLocation, int yEndLocation) {
        blocks = SchematicHandler.loadChunk(chunk, yStartLocation, yEndLocation);
    }

    public void paste(Chunk chunk) {
        SchematicHandler.pasteSchematic(chunk, blocks, ChunkUtils.calculateOptimalChunkYHeight(chunk));
    }

    public BlockVector getDimensions() {
        return SchematicHandler.getDimensions(blocks.keySet());
    }

    public boolean isEmpty() {
        return name == null || blocks == null || blocks.isEmpty();
    }

    public void displayPreview(@NotNull Player player, Chunk chunk) {
        new OpRunnable(() -> {
            UUID uuid = player.getUniqueId();
            int blockY = ChunkUtils.calculateOptimalChunkYHeight(chunk);
            World world = player.getWorld();
            Plugin.getDependency().get(SchematicFakeBlockTemporaryStorage.class).removeFakeBlocks(uuid);

            for (int y = blockY; y < blockY + getDimensions().y(); y++) {
                for (int x = 0; x < getDimensions().x(); x++) {
                    for (int z = 0; z < getDimensions().z() + 1; z++) {
                        Block block = chunk.getBlock(x, y, z);
                        Location location = block.getLocation();
                        Plugin.getDependency().get(SchematicFakeBlockTemporaryStorage.class).addFakeBlock(uuid, BlockVector.of(location), world);
                        BlockChunkVector vector = new BlockChunkVector(x, y - blockY, z);
                        if (blocks.containsKey(vector)) {
                            player.sendBlockChange(location, blocks.get(vector));
                        } else {
                            player.sendBlockChange(location, Material.AIR.createBlockData());
                        }

                    }
                }
            }
        }).runTaskAsynchronously();
    }

    public void displayPreview(Player player) {
        displayPreview(player, player.getLocation().getChunk());
    }
}
