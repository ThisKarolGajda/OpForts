package me.opkarol.opforts.forts.building;

import java.util.List;

public enum BuildingsPreviewSchematicsOrderType {
    HOUSE("house-1", "house-2", "house-3", "house-4"),
    ;

    private final List<String> schematics;

    BuildingsPreviewSchematicsOrderType(String... schematics) {
        this.schematics = List.of(schematics);
    }

    public List<String> getSchematics() {
        return schematics;
    }
}
