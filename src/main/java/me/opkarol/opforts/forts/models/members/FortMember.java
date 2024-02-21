package me.opkarol.opforts.forts.models.members;

import java.util.UUID;

public class FortMember {
    private final UUID member;
    private String role;

    public FortMember(UUID member, String role) {
        this.member = member;
        this.role = role;
    }

    public UUID getMember() {
        return member;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
