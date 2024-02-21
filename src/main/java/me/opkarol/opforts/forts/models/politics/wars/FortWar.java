package me.opkarol.opforts.forts.models.politics.wars;

import java.util.List;
import java.util.UUID;

public class FortWar {
    private final UUID declaredWar;
    private final UUID declaredAgainst;
    private List<FortWarAttack> attacks;

    public FortWar(UUID declaredWar, UUID declaredAgainst, List<FortWarAttack> attacks) {
        this.declaredWar = declaredWar;
        this.declaredAgainst = declaredAgainst;
        this.attacks = attacks;
    }

    public UUID getDeclaredWar() {
        return declaredWar;
    }

    public UUID getDeclaredAgainst() {
        return declaredAgainst;
    }

    public List<FortWarAttack> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<FortWarAttack> attacks) {
        this.attacks = attacks;
    }
}
