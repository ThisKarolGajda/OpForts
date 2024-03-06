package me.opkarol.opforts;

import me.opkarol.opforts.characters.commands.CharacterCommand;
import me.opkarol.opforts.characters.database.CharactersDatabaseManager;
import me.opkarol.opforts.core.EnderPearlBlockerListener;
import me.opkarol.opforts.forts.FortBorderManager;
import me.opkarol.opforts.forts.building.BuildingListener;
import me.opkarol.opforts.forts.building.BuildingPreviewManager;
import me.opkarol.opforts.forts.commands.FortCommand;
import me.opkarol.opforts.forts.database.FortsDatabase;
import me.opkarol.opforts.forts.listeners.BossBarFortListener;
import me.opkarol.opforts.forts.listeners.BuildingHeartInteractListener;
import me.opkarol.opforts.forts.npc.NPCPlayerTrait;
import me.opkarol.opforts.safeinventory.SafeInventoryManager;
import me.opkarol.opforts.schematics.SchematicCommand;
import me.opkarol.opforts.schematics.SchematicFakeBlockTemporaryStorage;
import me.opkarol.oplibrary.Plugin;
import org.jetbrains.annotations.Nullable;

public class OpForts extends Plugin {

    @Override
    public @Nullable Integer registerBStatsOnStartup() {
        return 20861;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        // Core
        new EnderPearlBlockerListener();

        // Characters
        getDependency().register(CharactersDatabaseManager.class, new CharactersDatabaseManager(this));
        getCommandRegister().registerClass(CharacterCommand.class);

        // Forts
        getDependency().register(FortsDatabase.class, new FortsDatabase(this));
        getCommandRegister().registerClass(FortCommand.class);
        getDependency().register(FortBorderManager.class, new FortBorderManager());
        new BossBarFortListener();
        new BuildingHeartInteractListener();
        new BuildingListener();

        // NPC
        net.citizensnpcs.api.CitizensAPI.getTraitFactory()
                .registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(NPCPlayerTrait.class));

        // Schematics
        getDependency().register(SchematicFakeBlockTemporaryStorage.class, new SchematicFakeBlockTemporaryStorage());
        getCommandRegister().registerClass(SchematicCommand.class);

        // Safe Inventory
        getDependency().register(SafeInventoryManager.class, new SafeInventoryManager());
        new BuildingPreviewManager();
    }
}
