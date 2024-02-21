package me.opkarol.opforts.characters;

import me.opkarol.oplibrary.location.StringUtil;
import me.opkarol.oporm.SerializableFieldOrm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CharacterModel implements SerializableFieldOrm, Serializable {
    private final Map<CharacterType, CharacterExperience> map;

    public CharacterModel(Map<CharacterType, CharacterExperience> map) {
        this.map = map;
    }

    public CharacterModel() {
        this.map = new HashMap<>();
        map.put(CharacterType.ALCHEMIST, new CharacterExperience(0, 0));
        map.put(CharacterType.ARCHER, new CharacterExperience(0, 0));
        map.put(CharacterType.BUILDER, new CharacterExperience(0, 0));
        map.put(CharacterType.CRAFTSMAN, new CharacterExperience(0, 0));
        map.put(CharacterType.ENGINEER, new CharacterExperience(0, 0));
        map.put(CharacterType.KNIGHT, new CharacterExperience(0, 0));
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        for (CharacterType type : map.keySet()) {
            CharacterExperience experience = map.get(type);
            //NAME, LEVEL, EXPERIENCE
            builder.append(type.name())
                    .append("-")
                    .append(experience.getLevel())
                    .append("-")
                    .append(experience.getExperience())
                    .append(",");
        }

        return builder.toString();
    }

    @Override
    public Object deserialize(String s) {
        Map<CharacterType, CharacterExperience> map = new HashMap<>();
        String[] types = s.split(",");
        for (String line : types) {
            String[] parts = line.split("-");
            CharacterType type = CharacterType.get(parts[0]);
            int level = StringUtil.getIntFromString(parts[1]);
            double experience = StringUtil.getDoubleFromString(parts[2]);
            map.put(type, new CharacterExperience(level, experience));
        }

        return new CharacterModel(map);
    }

    public Map<CharacterType, CharacterExperience> getMap() {
        return map;
    }
}
