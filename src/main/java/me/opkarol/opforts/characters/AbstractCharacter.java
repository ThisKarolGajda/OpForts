package me.opkarol.opforts.characters;

import me.opkarol.opforts.characters.levelling.CharacterLevel;

import java.util.Map;

public abstract class AbstractCharacter {

    public abstract String getName();

    public abstract CharacterType getType();

    public abstract String getInventoryHeadValue();

    public abstract Map<Integer, CharacterLevel> getLevels();
}
