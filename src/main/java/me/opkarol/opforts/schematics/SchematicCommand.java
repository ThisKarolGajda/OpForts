package me.opkarol.opforts.schematics;

import me.opkarol.oplibrary.commands.annotations.Command;
import me.opkarol.oplibrary.commands.annotations.Subcommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command("fort-schematic")
public class SchematicCommand {
    private Schematic schematic;

    @Subcommand("load")
    public void testScanChunk(@NotNull Player player) {
        schematic = Schematic.fromChunk(player.getLocation().getChunk(), player.getLocation().getBlockY(), player.getLocation().getBlockY() + 16);
    }

    @Subcommand("paste")
    public void pasteSchematicAtChunk(@NotNull Player player) {
        if (schematic == null) {
            return;
        }

        schematic.paste(player.getLocation().getChunk());
    }

    @Subcommand("serialize")
    public void serializeSchematic(@NotNull Player player, String fileName) {
        if (schematic == null) {
            return;
        }

        schematic.serializeFile(fileName);
    }

    @Subcommand("deserialize")
    public void deserializeSchematic(@NotNull Player player, String fileName) {
        schematic = Schematic.fromSerializedFile(fileName);
    }

    @Subcommand("display-preview")
    public void displayPreview(@NotNull Player player) {
        if (schematic == null) {
            return;
        }

        schematic.displayPreview(player);
    }
}
