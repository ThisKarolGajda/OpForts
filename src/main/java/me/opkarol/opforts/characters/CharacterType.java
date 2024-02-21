package me.opkarol.opforts.characters;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum CharacterType {
    KNIGHT,
    ARCHER,
    ALCHEMIST,
    CRAFTSMAN,
    BUILDER,
    ENGINEER,
    ;

    public @NotNull AbstractCharacter getCharacter() {
        return CharacterBridge.get(this);
    }

    public static CharacterType get(String name) {
        return Arrays.stream(CharacterType.values())
                .filter(characterType -> characterType.name().equals(name))
                .findAny()
                .orElseThrow();
    }
}
