package fr.astfaster.skyblock.shop.inventory.category.model;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.shop.SBShopCategory;
import fr.astfaster.skyblock.shop.SBShopItem;
import fr.astfaster.skyblock.shop.SBShopManager;
import fr.astfaster.skyblock.shop.inventory.SBShopChooseCategoryInventory;
import fr.astfaster.skyblock.shop.inventory.category.SBShopCategoryInventory;
import fr.astfaster.skyblock.util.References;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;

public class SBShopMobLootsInventory extends SBShopCategoryInventory {

    public SBShopMobLootsInventory(Skyblock skyblock, Player owner) {
        super(skyblock, owner, SBShopCategory.MOB_LOOTS);
    }

}
