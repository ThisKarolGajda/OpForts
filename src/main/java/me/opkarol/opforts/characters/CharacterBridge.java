package me.opkarol.opforts.characters;

import me.opkarol.opforts.characters.types.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CharacterBridge {
    private static final Map<CharacterType, AbstractCharacter> characterTypeMap = new HashMap<>();

    static {
        characterTypeMap.put(CharacterType.ALCHEMIST, new AlchemistCharacterImpl());
        characterTypeMap.put(CharacterType.ARCHER, new ArcherCharacterImpl());
        characterTypeMap.put(CharacterType.BUILDER, new BuilderCharacterImpl());
        characterTypeMap.put(CharacterType.CRAFTSMAN, new CraftsmanCharacterImpl());
        characterTypeMap.put(CharacterType.ENGINEER, new EngineerCharacterImpl());
        characterTypeMap.put(CharacterType.KNIGHT, new KnightCharacterImpl());
    }

    public static @NotNull AbstractCharacter get(CharacterType type) {
        return characterTypeMap.get(type);
    }
}
