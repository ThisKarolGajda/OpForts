package me.opkarol.opforts.forts.npc;

import me.opkarol.oplibrary.runnable.OpRunnable;
import me.opkarol.oplibrary.runnable.OpTimerRunnable;
import me.opkarol.oplibrary.tools.FormatTool;
import me.opkarol.oplibrary.wrappers.OpParticle;
import me.opkarol.oplibrary.wrappers.OpSound;
import me.opkarol.oplibrary.wrappers.OpTitle;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

@TraitName("npcplayertrait")
public class NPCPlayerTrait extends Trait {
    public NPCPlayerTrait() {
        super("npcplayertrait");
    }

    @EventHandler
    public void rightClickEvent(net.citizensnpcs.api.event.NPCRightClickEvent event) {
        interactWithNPC(event.getNPC(), event.getClicker());
    }

    @EventHandler
    public void leftClickEvent(net.citizensnpcs.api.event.NPCLeftClickEvent event) {
        interactWithNPC(event.getNPC(), event.getClicker());
    }

    private void interactWithNPC(NPC npc, Player sender) {
        if (npc != this.getNPC()) {
            return;
        }

        Player player = Bukkit.getPlayer(npc.getName());
        if (player == null) {
            return;
        }

        sender.sendMessage(FormatTool.formatMessage("&7Wczytywanie gracza &e" + player.getName() + "&7..."));
        Location location = this.getNPC().getStoredLocation().clone();
        getNPC().destroy();

        new OpTitle("&7Zostajesz zaatakowany", "&7Przygotuj się", 5, 50, 5)
                .display(player);
        new OpTimerRunnable(() -> {
            new OpSound(Sound.BLOCK_ANVIL_PLACE).play(player);
            new OpParticle(Particle.EXPLOSION_HUGE).setLocation(location).display(sender);
        }, 3);
        new OpRunnable(() -> player.teleport(location)).runTaskLater(60L);
    }
}
