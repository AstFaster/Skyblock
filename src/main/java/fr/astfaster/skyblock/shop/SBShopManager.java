package fr.astfaster.skyblock.shop;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.player.SBPlayerManager;
import fr.astfaster.skyblock.shop.inventory.SBShopBuyOptions;
import fr.astfaster.skyblock.shop.inventory.category.SBShopCategoryInventory;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SBShopManager {

    private final Map<SBShopCategory, Set<SBShopItem>> items;

    private final Skyblock skyblock;

    public SBShopManager(Skyblock skyblock) {
        this.skyblock = skyblock;
        this.items = new HashMap<>();
    }

    public void handleClick(InventoryClickEvent event, SBShopItem item, SBInventory inventory) {
        final Player player = (Player) event.getWhoClicked();

        if (event.getClick().equals(ClickType.LEFT)) {
            if (item.getAction() == null) {
                this.buyItem(player, inventory, item);
            } else {
                item.getAction().accept(event);
            }
        } else if (event.getClick().equals(ClickType.RIGHT)) {
            if (item.isStackable()) {
                new SBShopBuyOptions(this.skyblock, inventory, item, player).open();
            }
        }
    }

    public void buyItem(Player player, SBInventory inventory, SBShopItem item) {
        final SBPlayerManager playerManager = this.skyblock.getPlayerManager();
        final SBPlayer sbPlayer = playerManager.getPlayerFromRedis(player.getUniqueId());
        final ItemStack itemStack = item.getItemStack();

        if (sbPlayer.getMoney() >= item.getBuyingPrice()) {
            if (!this.hasInventoryFull(player)) {
                sbPlayer.setMoney(sbPlayer.getMoney() - item.getBuyingPrice());

                if (inventory instanceof SBShopCategoryInventory) {
                    ((SBShopCategoryInventory) inventory).removeSellingLore(player.getInventory());
                }

                player.getInventory().addItem(itemStack);

                playerManager.sendPlayerToRedis(sbPlayer);

                player.sendMessage(ChatColor.GOLD + "Vous venez d'acheter " +
                        ChatColor.RED + itemStack.getType().name() +
                        ChatColor.GRAY + " x" + itemStack.getAmount() +
                        ChatColor.GOLD + " pour " +
                        ChatColor.RED + item.getBuyingPrice() + "$" +
                        ChatColor.GOLD + "."
                );

                inventory.update();
            } else {
                player.sendMessage(ChatColor.RED + "Tu n'as pas assez de place dans ton inventaire !");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Tu n'as pas l'argent nécessaire !");
        }
    }

    public void sellItem(Player player, SBShopCategoryInventory inventory, ItemStack itemStack, int slot, SBShopItem item) {
        final SBPlayerManager playerManager = this.skyblock.getPlayerManager();
        final SBPlayer sbPlayer = playerManager.getPlayerFromRedis(player.getUniqueId());

        if (item != null) {
            if (item.getSellingPrice() > 0) {
                final double price = item.getSellingPrice() * itemStack.getAmount();
                sbPlayer.setMoney(sbPlayer.getMoney() + price);

                player.getInventory().setItem(slot, null);

                playerManager.sendPlayerToRedis(sbPlayer);

                player.sendMessage(ChatColor.GOLD + "Vous avez correctement vendu " +
                        ChatColor.RED + itemStack.getType().name() +
                        ChatColor.GRAY + " x" + itemStack.getAmount() +
                        ChatColor.GOLD + " pour " +
                        ChatColor.RED + price + "$" +
                        ChatColor.GOLD + "."
                );

                inventory.onSell(item, itemStack.getAmount());
            } else {
                player.sendMessage(ChatColor.RED + "Cet item ne peut pas être vendu !");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Cet item ne peut pas être vendu !");
        }
    }

    private boolean hasInventoryFull(Player player) {
        final Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getItemWithMeta(SBShopItem item) {
        final SBItemBuilder itemBuilder = new SBItemBuilder(item.getItemStack().clone()).withAllItemFlags();

        if (!item.getName().isEmpty()) {
            itemBuilder.withName(ChatColor.WHITE + item.getName());
        }

        if (item.isStackable()) {
            itemBuilder.withLore(
                    " ",
                    ChatColor.GRAY + "Prix d'achat :",
                    ChatColor.GOLD + "" + item.getBuyingPrice() + "$",
                    " ",
                    ChatColor.YELLOW + "Clique pour acheter !",
                   ChatColor.YELLOW + "Clique droit pour plus d'options !"
            );
        } else {
            itemBuilder.withLore(
                    " ",
                    ChatColor.GRAY + "Prix d'achat :",
                    ChatColor.GOLD + "" + item.getBuyingPrice() + "$",
                    " ",
                    ChatColor.YELLOW + "Clique pour acheter !"
            );
        }

        return itemBuilder.toItemStack();
    }

    public SBShopItem getItemByCategoryAndSlot(SBShopCategory category, int slot) {
        for (SBShopItem item : this.items.get(category)) {
            if (item.getSlot() == slot) return item;
        }
        return null;
    }

    public SBShopItem getItemByStack(ItemStack itemStack) {
        for (Map.Entry<SBShopCategory, Set<SBShopItem>> entry : this.items.entrySet()) {
            for (SBShopItem item : entry.getValue()) {
                final ItemStack stack = item.getItemStack();

                if (itemStack.getType().equals(stack.getType())) {
                    if (!itemStack.getItemMeta().hasDisplayName() && !stack.getItemMeta().hasDisplayName()) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    public void addItem(SBShopCategory category, SBShopItem item) {
        Set<SBShopItem> itemsSet = this.items.get(category);

        if (itemsSet == null) itemsSet = new HashSet<>();

        itemsSet.add(item);

        this.items.put(category, itemsSet);
    }

    public Map<SBShopCategory, Set<SBShopItem>> getItems() {
        return this.items;
    }
}
