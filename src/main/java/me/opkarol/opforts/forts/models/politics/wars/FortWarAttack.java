package me.opkarol.opforts.forts.models.politics.wars;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FortWarAttack {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private List<UUID> attackersOnline;
    private List<UUID> defendersOnline;
    private final UUID attackingFort;
    private final UUID defendingFort;
    private FortWarAttackStateType state;
    private int pointsGathered;

    public FortWarAttack(LocalDateTime start, LocalDateTime end, List<UUID> attackersOnline, List<UUID> defendersOnline, UUID attackingFort, UUID defendingFort, FortWarAttackStateType state, int pointsGathered) {
        this.start = start;
        this.end = end;
        this.attackersOnline = attackersOnline;
        this.defendersOnline = defendersOnline;
        this.attackingFort = attackingFort;
        this.defendingFort = defendingFort;
        this.state = state;
        this.pointsGathered = pointsGathered;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public List<UUID> getAttackersOnline() {
        return attackersOnline;
    }

    public void setAttackersOnline(List<UUID> attackersOnline) {
        this.attackersOnline = attackersOnline;
    }

    public List<UUID> getDefendersOnline() {
        return defendersOnline;
    }

    public void setDefendersOnline(List<UUID> defendersOnline) {
        this.defendersOnline = defendersOnline;
    }

    public UUID getAttackingFort() {
        return attackingFort;
    }

    public UUID getDefendingFort() {
        return defendingFort;
    }

    public FortWarAttackStateType getState() {
        return state;
    }

    public void setState(FortWarAttackStateType state) {
        this.state = state;
    }

    public int getPointsGathered() {
        return pointsGathered;
    }

    public void setPointsGathered(int pointsGathered) {
        this.pointsGathered = pointsGathered;
    }
}
