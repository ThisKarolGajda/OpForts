package me.opkarol.opforts.forts;

import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.opforts.forts.models.FortId;
import me.opkarol.oplibrary.autostart.IDisable;
import me.opkarol.oplibrary.autostart.OpAutoDisable;
import me.opkarol.oplibrary.runnable.OpRunnable;
import me.opkarol.oplibrary.wrappers.OpBossBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class FortBorderManager implements IDisable {
    private final Map<FortId, Set<UUID>> playersInsideFort = new HashMap<>();

    public FortBorderManager() {
        OpAutoDisable.add(this);
    }

    public void addPlayer(FortId fortId, UUID playerUUID) {
        if (!playersInsideFort.containsKey(fortId)) {
            playersInsideFort.put(fortId, new HashSet<>());
        }

        if (playersInsideFort.get(fortId).contains(playerUUID)) {
            return;
        }

        playersInsideFort.get(fortId).add(playerUUID);
        new OpRunnable((runnable) -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null || !playersInsideFort.get(fortId).contains(playerUUID)) {
                removePlayer(playerUUID);
                runnable.cancelTask();
                return;
            }

            Fort fort = FortHandler.getFort(fortId);

            double progress = FortHandler.getLocationDistanceFromCenter(player.getLocation(), fort) / 57;
            if (progress > 1) {
                progress = 1;
            }
            progress = Math.abs(progress - 1);

            OpBossBar bossBar = new OpBossBar("Jesteś na terenie fortu " + fort.basicInformation.getName());
            bossBar.build()
                    .setProgress(progress)
                    .setVisible(true)
                    .display(player);
            new OpRunnable(() -> {
                bossBar.setVisible(false);
                bossBar.removeAllPlayers();
            }).runTaskLater(10L);
        }).runTaskTimer(10L);
    }

    public void removePlayer(UUID player){
        for (FortId fortId : playersInsideFort.keySet()) {
            Set<UUID> uuids = playersInsideFort.get(fortId);
            uuids.remove(player);
            playersInsideFort.put(fortId, uuids);
        }
    }

    public void removePlayers() {
        playersInsideFort.forEach((fortId, uuids) -> {
            for (UUID uuid : uuids) {
                removePlayer(uuid);
            }
        });
    }

    @Override
    public void onDisable() {
        removePlayers();
    }
}
