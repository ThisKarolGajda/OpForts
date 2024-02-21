package me.opkarol.opforts.schematics;

import me.opkarol.opforts.schematics.vectors.BlockChunkVector;
import me.opkarol.opforts.schematics.vectors.BlockVector;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

class SchematicHandler {

    public static @NotNull Map<BlockChunkVector, BlockData> loadChunk(Chunk chunk, int yStartLocation, int yEndLocation) {
        assert yStartLocation < yEndLocation;
        Map<BlockChunkVector, BlockData> set = new HashMap<>();
        for (int y = yStartLocation; y < yEndLocation; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    // Don't include air, assume so if there is no given block it is air
                    if (block.isEmpty()) {
                        continue;
                    }

                    set.put(new BlockChunkVector(x, y - yStartLocation, z), block.getBlockData());
                }
            }
        }

        return set;
    }

    public static void pasteSchematic(Chunk chunk, Map<BlockChunkVector, BlockData> map, int yStartLocation) {
        for (int y = yStartLocation; y < yStartLocation + calculateHeight(map.keySet()); y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    BlockChunkVector vector = new BlockChunkVector(x, y - yStartLocation, z);
                    if (map.containsKey(vector)) {
                        block.setType(map.get(vector).getMaterial());
                        block.setBlockData(map.get(vector));
                    } else {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public static @NotNull String serialize(@NotNull Map<BlockChunkVector, BlockData> blocks) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, List<BlockChunkVector>> serializedBlocks = new HashMap<>();

        for (Map.Entry<BlockChunkVector, BlockData> entry : blocks.entrySet()) {
            BlockChunkVector vector = entry.getKey();
            BlockData blockData = entry.getValue();
            String serializedBlock = blockData.getAsString();
            List<BlockChunkVector> list = serializedBlocks.computeIfAbsent(serializedBlock, k -> new ArrayList<>());
            list.add(vector);
            serializedBlocks.put(serializedBlock, list);
        }

        for (Map.Entry<String, List<BlockChunkVector>> serializedEntry : serializedBlocks.entrySet()) {
            String serializedBlock = serializedEntry.getKey();
            List<BlockChunkVector> locations = serializedEntry.getValue();
            if (locations.size() > 0) {
                stringBuilder.append(serializedBlock);
                stringBuilder.append(";");
                for (BlockChunkVector location : locations) {
                    stringBuilder.append(location.serialize()).append("@");
                }

                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.append("?");
            }

        }

        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString()
                .replace("minecraft:", ":")
                .replace("grass_block[snowy=false]", "+B0")
                .replace("waterlogged", "+G")
                .replace("facing", "+F")
                .replace("shape", "+S")
                .replace("half", "+H")
                .replace("rotation", "+R")
                .replace("east", "+E")
                .replace("bottom", "+B1")
                .replace("straight", "+B2")
                .replace("stairs", "#S");
    }

    public static @NotNull Map<BlockChunkVector, BlockData> deserialize(@NotNull String serializedString) {
        serializedString = serializedString
                .replace("#S", "stairs")
                .replace("+B2", "straight")
                .replace("+B1", "bottom")
                .replace("+E", "east")
                .replace("+R", "rotation")
                .replace("+H", "half")
                .replace("+S", "shape")
                .replace("+F", "facing")
                .replace("+G", "waterlogged")
                .replace("+B0", "grass_block[snowy=false]")
                .replace(":", "minecraft:")
        ;
        Map<BlockChunkVector, BlockData> blocks = new HashMap<>();
        String[] entries = serializedString.split("\\?");
        for (String entry : entries) {
            String[] parts = entry.split(";");
            if (parts.length == 2) {
                String[] blockAndLocations = parts[1].split("@");
                BlockData blockData = Bukkit.createBlockData(parts[0]);
                for (String locationStr : blockAndLocations) {
                    BlockChunkVector vector = BlockChunkVector.deserialize(locationStr);
                    if (vector != null) {
                        blocks.put(vector, blockData);
                    }
                }
            }
        }
        return blocks;
    }

    public static int calculateHeight(@NotNull Set<BlockChunkVector> vectors) {
        int maxY = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;

        for (BlockChunkVector vector : vectors) {
            int y = vector.y();
            maxY = Math.max(maxY, y);
            minY = Math.min(minY, y);
        }

        return maxY - minY + 1;
    }

    public static int calculateWidth(@NotNull Set<BlockChunkVector> vectors) {
        int maxX = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;

        for (BlockChunkVector vector : vectors) {
            int x = vector.xOffset();
            maxX = Math.max(maxX, x);
            minX = Math.min(minX, x);
        }

        return maxX - minX + 1;
    }

    public static int calculateLength(@NotNull Set<BlockChunkVector> vectors) {
        int maxZ = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (BlockChunkVector vector : vectors) {
            int z = vector.zOffset();
            maxZ = Math.max(maxZ, z);
            minZ = Math.min(minZ, z);
        }

        return maxZ - minZ + 1;
    }

    @Contract("_ -> new")
    public static @NotNull BlockVector getDimensions(@NotNull Set<BlockChunkVector> vectors) {
        return new BlockVector(calculateWidth(vectors), calculateHeight(vectors), calculateLength(vectors));
    }
}
