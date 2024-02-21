package me.opkarol.opforts.forts.models.buildings;

import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.tools.FormatTool;
import me.opkarol.oplibrary.tools.Heads;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BuildingHeartManager {

    public static void createBuildingHeart(@NotNull Location location) {
        if (location.getWorld() == null) {
            return;
        }

        location.setYaw(0);
        location.setPitch(0);
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setMarker(false);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.setCustomName(FormatTool.formatMessage("&7Naciśnij, aby zarządzać"));
        armorStand.setCustomNameVisible(true);
        armorStand.getEquipment().setHelmet(Heads.get("3eb3d71cd5ac91d8bd1d61fceb5557b9e19c0b9a54dc2720dffd5fbcc1227fc0"));
        NamespacedKey key = new NamespacedKey(Plugin.getInstance(), "isBuildingHeart");
        armorStand.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");
    }
}
