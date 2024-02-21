package me.opkarol.opforts.forts.npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NPCPlayerSpawner {

    public static void spawn(@NotNull Player player, Location location) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getName());
        npc.setProtected(true);
        npc.spawn(location);
        Equipment equipment = npc.getOrAddTrait(Equipment.class);
        equipment.set(Equipment.EquipmentSlot.BOOTS, player.getInventory().getBoots());
        equipment.set(Equipment.EquipmentSlot.LEGGINGS, player.getInventory().getLeggings());
        equipment.set(Equipment.EquipmentSlot.CHESTPLATE, player.getInventory().getChestplate());
        equipment.set(Equipment.EquipmentSlot.HELMET, player.getInventory().getHelmet());
        equipment.set(Equipment.EquipmentSlot.HAND, player.getInventory().getItemInMainHand());
        equipment.set(Equipment.EquipmentSlot.OFF_HAND, player.getInventory().getItemInOffHand());
        npc.addTrait(new NPCPlayerTrait());
    }
}
