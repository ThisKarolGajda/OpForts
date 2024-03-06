package me.opkarol.opforts.forts.building;

import com.jeff_media.customblockdata.CustomBlockData;
import me.opkarol.opforts.NamespacedKeyType;
import me.opkarol.opforts.schematics.Schematic;
import me.opkarol.opforts.schematics.vectors.BlockChunkVector;
import me.opkarol.opforts.utils.ChunkUtils;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.misc.Tuple;
import me.opkarol.oplibrary.runnable.OpRunnable;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BuildingSchematic extends Schematic {
    public BuildingSchematic(String name, Map<BlockChunkVector, BlockData> blocks) {
        super(name, blocks);
    }

    public BuildingSchematic(String name) {
        super(name);
    }

    public int getBlockCountWithAir() {
        return getDimensions().x() * getDimensions().y()  * getDimensions().z();
    }

    public int getBlockCountWithoutAir() {
        return getBlocks().size();
    }

    public Tuple<BlockChunkVector, BlockData> getFromIndex(int block) {
        assert block >= getBlockCountWithoutAir();
        int i = 0;
        for (Map.Entry<BlockChunkVector, BlockData> entry : getBlocks().entrySet()) {
            if (i == block) {
                return new Tuple<>(entry.getKey(), entry.getValue());
            }
            i++;
        }

        return null;
    }

    public void buildFromIndex(Chunk chunk, int startIndex, double speed, BiConsumer<Integer, Block> onProgressCallback, Runnable onEndCallback) {
        Map<BlockChunkVector, BlockData> sortedBlocks = sortBlocksByY(getBlocks());
        Iterator<Map.Entry<BlockChunkVector, BlockData>> iterator = sortedBlocks.entrySet().iterator();
        AtomicInteger progress = new AtomicInteger(startIndex);
        for (int i = 0; i < startIndex; i++) {
            if (iterator.hasNext()) {
                iterator.next();
            } else {
                if (onEndCallback != null) {
                    onEndCallback.run();
                }
                return;
            }
        }
        buildNextBlock(iterator, chunk, ChunkUtils.calculateOptimalChunkYHeight(chunk), speed, progress, onProgressCallback, onEndCallback);
    }

    public void build(Chunk chunk, double speed, BiConsumer<Integer, Block> onProgressCallback, Runnable onEndCallback) {
        AtomicInteger progress = new AtomicInteger(0);
        Map<BlockChunkVector, BlockData> sortedBlocks = sortBlocksByY(getBlocks());
        Iterator<Map.Entry<BlockChunkVector, BlockData>> iterator = sortedBlocks.entrySet().iterator();
        buildNextBlock(iterator, chunk, ChunkUtils.calculateOptimalChunkYHeight(chunk), speed, progress, onProgressCallback, onEndCallback);
    }

    private Map<BlockChunkVector, BlockData> sortBlocksByY(@NotNull Map<BlockChunkVector, BlockData> blocks) {
        return blocks.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparingInt(BlockChunkVector::y)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private void buildNextBlock(@NotNull Iterator<Map.Entry<BlockChunkVector, BlockData>> iterator, Chunk chunk, int yStartLocation, double speed, AtomicInteger progress, BiConsumer<Integer, Block> onProgressCallback, Runnable onEndCallback) {
        if (iterator.hasNext()) {
            new OpRunnable(() -> {
                Map.Entry<BlockChunkVector, BlockData> entry = iterator.next();
                BlockChunkVector blockChunkVector = entry.getKey();
                BlockData blockData = entry.getValue();


                Block block = chunk.getBlock(blockChunkVector.xOffset(), blockChunkVector.y() + yStartLocation, blockChunkVector.zOffset());
                block.setBlockData(blockData, false);
                PersistentDataContainer customBlockData = new CustomBlockData(block, Plugin.getInstance());
                customBlockData.set(NamespacedKeyType.FORTS.get(), PersistentDataType.STRING, "building_schematic");

                int currentProgress = progress.incrementAndGet();
                if (onProgressCallback != null) {
                    onProgressCallback.accept(currentProgress, block);
                }

                chunk.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, block.getLocation(), 1);
                buildNextBlock(iterator, chunk, yStartLocation, speed, progress, onProgressCallback, onEndCallback);
            }).runTaskLater((long) (speed * 20));

        } else {
            if (onEndCallback != null) {
                onEndCallback.run();
            }
        }
    }

    public List<BlockChunkVector> getVectorsMatchingBlockType(Material type) {
        return getBlocks().entrySet().stream()
                .filter(entry -> entry.getValue().getMaterial() == type)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public BlockChunkVector getSpecialBlock() {
        return getVectorsMatchingBlockType(Material.BEDROCK)
                .get(0);
    }

    public Location getSpecialBlockLocation(Chunk chunk) {
        return getSpecialBlock().toBlock(chunk).getLocation();
    }

    public Location getStartingBlockLocation(Chunk chunk) {
        return ChunkUtils.getSmallestXYLocationAtChunk(chunk);
    }

    public List<Location> getNeededEmptyBlocks(Chunk chunk) {
        Location location = getStartingBlockLocation(chunk);
        List<Location> list = new ArrayList<>();
        for (int x = 0; x < getDimensions().x(); x++) {
            for (int y = 0; y < getDimensions().y(); y++) {
                for (int z = 0; z < getDimensions().z() + 1; z++) {
                    list.add(new Location(chunk.getWorld(), location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z));
                }
            }
        }
        return list;
    }

    public List<Block> getAllBlocksThatNeedToBeEmpty(Chunk chunk) {
        List<Location> locations = getNeededEmptyBlocks(chunk);
        System.out.println(locations.size());
        List<Block> blocks = new ArrayList<>();
        for (Location location : locations) {
            blocks.add(location.getBlock());
        }

        System.out.println(blocks.size());
        blocks.removeIf(Block::isEmpty);
        System.out.println(blocks.size());

        return blocks;
    }

    public List<Map.Entry<Material, Integer>> getRequiredBlocksSortedByAmount() {
        Map<Material, Integer> map = new HashMap<>();
        for (BlockData blockData : getBlocks().values()) {
            Material material = blockData.getMaterial();
            if (material == Material.BEDROCK || !material.isItem()) {
                continue;
            }

            map.put(material, map.getOrDefault(material, 0) + 1);
        }

        List<Map.Entry<Material, Integer>> sortedList = new ArrayList<>(map.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());
        Collections.reverse(sortedList);

        return sortedList;
    }
}
