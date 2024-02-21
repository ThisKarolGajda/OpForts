package me.opkarol.opforts.characters.types;

import me.opkarol.opforts.characters.AbstractCharacter;
import me.opkarol.opforts.characters.CharacterType;
import me.opkarol.opforts.characters.levelling.CharacterLevel;

import java.util.HashMap;
import java.util.Map;

public class KnightCharacterImpl extends AbstractCharacter {
    private final Map<Integer, CharacterLevel> map = new HashMap<>();

    @Override
    public String getName() {
        return "Knight";
    }

    @Override
    public CharacterType getType() {
        return CharacterType.KNIGHT;
    }

    @Override
    public String getInventoryHeadValue() {
        return "7c9d7a65f05c5d4451303e19cf3141d419f517f4f6d5bab1e6ebef263979bd17";
    }

    @Override
    public Map<Integer, CharacterLevel> getLevels() {
        return map;
    }
}
