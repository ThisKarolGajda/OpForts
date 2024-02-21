package me.opkarol.opforts.characters.types;

import me.opkarol.opforts.characters.AbstractCharacter;
import me.opkarol.opforts.characters.CharacterType;
import me.opkarol.opforts.characters.levelling.CharacterLevel;

import java.util.HashMap;
import java.util.Map;

public class EngineerCharacterImpl extends AbstractCharacter {
    private final Map<Integer, CharacterLevel> map = new HashMap<>();

    @Override
    public String getName() {
        return "Engineer";
    }

    @Override
    public CharacterType getType() {
        return CharacterType.ENGINEER;
    }

    @Override
    public String getInventoryHeadValue() {
        return "9ccc384c0535124e008395815e75b08f9629732164fdd0ffb327cffff639a834";
    }

    @Override
    public Map<Integer, CharacterLevel> getLevels() {
        return map;
    }
}
