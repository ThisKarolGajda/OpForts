package me.opkarol.opforts.forts;

import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.oplibrary.runnable.OpTimerRunnable;
import org.bukkit.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FortHighlighter {
    public static @NotNull List<Location> getFortBorderLocations(@NotNull Fort fort) {
        List<Location> locations = new ArrayList<>();
        Set<Chunk> fortChunks = new HashSet<>(FortHandler.getChunks(fort)); // Convert to set for fast lookup

        for (Chunk chunk : fortChunks) {
            World world = chunk.getWorld();
            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();
            int minX = chunkX << 4; // Minimum X coordinate of the chunk
            int minZ = chunkZ << 4; // Minimum Z coordinate of the chunk
            int maxX = minX + 15; // Maximum X coordinate of the chunk
            int maxZ = minZ + 15; // Maximum Z coordinate of the chunk

            // Check neighboring chunks and exclude border locations shared with fort chunks
            if (!fortChunks.contains(world.getChunkAt(chunkX - 1, chunkZ))) {
                for (int z = minZ; z <= maxZ; z++) {
                    locations.add(new Location(world, minX, 0, z));
                }
            }
            if (!fortChunks.contains(world.getChunkAt(chunkX + 1, chunkZ))) {
                for (int z = minZ; z <= maxZ; z++) {
                    locations.add(new Location(world, maxX, 0, z));
                }
            }
            if (!fortChunks.contains(world.getChunkAt(chunkX, chunkZ - 1))) {
                for (int x = minX; x <= maxX; x++) {
                    locations.add(new Location(world, x, 0, minZ));
                }
            }
            if (!fortChunks.contains(world.getChunkAt(chunkX, chunkZ + 1))) {
                for (int x = minX; x <= maxX; x++) {
                    locations.add(new Location(world, x, 0, maxZ));
                }
            }
        }

        return locations;
    }

    private static void spawnParticle(World world, double x, double y, double z) {
        Location location = new Location(world, x + 0.5, y, z + 0.5);
        Particle particle = Particle.BLOCK_MARKER;
        world.spawnParticle(particle, location, 1, 0, 0, 0, 1, Material.RED_WOOL.createBlockData());
    }

    public static void highlightFort(@NotNull Fort fort, double y) {
        final List<Location> locations = getFortBorderLocations(fort);
        for (int i = 0; i < 5; i++) {
            final double height = y + (i - 2) * 5;
            new OpTimerRunnable().runTaskTimesUp((__, ___) -> {
                for (Location location : locations) {
                    spawnParticle(location.getWorld(), location.getX(), height, location.getZ());
                }
            }, (____) -> {}, 30);
        }

    }
}
