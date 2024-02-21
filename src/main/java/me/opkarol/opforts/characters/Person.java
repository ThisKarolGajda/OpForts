package me.opkarol.opforts.characters;

import me.opkarol.opforts.characters.database.CharactersDatabaseManager;
import me.opkarol.oplibrary.Plugin;
import me.opkarol.oplibrary.wrappers.OpActionBar;
import me.opkarol.oporm.DatabaseEntity;
import me.opkarol.oporm.PrimaryKey;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

public class Person implements DatabaseEntity<String>, Serializable {
    public CharacterModel charactersExperience;
    @PrimaryKey
    public String uuid;

    @Contract(pure = true)
    public Person(CharacterModel charactersExperience, @NotNull UUID user) {
        this.charactersExperience = charactersExperience;
        this.uuid = user.toString();
    }

    public Person(UUID user) {
        this(new CharacterModel(), user);
    }

    public Person() {

    }

    public CharacterModel getCharactersExperience() {
        return charactersExperience;
    }

    public UUID getUUID() {
        return UUID.fromString(uuid);
    }

    public OfflinePlayer getOfflineUser() {
        return Bukkit.getOfflinePlayer(getUUID());
    }

    @Override
    public String getId() {
        return uuid;
    }

    private void addExperience(CharacterType type, int experience) {
        CharacterExperience characterExperience = charactersExperience.getMap().get(type);
        characterExperience.addExperience(experience);

        if (type.getCharacter().getLevels().size() != 0) {
            int requiredExperience = getRequiredScoreForNextLevel(type);
            while (characterExperience.getExperience() > requiredExperience) {
                characterExperience.increaseLevel();
                characterExperience.setExperience(characterExperience.getExperience() - requiredExperience);
                requiredExperience = getRequiredScoreForNextLevel(type);
            }
        }

        charactersExperience.getMap().put(type, characterExperience);
        Plugin.getDependency().get(CharactersDatabaseManager.class).getCharacters().save(this);
    }

    public void addExperienceWithNotify(@NotNull Player player, CharacterType type, int experience) {
        addExperience(type, experience);
        OpActionBar actionBar = new OpActionBar("&6TYPE: " + type.getCharacter().getName() + " -> " + experience + "/" + getRequiredScoreForNextLevel(type));
        actionBar.build().send(player);
    }

    public int getRequiredScoreForNextLevel(@NotNull CharacterType type) {
        CharacterExperience characterExperience = charactersExperience.getMap().get(type);
        if (type.getCharacter().getLevels().size() == 0 || type.getCharacter().getLevels().containsKey(characterExperience.getLevel() + 1)) {
            return 0;
        }

        return type.getCharacter().getLevels().get(characterExperience.getLevel() + 1).experience();
    }
}
