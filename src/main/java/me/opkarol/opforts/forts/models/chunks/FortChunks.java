package me.opkarol.opforts.forts.models.chunks;

import com.google.gson.Gson;
import me.opkarol.oporm.SerializableFieldOrm;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FortChunks implements SerializableFieldOrm, Serializable {
    private List<ChunkLocation> chunkLocations;

    public FortChunks(List<ChunkLocation> chunkLocations) {
        this.chunkLocations = chunkLocations;
    }

    public FortChunks() {
        this.chunkLocations = new ArrayList<>();
    }

    @Override
    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(chunkLocations);
    }

    @Override
    public Object deserialize(String s) {
        Gson gson = new Gson();
        chunkLocations = gson.fromJson(s, List.class);
        return this;
    }

    public void addChunk(@NotNull Chunk chunk) {
        chunkLocations.add(ChunkLocation.from(chunk));
    }

    public List<ChunkLocation> getChunkLocations() {
        return chunkLocations;
    }
}