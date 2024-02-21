package me.opkarol.opforts.characters.types;

import me.opkarol.opforts.characters.AbstractCharacter;
import me.opkarol.opforts.characters.CharacterType;
import me.opkarol.opforts.characters.levelling.CharacterLevel;

import java.util.HashMap;
import java.util.Map;

public class BuilderCharacterImpl extends AbstractCharacter {
    private final Map<Integer, CharacterLevel> map = new HashMap<>();

    @Override
    public String getName() {
        return "Builder";
    }

    @Override
    public CharacterType getType() {
        return CharacterType.BUILDER;
    }

    @Override
    public String getInventoryHeadValue() {
        return "65390e3cc1f07441e9de139840d08217f861857ff98f4211c79f3c0b4860d80e";
    }

    @Override
    public Map<Integer, CharacterLevel> getLevels() {
        return map;
    }
}
