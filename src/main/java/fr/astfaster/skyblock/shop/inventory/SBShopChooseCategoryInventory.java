package fr.astfaster.skyblock.shop.inventory;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.shop.SBShopCategory;
import fr.astfaster.skyblock.shop.inventory.category.model.*;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SBShopChooseCategoryInventory extends SBInventory {

    private final Skyblock skyblock;

    public SBShopChooseCategoryInventory(Skyblock skyblock, Player owner) {
        super(owner, ChatColor.RED + "Shop", 3 * 9);
        this.skyblock = skyblock;

        this.addBlocksItem();
        this.addOresItem();
        this.addNatureItem();
        this.addMobLootsItem();
        this.addFarmingItem();
        this.addUpgradeItem();

        this.setFill(new SBItemBuilder(Material.STAINED_GLASS_PANE, 1, 15)
                .withName(" ")
                .toItemStack());
    }

    private void addBlocksItem() {
        this.setItem(10, new SBItemBuilder(Material.GRASS)
                .withName(ChatColor.YELLOW + SBShopCategory.BLOCKS.getDisplayName())
                .toItemStack(),
                event -> new SBShopBlocksInventory(this.skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addOresItem() {
        this.setItem(11, new SBItemBuilder(Material.DIAMOND)
                .withName(ChatColor.YELLOW + SBShopCategory.ORES.getDisplayName())
                .toItemStack(),
                event -> new SBShopOresInventory(this.skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addNatureItem() {
        this.setItem(12, new SBItemBuilder(Material.SAPLING)
                .withName(ChatColor.YELLOW + SBShopCategory.NATURE.getDisplayName())
                .toItemStack(),
                event -> new SBShopNatureInventory(this.skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addMobLootsItem() {
        this.setItem(13, new SBItemBuilder(Material.ROTTEN_FLESH)
                .withName(ChatColor.YELLOW + SBShopCategory.MOB_LOOTS.getDisplayName())
                .toItemStack(),
                event -> new SBShopMobLootsInventory(this.skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addFarmingItem() {
        this.setItem(14, new SBItemBuilder(Material.WHEAT)
                .withName(ChatColor.YELLOW + SBShopCategory.FARMING.getDisplayName())
                .toItemStack(),
                event -> new SBShopFarmingInventory(this.skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addUpgradeItem() {
        this.setItem(16, new SBItemBuilder(Material.REDSTONE_TORCH_ON)
                .withName(ChatColor.YELLOW + SBShopCategory.UPGRADES.getDisplayName())
                .toItemStack(),
                event -> new SBShopUpgradesInventory(this.skyblock, (Player) event.getWhoClicked()).open());
    }


}
