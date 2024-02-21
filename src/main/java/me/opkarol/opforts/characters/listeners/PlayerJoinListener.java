package me.opkarol.opforts.characters.listeners;

import me.opkarol.opforts.characters.Person;
import me.opkarol.opforts.characters.database.CharactersDatabaseManager;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.listeners.BasicListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener extends BasicListener {
    private final Plugin plugin;

    public PlayerJoinListener(Plugin plugin) {
        this.plugin = plugin;
        runListener();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void createPersonObjectForUserOnJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!plugin.getDependencyManager().get(CharactersDatabaseManager.class).getCharacters().hasUser(uuid)) {
            plugin.getDependencyManager().get(CharactersDatabaseManager.class).getCharacters().save(new Person(uuid));
        }
    }
}
