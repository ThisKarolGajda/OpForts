package me.opkarol.opforts.forts;

import me.opkarol.opforts.forts.database.FortsDatabase;
import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.opforts.forts.models.FortId;
import me.opkarol.opforts.forts.models.chunks.ChunkLocation;
import me.opkarol.opforts.utils.ChunkUtils;
import me.opkarol.oplibrary.Plugin;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FortHandler {

    /**
     * Checks if the chunk is already taken by the fort
     * @param chunk The chunk to check
     * @param fort The fort to check
     * @return True if the chunk is already taken by the fort, false otherwise
     */
    public static boolean isChunkTaken(Chunk chunk, @NotNull Fort fort) {
        return getChunks(fort)
                .stream()
                .anyMatch(fortChunk -> fortChunk.equals(chunk));
    }

    /**
     * Checks if the chunk is the center of the fort
     * @param chunk The chunk to check
     * @param fort The fort to check
     * @return True if the chunk is the center of the fort, false otherwise
     */
    @Contract("null, _ -> false")
    public static boolean isChunkCenter(Chunk chunk, @NotNull Fort fort) {
        return ChunkUtils.getChunkAtWorldLocation(fort.basicInformation.getCenter().getLocation())
                .equals(chunk);
    }

    /**
     * Checks if the chunk is next to the all fort chunks, except for the diagonal chunks
     * @param chunk The chunk to check
     * @param fort The fort to check
     * @return True if the chunk is next to the fort, false otherwise
     */
    public static boolean isChunkNextToFort(@NotNull Chunk chunk, @NotNull Fort fort) {
        List<Chunk> fortChunks = getChunks(fort);
        // If the chunk is already taken by the fort, then it's not next to the fort
        if (fortChunks.stream().anyMatch(chunk::equals)) {
            return false;
        }

        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        for (Chunk fortChunk : fortChunks) {
            int fortChunkX = fortChunk.getX();
            int fortChunkZ = fortChunk.getZ();

            // If the absolute difference between X coordinates is 1 and the absolute difference between Z coordinates is 0
            // Or the absolute difference between Z coordinates is 1 and the absolute difference between X coordinates is 0
            // Then the chunk is next to the fort
            if ((Math.abs(fortChunkX - chunkX) == 1 && Math.abs(fortChunkZ - chunkZ) == 0) ||
                    (Math.abs(fortChunkZ - chunkZ) == 1 && Math.abs(fortChunkX - chunkX) == 0)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Deletes the fort
     * @param fortId The id of the fort to delete
     */
    public static void delete(FortId fortId) {
        Plugin.getDependency().get(FortsDatabase.class).deleteFort(fortId);
    }

    public static void delete(@NotNull Fort fort) {
        delete(fort.fortId);
    }

    /**
     * Gets the chunks of the fort
     * @param fort The fort to get the chunks of
     * @return The chunks of the fort
     */
    public static @NotNull List<Chunk> getChunks(@NotNull Fort fort) {
        double centerX = fort.basicInformation.getCenter().getX();
        double centerZ = fort.basicInformation.getCenter().getZ();
        World world = fort.basicInformation.getCenter().getWorld();

        List<Chunk> chunks = new ArrayList<>();
        chunks.add(ChunkUtils.getChunkAtWorldLocation(world, centerX, centerZ));
        for (ChunkLocation chunkLocation : fort.chunks.getChunkLocations()) {
            int chunkX = chunkLocation.x();
            int chunkZ = chunkLocation.z();
            Chunk chunk = world.getChunkAt(chunkX, chunkZ);
            chunks.add(chunk);
        }

        return chunks;
    }

    /**
     * Gets the distance from the location to the center of the fort
     * @param location The location to get the distance from
     * @param fort The fort to get the distance to
     * @return The distance from the location to the center of the fort
     */
    public static double getLocationDistanceFromCenter(@NotNull Location location, @NotNull Fort fort) {
        location.setY(fort.basicInformation.getCenter().getY());
        return location.distance(fort.basicInformation.getCenter().getLocation());
    }

    /**
     * Gets the fort at the location
     * @param location The location to get the fort at
     * @return The fort at the location
     */
    public static @NotNull Optional<Fort> getFortAtLocation(Location location) {
        return Plugin.getDependency().get(FortsDatabase.class).getAllForts().stream()
                .filter(fort -> isLocationInsideFort(location, fort))
                .findFirst();
    }

    /**
     * Gets the fort by the id
     * @param fortId The id of the fort to get
     * @return The fort by the id
     */
    public static Optional<Fort> getFort(String fortId) {
        return Optional.ofNullable(Plugin.getDependency().get(FortsDatabase.class).getFort(new FortId(UUID.fromString(fortId))));
    }

    public static Fort getFort(FortId fortId) {
        return Plugin.getDependency().get(FortsDatabase.class).getFort(fortId);
    }

    /**
     * Gets the forts of the player
     * @param player The player to get the forts of
     * @return The forts of the player
     */
    public static List<Fort> getPlayerForts(UUID player) {
        return Plugin.getDependency().get(FortsDatabase.class).getFortsOfPlayer(player);
    }

    /**
     * Checks if the player is part of any fort
     * @param player The player to check
     * @return True if the player is part of any fort, false otherwise
     */
    public static boolean isPlayerPartOfAnyFort(UUID player) {
        return !getPlayerForts(player).isEmpty();
    }

    /**
     * Checks if the location is inside any fort
     * @param location The location to check
     * @return True if the location is inside any fort, false otherwise
     */
    public static boolean isLocationInsideAnyFort(Location location) {
        return Plugin.getDependency().get(FortsDatabase.class).getAllForts().stream()
                .anyMatch(fort -> isLocationInsideFort(location, fort));
    }

    /**
     * Checks if the location is inside the fort
     * @param location The location to check
     * @param fort The fort to check
     * @return True if the location is inside the fort, false otherwise
     */
    public static boolean isLocationInsideFort(Location location, @NotNull Fort fort) {
        Chunk chunkInCenter = ChunkUtils.getChunkAtWorldLocation(location);
        List<Chunk> chunks = getChunks(fort);

        return chunks.stream()
                .anyMatch(chunk -> chunk.equals(chunkInCenter));
    }

    /**
     * Checks if the player is the owner of any fort
     * @param player The player to check
     * @return True if the player is the owner of any fort, false otherwise
     */
    public static boolean isOwnerOfAnyFort(UUID player) {
        return Plugin.getDependency().get(FortsDatabase.class).getAllForts().stream()
                .anyMatch(fort -> fort.members.getOwner().equals(player));
    }

    /**
     * Gets the fort of the owner
     * @param player The owner to get the fort of
     * @return The fort of the owner
     */
    public static @NotNull Optional<Fort> getOwnerFort(UUID player) {
        return Plugin.getDependency().get(FortsDatabase.class).getAllForts().stream()
                .filter(fort -> fort.members.getOwner().equals(player))
                .findFirst();
    }
}
