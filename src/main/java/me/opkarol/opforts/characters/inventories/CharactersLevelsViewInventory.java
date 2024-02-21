package me.opkarol.opforts.characters.inventories;

import me.opkarol.opforts.characters.CharacterExperience;
import me.opkarol.opforts.characters.CharacterType;
import me.opkarol.opforts.characters.Person;
import me.opkarol.opforts.characters.database.CharactersDatabaseManager;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.inventories.ChestInventory;
import me.opkarol.oplibrary.tools.Heads;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CharactersLevelsViewInventory extends ChestInventory {

    public CharactersLevelsViewInventory(Player player, UUID target) {
        super("characters_levels_view", 3);

        Optional<Person> optional = Plugin.getDependency().get(CharactersDatabaseManager.class).getCharacters().getPerson(target);
        Person person;
        if (optional.isPresent()) {
            person = optional.get();
        } else {
            person = new Person(target);
            Plugin.getDependency().get(CharactersDatabaseManager.class).getCharacters().save(person);
        }

        Map<CharacterType, CharacterExperience> map = person.getCharactersExperience().getMap();
        setItem(player, 4, CharacterType.ALCHEMIST, map.get(CharacterType.ALCHEMIST));
        setItem(player, 10, CharacterType.BUILDER, map.get(CharacterType.BUILDER));
        setItem(player, 12, CharacterType.CRAFTSMAN, map.get(CharacterType.CRAFTSMAN));
        setItem(player, 14, CharacterType.ENGINEER, map.get(CharacterType.ENGINEER));
        setItem(player, 16, CharacterType.KNIGHT, map.get(CharacterType.KNIGHT));
        setItem(player, 22, CharacterType.ARCHER, map.get(CharacterType.ARCHER));

        fillEmptyWithBlank();
        open(player);
    }

    public CharactersLevelsViewInventory(Player player) {
        this(player, player.getUniqueId());
    }

    public void setItem(Player player, int index, CharacterType type, CharacterExperience experience) {
        setItem("character", index, Heads.get(type.getCharacter().getInventoryHeadValue()), event -> {
            event.setCancelled(true);
            new CharacterLevelTreeInventory(player, type);
        }, Map.ofEntries(
                Map.entry("%name%", type.getCharacter().getName()),
                Map.entry("%level%", String.valueOf(experience.getLevel())),
                Map.entry("%experience%", String.valueOf(experience.getExperience()))
        ));
    }
}
    