package me.opkarol.opforts.schematics;

import me.opkarol.oplibrary.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;

class SchematicFileHandler {

    public static @NotNull String deserialize(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();

        File directory = new File(Plugin.getInstance().getDataFolder(), "schematics");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName + ".ops");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static void serialize(String fileName, String serializedData) {
        File directory = new File(Plugin.getInstance().getDataFolder(), "schematics");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName + ".ops");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(serializedData);
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
