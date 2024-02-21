package me.opkarol.opforts.forts.models.members;

import me.opkarol.oporm.SerializableFieldOrm;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class FortMembers implements SerializableFieldOrm, Serializable {
    private UUID owner;
    private List<FortMember> members;
    private List<FortMemberRole> roles;

    public FortMembers(UUID owner, List<FortMember> members, List<FortMemberRole> roles) {
        this.owner = owner;
        this.members = members;
        this.roles = roles;
    }

    public FortMembers() {

    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public Object deserialize(String s) {
        return null;
    }

    public boolean isMember(UUID player) {
        return members.stream().anyMatch(member -> member.getMember().equals(player));
    }

    public List<FortMember> getMembers() {
        return members;
    }

    public List<FortMemberRole> getRoles() {
        return roles;
    }

    public UUID getOwner() {
        return owner;
    }
}
