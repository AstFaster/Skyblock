package fr.astfaster.skyblock.shop.inventory.category;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.shop.SBShopCategory;
import fr.astfaster.skyblock.shop.SBShopItem;
import fr.astfaster.skyblock.shop.SBShopManager;
import fr.astfaster.skyblock.shop.inventory.SBShopChooseCategoryInventory;
import fr.astfaster.skyblock.util.References;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class SBShopCategoryInventory extends SBInventory {

    protected final SBShopCategory category;

    protected final Skyblock skyblock;

    public SBShopCategoryInventory(Skyblock skyblock, Player owner, SBShopCategory category) {
        super(owner, ChatColor.RED + "Shop > " + category.getDisplayName(), SBShopCategory.BLOCKS.getInventorySize());
        this.skyblock = skyblock;
        this.category = category;

        this.setHorizontalLine(36, 44, new SBItemBuilder(Material.STAINED_GLASS_PANE, 1, 15).withName(" ").toItemStack());

        this.addGoBackItem();

        this.addSellItem();
    }

    protected void addItems() {
        final SBShopManager shopManager = this.skyblock.getShopManager();
        final Set<SBShopItem> items = shopManager.getItems().get(this.category);

        if (items != null) {
            for (SBShopItem item : items) {
                this.setItem(item.getSlot(), shopManager.getItemWithMeta(item), event -> shopManager.handleClick(event, item, this));
            }
        }
    }

    private void addGoBackItem() {
        this.setItem(36, new SBItemBuilder(Material.SKULL_ITEM, 1, 3)
                        .withName(ChatColor.GRAY + "Revenir en arrière")
                        .withCustomHead(References.GO_BACK_HEAD)
                        .toItemStack(),
                event -> new SBShopChooseCategoryInventory(this.skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addSellItem() {
        this.setItem(40, new SBItemBuilder(Material.HOPPER)
                        .withName(ChatColor.GREEN + "Vendre des items")
                        .withLore(ChatColor.GRAY + "Clique sur des items dans ton inventaire", ChatColor.GRAY + "pour les vendre.")
                        .toItemStack());
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {
        final Player player = (Player) event.getPlayer();

        this.addSellingLore(player.getInventory());
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();

        this.removeSellingLore(player.getInventory());
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getClickedInventory();
        final int clickedSlot = event.getSlot();

        if (inventory == player.getInventory()) {
            if (inventory.getItem(clickedSlot) != null) {
                final ItemStack itemStack = inventory.getItem(clickedSlot);
                final SBShopManager shopManager = this.skyblock.getShopManager();
                final SBShopItem item = shopManager.getItemByStack(itemStack);

                shopManager.sellItem(player, this, itemStack, clickedSlot, item);
            }
        }
    }

    public void onSell(SBShopItem shopItem, int amount) {
        final SBShopItem item = shopItem.clone();
        final ItemStack itemStack = item.getItemStack();

        item.setBuyingPrice(item.getSellingPrice() * amount);

        itemStack.setAmount(amount);

        final List<String> lore = Arrays.asList(
                " ",
                ChatColor.GRAY + "Prix de rachat :",
                ChatColor.GOLD + "" + item.getBuyingPrice() + "$",
                " ",
                ChatColor.YELLOW + "Clique pour racheter !"
        );

        this.setItem(40, new SBItemBuilder(itemStack).withLore(lore).toItemStack(),
                event -> {
                    final SBShopManager shopManager = this.skyblock.getShopManager();

                    shopManager.buyItem((Player) event.getWhoClicked(), this, item);

                    this.addSellItem();
                });
    }

    @Override
    public void update() {
        this.addSellingLore(this.getOwner().getInventory());
    }

    private void addSellingLore(Inventory inventory) {
        final SBShopManager shopManager = this.skyblock.getShopManager();

        for (int i = 0; i < inventory.getSize(); i++) {
            final ItemStack itemStack = inventory.getItem(i);

            if (itemStack != null) {
                final SBShopItem item = shopManager.getItemByStack(itemStack);
                final ItemMeta itemMeta = itemStack.getItemMeta();

                if (item != null) {
                    final double price = item.getSellingPrice() * itemStack.getAmount();

                    itemMeta.setLore(Arrays.asList(
                            " ",
                            ChatColor.GRAY + "Prix de vente :",
                            ChatColor.GOLD + "" + price + "$",
                            " ",
                            ChatColor.YELLOW + "Clique pour vendre !"));
                }

                itemStack.setItemMeta(itemMeta);
            }
        }
    }

    public void removeSellingLore(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            final ItemStack itemStack = inventory.getItem(i);

            if (itemStack != null) {
                final ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setLore(null);

                itemStack.setItemMeta(itemMeta);
            }
        }
    }

    public SBShopCategory getCategory() {
        return this.category;
    }
}
