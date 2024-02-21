package me.opkarol.opforts.characters.types;

import me.opkarol.opforts.characters.AbstractCharacter;
import me.opkarol.opforts.characters.CharacterType;
import me.opkarol.opforts.characters.levelling.CharacterLevel;

import java.util.HashMap;
import java.util.Map;

public class ArcherCharacterImpl extends AbstractCharacter {
    private final Map<Integer, CharacterLevel> map = new HashMap<>();

    @Override
    public String getName() {
        return "Archer";
    }

    @Override
    public CharacterType getType() {
        return CharacterType.ARCHER;
    }

    @Override
    public String getInventoryHeadValue() {
        return "f4c8d286ef86c1e9009372e84a41c3112d7201bcb0771a6df3777dcc0dc2025b";
    }

    @Override
    public Map<Integer, CharacterLevel> getLevels() {
        return map;
    }
}
