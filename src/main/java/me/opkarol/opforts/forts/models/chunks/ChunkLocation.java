package me.opkarol.opforts.forts.models.chunks;

import me.opkarol.opforts.utils.ChunkUtils;
import me.opkarol.oplibrary.location.OpSerializableLocation;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record ChunkLocation(int x, int z) implements Serializable {

    @Contract("_ -> new")
    public static @NotNull ChunkLocation from(@NotNull Chunk chunk) {
        return new ChunkLocation(chunk.getX(), chunk.getZ());
    }

    @Contract("_ -> new")
    public static @NotNull ChunkLocation from(@NotNull Location location) {
        return from(location.getChunk());
    }

    @Contract("_ -> new")
    public static @NotNull ChunkLocation from(@NotNull OpSerializableLocation location) {
        return from(location.getLocation());
    }

    public @NotNull Location getCenter(World world) {
        return ChunkUtils.getChunkCenter(world, x, z);
    }

    @Contract("_ -> new")
    public @NotNull Location getSmallestLocation(World world) {
        return ChunkUtils.getSmallestXYLocationAtChunk(ChunkUtils.getChunkAtChunkCoords(world, x, z));
    }

    public @NotNull Chunk getChunk(World world) {
        return ChunkUtils.getChunkAtChunkCoords(world, x, z);
    }
}
