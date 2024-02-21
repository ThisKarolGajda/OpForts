package me.opkarol.opforts.characters.types;

import me.opkarol.opforts.characters.AbstractCharacter;
import me.opkarol.opforts.characters.CharacterType;
import me.opkarol.opforts.characters.levelling.CharacterLevel;

import java.util.HashMap;
import java.util.Map;

public class CraftsmanCharacterImpl extends AbstractCharacter {
    private final Map<Integer, CharacterLevel> map = new HashMap<>();

    @Override
    public String getName() {
        return "Craftsman";
    }

    @Override
    public CharacterType getType() {
        return CharacterType.CRAFTSMAN;
    }

    @Override
    public String getInventoryHeadValue() {
        return "a6b269b33dd1b1633ffc1740806ab2f1b8133e72a54dadcb7328badd55b7b918";
    }

    @Override
    public Map<Integer, CharacterLevel> getLevels() {
        return map;
    }
}
