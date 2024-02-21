package me.opkarol.opforts.schematics.vectors;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record BlockVector(int x, int y, int z) {
    @Contract("_ -> new")
    public static @NotNull BlockVector of(@NotNull Location location) {
        return new BlockVector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
