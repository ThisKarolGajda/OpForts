package me.opkarol.opforts.characters.commands;

import me.opkarol.opforts.characters.inventories.CharactersLevelsViewInventory;
import me.opkarol.oplibrary.commands.annotations.Command;
import me.opkarol.oplibrary.commands.annotations.Subcommand;
import org.bukkit.entity.Player;

@Command("character")
public class CharacterCommand {

    @Subcommand("charactersLevelsViewInventory")
    public void openCharactersLevelsViewInventory(Player player) {
        new CharactersLevelsViewInventory(player);
    }
}
