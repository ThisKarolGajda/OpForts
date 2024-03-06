package me.opkarol.opforts.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DirectionUtils {
    public static @NotNull String getPlayerDirection(@NotNull Player player) {
        float yaw = player.getLocation().getYaw();

        if (yaw < 0) {
            yaw += 360;
        }

        if (yaw >= 315 || yaw < 45) {
            return "SOUTH";
        } else if (yaw >= 45 && yaw < 135) {
            return "WEST";
        } else if (yaw >= 135 && yaw < 225) {
            return "NORTH";
        } else {
            return "EAST";
        }
    }
}
