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
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class SBShopBlocksInventory extends SBShopCategoryInventory {

    public SBShopBlocksInventory(Skyblock skyblock, Player owner) {
        super(skyblock, owner, SBShopCategory.BLOCKS);

        this.addItems();
    }

    @Override
    protected void addItems() {
        final SBShopManager shopManager = this.skyblock.getShopManager();
        final SBShopItem gravel = new SBShopItem(new ItemStack(Material.GRAVEL), "", 0, 3.0d, 1.0d, true, null);

        shopManager.addItem(this.category, gravel);

        super.addItems();
    }
}
