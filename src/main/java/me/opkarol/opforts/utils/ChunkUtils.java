package me.opkarol.opforts.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ChunkUtils {
    @Contract("_ -> new")
    public static @NotNull Location getChunkCenter(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location's world is null");
        }

        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;
        int centerX = (chunkX << 4) + 8;
        int centerZ = (chunkZ << 4) + 8;
        int centerY = world.getHighestBlockYAt(centerX, centerZ);
        return new Location(world, centerX, centerY, centerZ);
    }

    @Contract("_ -> new")
    public static @NotNull Location getChunkCenter(Chunk chunk) {
        World world = chunk.getWorld();

        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        int centerX = (chunkX << 4) + 8;
        int centerZ = (chunkZ << 4) + 8;
        int centerY = world.getHighestBlockYAt(centerX, centerZ);
        return new Location(world, centerX, centerY, centerZ);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Location getChunkCenter(World world, int x, int z) {
        return getChunkCenter(getChunkAtChunkCoords(world, x, z));
    }

    public static @NotNull Chunk getChunkAtWorldLocation(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location's world is null");
        }

        return world.getChunkAt(location);
    }

    public static @NotNull Chunk getChunkAtWorldLocation(World world, double x, double z) {
        return getChunkAtWorldLocation(new Location(world, x, 0, z));
    }

    public static @NotNull Chunk getChunkAtChunkCoords(@NotNull World world, int x, int z) {
        return world.getChunkAt(x, z);
    }

    // This method should return the smallest x and z coordinates of the chunk, so
    // pasting a schematic at the returned location will not cause any issues.
    @Contract("_ -> new")
    public static @NotNull Location getSmallestXYLocationAtChunk(@NotNull Chunk chunk) {
        World world = chunk.getWorld();
        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x, y, z);
    }

    public static @NotNull String toString(@NotNull Chunk chunk) {
        return "[" + chunk.getX() + ", " + chunk.getZ() + "]";
    }
}
