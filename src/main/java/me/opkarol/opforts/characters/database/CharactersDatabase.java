package me.opkarol.opforts.characters.database;

import me.opkarol.opforts.characters.Person;
import me.opkarol.opforts.characters.listeners.KnightListener;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.configurationfile.ConfigurationFile;
import me.opkarol.oplibrary.database.manager.DatabaseImpl;
import me.opkarol.oplibrary.database.manager.settings.DatabaseFactory;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class CharactersDatabase {
    private final DatabaseImpl<String, Person> database;

    public CharactersDatabase(@NotNull Plugin plugin) {
        ConfigurationFile config = plugin.getConfigurationFile();
        if (config.getFileConfiguration().getBoolean("isMysqlEnabled")) {
            database = DatabaseFactory.createSql("jdbc:mysql://" + config.getFileConfiguration().getString("connectionSettings.host") + ":" + config.getFileConfiguration().getString("connectionSettings.port") + "/" + config.getFileConfiguration().getString("connectionSettings.database") + "?autoReconnect=true", config.getFileConfiguration().getString("connectionSettings.username"), config.getFileConfiguration().getString("connectionSettings.password"), Person.class);
        } else {
            database = DatabaseFactory.createFlat(plugin, "characters.db");
        }

        // Register listeners
        new KnightListener();
    }

    public DatabaseImpl<String, Person> getDatabase() {
        return database;
    }

    public boolean hasUser(UUID uuid) {
        return getDatabase().getAll().stream()
                .anyMatch(person -> person.getUUID().equals(uuid));
    }

    public void save(Person person) {
        getDatabase().save(person);
    }

    public Optional<Person> getPerson(UUID uuid) {
        return getDatabase().getAll().stream()
                .filter(person -> person.getUUID().equals(uuid))
                .findFirst();
    }

    public Optional<Person> getPerson(@NotNull OfflinePlayer offlinePlayer) {
        return getPerson(offlinePlayer.getUniqueId());
    }
}
