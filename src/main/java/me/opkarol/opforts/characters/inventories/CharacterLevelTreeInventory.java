package me.opkarol.opforts.characters.inventories;

import me.opkarol.opforts.characters.CharacterType;
import me.opkarol.opforts.characters.Person;
import me.opkarol.opforts.characters.database.CharactersDatabaseManager;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.inventories.ChestInventory;
import me.opkarol.oplibrary.inventories.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class CharacterLevelTreeInventory extends ChestInventory {

    public CharacterLevelTreeInventory(Player player, @NotNull CharacterType type) {
        super("character_level_tree", 2);

        setItemPreviousPage(16, player);
        setItemNextPage(17, player);
        setGlobalRowEmptyIfNotTaken(2);

        Optional<Person> optional = Plugin.getDependency().get(CharactersDatabaseManager.class).getCharacters().getPerson(player);
        if (optional.isEmpty()) {
            return;
        }

        Person person = optional.get();

        type.getCharacter().getLevels().forEach((integer, characterLevel) -> {
            ItemBuilder builder = new ItemBuilder(Material.STONE);
            if (person.getCharactersExperience().getMap().get(type).getLevel() > integer) {
                builder.setFlags(ItemFlag.HIDE_ENCHANTS);
                builder.setEnchants(Map.of(Enchantment.LUCK, 1));
            }
            setNextFree("upgrade", builder, event -> event.setCancelled(true), Map.of(
                    "%points%", String.valueOf(characterLevel.experience()),
                    "%level%", String.valueOf(integer),
                    "%reward_type%", characterLevel.reward().type().getName(),
                    "%reward%", characterLevel.reward().type().getFormattedInformation(characterLevel.reward().object())
            ));
        });

        open(player);
    }
}
    