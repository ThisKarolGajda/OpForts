package me.opkarol.opforts.characters.types;

import me.opkarol.opforts.characters.AbstractCharacter;
import me.opkarol.opforts.characters.CharacterType;
import me.opkarol.opforts.characters.levelling.CharacterLevel;
import me.opkarol.opforts.characters.levelling.CharacterReward;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AlchemistCharacterImpl extends AbstractCharacter {
    private final Map<Integer, CharacterLevel> map = new HashMap<>();
    {
        map.put(1, new CharacterLevel(50, new CharacterReward(CharacterReward.Type.MONEY, 500)));
        map.put(2, new CharacterLevel(50, new CharacterReward(CharacterReward.Type.ITEM, new ItemStack(Material.STONE, 1))));
    }

    @Override
    public String getName() {
        return "Alchemist";
    }

    @Override
    public CharacterType getType() {
        return CharacterType.ALCHEMIST;
    }

    @Override
    public String getInventoryHeadValue() {
        return "154f8c964451eb7bea3607bad7711647eca3ba0e3bb1ca10bd363974ba9a4e00";
    }

    @Override
    public Map<Integer, CharacterLevel> getLevels() {
        return map;
    }
}
