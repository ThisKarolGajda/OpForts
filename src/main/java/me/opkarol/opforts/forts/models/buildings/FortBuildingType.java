package me.opkarol.opforts.forts.models.buildings;

import java.io.Serializable;

public enum FortBuildingType implements Serializable {
    FORT("Fort", "Potężna twierdza, stanowiąca serce fortu i główne miejsce zarządzania.", "", new FortBuildingLevelsHeartLocation()),
    HOUSE("Dom", "Podstawowa kwatera dla graczy, także punkt respawnu.", "schem", new FortBuildingLevelsHeartLocation()),
    STORAGE("Magazyn", "Przechowuje materiały i przedmioty.", "", new FortBuildingLevelsHeartLocation()),
    TREASURY("Skarbiec", "Przechowuje skarby fortu.", "", new FortBuildingLevelsHeartLocation()),
    FARM("Farma", "Uprawia rośliny, wymaga współpracy graczy.", "", new FortBuildingLevelsHeartLocation()),
    BLACKSMITH("Kuźnia", "Tworzy broń i zbroję z kamienia i żelaza.", "", new FortBuildingLevelsHeartLocation()),
    TRAINING_ARENA("Arena treningowa", "Miejsce do treningu umiejętności i zdobywania doświadczenia.", "", new FortBuildingLevelsHeartLocation()),
    BOTANIC_GARDEN("Ogród botaniczny", "Urokliwe miejsce z różnorodną roślinnością.", "", new FortBuildingLevelsHeartLocation()),
    WATCHTOWER("Strażnica", "Pełni funkcję obserwacyjną i obronną.", "", new FortBuildingLevelsHeartLocation()),
    MINE("Kopalnia", "Wybiera surowce mineralne dla budowy i produkcji.", "", new FortBuildingLevelsHeartLocation()),
    ;

    private final String familyName;
    private final String description;
    private final String schematicPath;
    private final FortBuildingLevelsHeartLocation heartLocation;

    FortBuildingType(String familyName, String description, String schematicPath, FortBuildingLevelsHeartLocation heartLocation) {
        this.familyName = familyName;
        this.description = description;
        this.schematicPath = schematicPath;
        this.heartLocation = heartLocation;
    }

    @Override
    public String toString() {
        return familyName;
    }

    public String getDescription() {
        return description;
    }

    public String getSchematicPath() {
        return schematicPath;
    }

    public FortBuildingLevelsHeartLocation getHeartLocation() {
        return heartLocation;
    }
}
