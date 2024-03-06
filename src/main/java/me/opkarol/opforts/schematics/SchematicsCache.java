package me.opkarol.opforts.schematics;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class SchematicsCache {
    private static final HashSet<Schematic> schematics = new HashSet<>();

    public static @NotNull Schematic get(String name) {
        for (Schematic schematic : schematics) {
            if (schematic.getName() == null) {
                continue;
            }

            if (schematic.getName().equals(name)) {
                return schematic;
            }
        }

        Schematic schematic = Schematic.fromSerializedFile(name);
        schematics.add(schematic);
        return schematic;
    }
}
