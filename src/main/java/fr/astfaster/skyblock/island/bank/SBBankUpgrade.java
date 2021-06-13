package fr.astfaster.skyblock.island.bank;

public enum SBBankUpgrade {

    TIER_0(0, "Tier 1", 9),
    TIER_1(1, "Tier 1", 2 * 9),
    TIER_2(2, "Tier 2", 3 * 9),
    TIER_3(3, "Tier 3", 4 * 9);

    private final int id;
    private final String displayName;
    private final int slot;

    SBBankUpgrade(int id, String displayName, int slot) {
        this.id = id;
        this.displayName = displayName;
        this.slot = slot;
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getSlot() {
        return this.slot;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
