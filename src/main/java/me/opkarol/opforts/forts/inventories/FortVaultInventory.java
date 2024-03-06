package me.opkarol.opforts.forts.inventories;

import me.opkarol.opforts.forts.models.Fort;
import me.opkarol.opforts.forts.models.vault.FortVault;
import me.opkarol.oplibrary.inventories.ChestInventory;
import me.opkarol.oplibrary.inventories.ItemBuilder;
import me.opkarol.oplibrary.location.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class FortVaultInventory extends ChestInventory {

    public FortVaultInventory(Player player, @NotNull Fort fort) {
        this(player, fort, 0);
    }

    public FortVaultInventory(Player player, @NotNull Fort fort, int page) {
        super("fort_vault", 6);

        setItemPreviousPage(52, player);
        setItemNextPage(53, player);
        setGlobalRowEmptyIfNotTaken(6);

        for (Map.Entry<String, Integer> entry : fort.vault.getItemsInVaults().entrySet()) {
            Material material = StringUtil.getMaterialFromString(entry.getKey());
            int amount = entry.getValue();
            setNextFree("block", new ItemBuilder(material, amount), event -> event.setCancelled(true), Map.of(
                    "%type%", material.name(),
                    "%amount%", String.valueOf(amount)
            ));
        }

        FortVault vault = fort.vault;
        setClickEventConsumer(event -> {
            if (Objects.equals(event.getClickedInventory(), player.getInventory())) {
                if (!event.isShiftClick()) {
                    return;
                }

                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                ItemStack item = event.getCurrentItem();
                if (item == null || item.getType().isAir()) {
                    return;
                }

                if (!vault.isValidItem(item)) {
                    player.sendMessage("You can't put this item in vault");
                    return;

                }

                if (!vault.isEnoughSpaceInVault(item)) {
                    player.sendMessage("There is not enough space in vault");
                    return;
                }

                player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                vault.addItem(item);
            } else if (event.getSlot() < 45) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem == null || clickedItem.getType().isAir()) {
                    return;
                }

                ItemStack item = vault.retrieveItem(clickedItem.getType());
                player.getInventory().addItem(item);
            }
            new FortVaultInventory(player, fort, getCurrentPage());
            fort.save();
        });

        open(player, page);
    }
}
    