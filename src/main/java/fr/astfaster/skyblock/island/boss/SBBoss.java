package fr.astfaster.skyblock.island.boss;

import fr.astfaster.skyblock.island.boss.model.SBZombieBoss;

public enum SBBoss {

    ZOMBIE(new SBZombieBoss());

    private final SBBossEntity entity;

    SBBoss(SBBossEntity entity) {
        this.entity = entity;
    }

    public SBBossEntity getEntity() {
        return this.entity;
    }

    public static SBBoss getBossByName(String name) {
        for (SBBoss boss : values()) {
            if (boss.getEntity().getName().equals(name)) {
                return boss;
            }
        }
        return null;
    }

}
