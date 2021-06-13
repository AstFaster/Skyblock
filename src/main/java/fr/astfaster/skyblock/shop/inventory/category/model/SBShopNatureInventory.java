package fr.astfaster.skyblock.shop.inventory.category.model;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.shop.SBShopCategory;
import fr.astfaster.skyblock.shop.inventory.category.SBShopCategoryInventory;
import org.bukkit.entity.Player;

public class SBShopNatureInventory extends SBShopCategoryInventory {

    public SBShopNatureInventory(Skyblock skyblock, Player owner) {
        super(skyblock, owner, SBShopCategory.NATURE);
    }

}
