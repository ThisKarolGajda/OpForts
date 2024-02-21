package me.opkarol.opforts.characters.database;

import me.opkarol.oplibrary.Plugin;

public class CharactersDatabaseManager {
    private final CharactersDatabase database;

    public CharactersDatabaseManager(Plugin plugin) {
        this.database = new CharactersDatabase(plugin);
    }

    public CharactersDatabase getCharacters() {
        return database;
    }
}
