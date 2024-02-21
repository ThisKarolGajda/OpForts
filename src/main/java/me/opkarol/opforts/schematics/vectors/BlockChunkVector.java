package me.opkarol.opforts.schematics.vectors;

import me.opkarol.oplibrary.location.StringUtil;
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
}
