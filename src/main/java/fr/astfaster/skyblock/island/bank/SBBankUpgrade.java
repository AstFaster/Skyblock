package fr.astfaster.skyblock.island.bank;

public enum SBBankUpgrade {

    TIER_1(9),
    TIER_2(2 * 9),
    TIER_3(3 * 9);

    private final int slot;

    SBBankUpgrade(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }
}
