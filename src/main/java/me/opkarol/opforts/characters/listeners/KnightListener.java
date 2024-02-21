package me.opkarol.opforts.characters.listeners;

import me.opkarol.opforts.characters.CharacterType;
import me.opkarol.opforts.characters.Person;
import me.opkarol.opforts.characters.database.CharactersDatabaseManager;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.listeners.BasicListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

public class KnightListener extends BasicListener {

    public KnightListener() {
        runListener();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Person person = Plugin.getDependency().get(CharactersDatabaseManager.class).getCharacters().getPerson(event.getEntity().getKiller()).orElseThrow();
            person.addExperienceWithNotify(event.getEntity().getKiller(), CharacterType.KNIGHT, 1);
        }
    }
}
