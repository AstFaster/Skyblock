package fr.astfaster.skyblock.shop.inventory.category.model;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.shop.SBShopCategory;
import fr.astfaster.skyblock.shop.inventory.category.SBShopCategoryInventory;
import org.bukkit.entity.Player;

public class SBShopOresInventory extends SBShopCategoryInventory {

    public SBShopOresInventory(Skyblock skyblock, Player owner) {
        super(skyblock, owner, SBShopCategory.ORES);
    }

}
