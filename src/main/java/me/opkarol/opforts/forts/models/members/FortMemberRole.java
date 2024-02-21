package me.opkarol.opforts.forts.models.members;

import java.util.List;

public class FortMemberRole {
    private final String name;
    private List<FortMemberSettingType> allowedSettings;

    public String getName() {
        return name;
    }

    public List<FortMemberSettingType> getAllowedSettings() {
        return allowedSettings;
    }

    public void setAllowedSettings(List<FortMemberSettingType> allowedSettings) {
        this.allowedSettings = allowedSettings;
    }

    public FortMemberRole(String name, List<FortMemberSettingType> allowedSettings) {
        this.name = name;
        this.allowedSettings = allowedSettings;
    }
}
