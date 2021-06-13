package fr.astfaster.skyblock.shop;

public enum SBShopCategory {

    BLOCKS("Blocs", 5 * 9),
    ORES("Minerais", 5 * 9),
    NATURE("Nature", 5 * 9),
    MOB_LOOTS("Loots de Mob", 5 * 9),
    FARMING("Plantations", 5 * 9),
    UPGRADES("Améliorations", 5 * 9);

    private final String displayName;
    private final int inventorySize;

    SBShopCategory(String displayName, int inventorySize) {
        this.displayName = displayName;
        this.inventorySize = inventorySize;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getInventorySize() {
        return this.inventorySize;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
