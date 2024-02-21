package me.opkarol.opforts.forts.database;

import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.opforts.forts.models.FortId;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.configurationfile.ConfigurationFile;
import me.opkarol.oplibrary.database.manager.DatabaseImpl;
import me.opkarol.oplibrary.database.manager.settings.DatabaseFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class FortsDatabase {
    private final DatabaseImpl<FortId, Fort> database;

    public FortsDatabase(@NotNull Plugin plugin) {
        ConfigurationFile config = plugin.getConfigurationFile();
        if (config.getFileConfiguration().getBoolean("isMysqlEnabled")) {
            database = DatabaseFactory.createSql("jdbc:mysql://" + config.getFileConfiguration().getString("connectionSettings.host") + ":" + config.getFileConfiguration().getString("connectionSettings.port") + "/" + config.getFileConfiguration().getString("connectionSettings.database") + "?autoReconnect=true", config.getFileConfiguration().getString("connectionSettings.username"), config.getFileConfiguration().getString("connectionSettings.password"), Fort.class);
        } else {
            database = DatabaseFactory.createFlat(plugin, "forts.db");
        }
    }

    public DatabaseImpl<FortId, Fort> getDatabase() {
        return database;
    }

    public Fort getFort(FortId fortId) {
        return database.getById(fortId);
    }

    public void saveFort(Fort fort) {
        database.save(fort);
    }

    public List<Fort> getAllForts() {
        return database.getAll();
    }

    public void deleteFort(FortId fortId) {
        database.delete(fortId);
    }

    public List<Fort> getFortsOfPlayer(UUID player) {
        return database.getAll()
                .stream()
                .filter(fort -> fort.members.isMember(player))
                .toList();
    }

    public FortId getNotTakenFortId() {
        while (true){
            FortId fortId = new FortId(UUID.randomUUID());
            if (getFort(fortId) == null) {
                return fortId;
            }
        }
    }
}
