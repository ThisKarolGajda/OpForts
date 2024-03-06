package me.opkarol.opforts.schematics.vectors;

import me.opkarol.opforts.utils.ChunkUtils;
import me.opkarol.oplibrary.location.StringUtil;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public record BlockChunkVector(int xOffset, int y, int zOffset) implements Serializable {

    @Contract(pure = true)
    public @NotNull String serialize() {
        if (xOffset == y && y == zOffset) {
            return String.valueOf(xOffset);
        }
        if (xOffset == y) {
            return xOffset + "|" + zOffset;
        }
        if (y == 0) {
            return xOffset + "||" + zOffset;
        }
        return xOffset + "|" + y + "|" + zOffset;
    }

    public static @Nullable BlockChunkVector deserialize(@NotNull String serializedString) {
        String[] parts = serializedString.split("\\|");
        if (parts.length == 3 && parts[1].isEmpty()) {
            int xOffset = StringUtil.getIntFromString(parts[0]);
            int y = 0;
            int zOffset = StringUtil.getIntFromString(parts[2]);
            return new BlockChunkVector(xOffset, y, zOffset);
        }

        if (parts.length == 3) {
            int xOffset = StringUtil.getIntFromString(parts[0]);
            int y = StringUtil.getIntFromString(parts[1]);
            int zOffset = StringUtil.getIntFromString(parts[2]);
            return new BlockChunkVector(xOffset, y, zOffset);
        }

        if (parts.length == 2) {
            int xOffset = StringUtil.getIntFromString(parts[0]);
            int y = StringUtil.getIntFromString(parts[0]);
            int zOffset = StringUtil.getIntFromString(parts[1]);
            return new BlockChunkVector(xOffset, y, zOffset);
        }

        if (parts.length == 1) {
            int number = StringUtil.getIntFromString(parts[0]);
            return new BlockChunkVector(number, number, number);
        }

        return null;
    }

    @Contract("_, _, _ -> new")
    public @NotNull BlockChunkVector rotate2D(int angle, int centerX, int centerZ) {
        double radians = Math.toRadians(angle);
        int x = (int) Math.round((xOffset - centerX) * Math.cos(radians) - (zOffset - centerZ) * Math.sin(radians)) + centerX;
        int z = (int) Math.round((xOffset - centerX) * Math.sin(radians) + (zOffset - centerZ) * Math.cos(radians)) + centerZ;
        return new BlockChunkVector(x, y, z);
    }

    public @NotNull Block toBlock(@NotNull Chunk chunk) {
        return chunk.getBlock(xOffset, y + ChunkUtils.calculateOptimalChunkYHeight(chunk), zOffset);
    }
}
