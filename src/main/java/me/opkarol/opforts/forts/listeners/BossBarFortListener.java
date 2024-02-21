package me.opkarol.opforts.forts.listeners;

import me.opkarol.opforts.forts.FortBorderManager;
import me.opkarol.opforts.forts.FortHandler;
import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.listeners.BasicListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BossBarFortListener extends BasicListener {

    public BossBarFortListener() {
        runListener();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        handlePlayerMovement(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(@NotNull PlayerTeleportEvent event) {
        handlePlayerMovement(event.getPlayer(), event.getFrom(), event.getTo());
    }

    private void handlePlayerMovement(@NotNull Player player, @NotNull Location from, Location to) {
        if (to == null || from.getBlock().equals(to.getBlock()) || to.getChunk().equals(from.getChunk())) {
            return;
        }

        if (FortHandler.isLocationInsideAnyFort(to)) {
            Optional<Fort> optional = FortHandler.getFortAtLocation(to);
            if (optional.isEmpty()) {
                return;
            }

            Plugin.getDependency().get(FortBorderManager.class).addPlayer(optional.get().fortId, player.getUniqueId());
        } else {
            Plugin.getDependency().get(FortBorderManager.class).removePlayer(player.getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLeave(@NotNull PlayerQuitEvent event) {
        Plugin.getDependency().get(FortBorderManager.class).removePlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (FortHandler.isLocationInsideAnyFort(event.getPlayer().getLocation())) {
            Optional<Fort> optional = FortHandler.getFortAtLocation(event.getPlayer().getLocation());
            if (optional.isEmpty()) {
                return;
            }

            Plugin.getDependency().get(FortBorderManager.class).addPlayer(optional.get().fortId, event.getPlayer().getUniqueId());
        }
    }
}
