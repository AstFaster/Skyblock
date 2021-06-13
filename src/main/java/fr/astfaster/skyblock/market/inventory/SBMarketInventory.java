package fr.astfaster.skyblock.market.inventory;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.market.SBMarketItem;
import fr.astfaster.skyblock.market.SBMarketManager;
import fr.astfaster.skyblock.shop.SBShopItem;
import fr.astfaster.skyblock.shop.SBShopManager;
import fr.astfaster.skyblock.util.SerializerUtils;
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

public class SBMarketInventory extends SBInventory {

    private final Skyblock skyblock;

    public SBMarketInventory(Skyblock skyblock, Player owner) {
        super(owner, ChatColor.RED + "Hôtel des ventes", 6 * 9);
        this.skyblock = skyblock;

        this.setHorizontalLine(45, 53, new SBItemBuilder(Material.STAINED_GLASS_PANE, 1, 15).withName(" ").toItemStack());

        this.addItems();

        this.addCloseItem();
    }

    private void addItems() {
        final SBMarketManager marketManager = this.skyblock.getMarketManager();

        int slot = 0;
        for (String itemId : marketManager.getItems()) {
            final SBMarketItem item = marketManager.getItemFromRedis(itemId);
            final ItemStack itemStack = SerializerUtils.stringToItemStack(item.getItemStack());

            if (itemStack != null) {
                final ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setLore(Arrays.asList(
                        " ",
                        ChatColor.GRAY + "Prix d'achat :",
                        ChatColor.GOLD + "" + item.getBuyingPrice() + "$",
                        " ",
                        ChatColor.YELLOW + "Clique pour acheter !")
                );

                itemStack.setItemMeta(itemMeta);

                this.setItem(slot, itemStack);

                slot++;
            }
        }
    }

    private void addCloseItem() {
        this.setItem(49, new SBItemBuilder(Material.BARRIER)
                        .withName(ChatColor.RED + "Fermer l'inventaire")
                        .toItemStack(),
                event -> event.getWhoClicked().closeInventory());
    }

    @Override
    public void update() {
        for (int i = 0; i < this.getInventory().getSize() - 9; i++) {
            this.getInventory().setItem(i, null);
        }

        this.addItems();
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {
        this.skyblock.getMarketManager().getMarketsOpen().put((Player) event.getPlayer(), this);
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        this.skyblock.getMarketManager().getMarketsOpen().remove((Player) event.getPlayer());
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getClickedInventory();
        final int clickedSlot = event.getSlot();

        if (inventory.getHolder() instanceof SBMarketInventory) {
            if (inventory.getItem(clickedSlot) != null) {
                final ItemStack itemStack = inventory.getItem(clickedSlot);
                final SBMarketManager marketManager = this.skyblock.getMarketManager();
                final SBMarketItem item = marketManager.getItemByStack(itemStack);

                new SBConfirmBuyInventory(this.skyblock, item, player).open();
            }
        }
    }
}
