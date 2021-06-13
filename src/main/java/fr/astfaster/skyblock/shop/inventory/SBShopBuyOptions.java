package fr.astfaster.skyblock.shop.inventory;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.shop.SBShopItem;
import fr.astfaster.skyblock.shop.SBShopManager;
import fr.astfaster.skyblock.util.References;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SBShopBuyOptions extends SBInventory {

    private final SBShopItem item;
    private final SBInventory previousInventory;

    private final Skyblock skyblock;

    public SBShopBuyOptions(Skyblock skyblock, SBInventory previousInventory, SBShopItem item, Player owner) {
        super(owner, "Options d'achat", 4 * 9);
        this.skyblock = skyblock;
        this.previousInventory = previousInventory;
        this.item = item;

        this.addOptions();

        this.addGoBackItem();

        this.addCloseItem();

        this.setFill(new SBItemBuilder(Material.STAINED_GLASS_PANE, 1, 15)
                .withName(" ")
                .toItemStack());
    }

    private void addOptions() {
        this.setupItem(11, 1);
        this.setupItem(12, 5);
        this.setupItem(13, 10);
        this.setupItem(14, 32);
        this.setupItem(15, 64);
    }

    private void setupItem(int slot, int amount) {
        final SBShopManager shopManager = this.skyblock.getShopManager();
        final SBShopItem shopItem = new SBShopItem(this.item.getItemStack().clone(), this.item.getName(), this.item.getSlot(), this.item.getBuyingPrice(), this.item.getSellingPrice(), false, null);

        shopItem.getItemStack().setAmount(amount);
        shopItem.setBuyingPrice(this.item.getBuyingPrice() * amount);

        this.setItem(slot, shopManager.getItemWithMeta(shopItem), event -> shopManager.handleClick(event, shopItem, this));
    }

    private void addGoBackItem() {
        this.setItem(27, new SBItemBuilder(Material.SKULL_ITEM, 1, 3)
                        .withName(ChatColor.GRAY + "Revenir en arrière")
                        .withCustomHead(References.GO_BACK_HEAD)
                        .toItemStack(),
                event -> this.previousInventory.open());
    }

    private void addCloseItem() {
        this.setItem(31, new SBItemBuilder(Material.BARRIER)
                        .withName(ChatColor.RED + "Fermer l'inventaire")
                        .toItemStack(),
                event -> event.getWhoClicked().closeInventory());
    }

}
