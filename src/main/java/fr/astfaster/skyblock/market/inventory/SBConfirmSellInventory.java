package fr.astfaster.skyblock.market.inventory;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.market.SBMarketItem;
import fr.astfaster.skyblock.market.SBMarketManager;
import fr.astfaster.skyblock.util.MoneyUtils;
import fr.astfaster.skyblock.util.SerializerUtils;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.ItemColor;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SBConfirmSellInventory extends SBInventory {

    private final double sellPrice;

    private final int slot;
    private final SBMarketItem item;

    private final Skyblock skyblock;

    public SBConfirmSellInventory(Skyblock skyblock, SBMarketItem item, int slot, Player owner) {
        super(owner, "Confirmer la vente", 5 * 9);
        this.skyblock = skyblock;
        this.item = item;
        this.slot = slot;

        final SBMarketManager marketManager = this.skyblock.getMarketManager();

        this.sellPrice = MoneyUtils.roundMoney(String.valueOf(item.getBuyingPrice() * marketManager.getPercentage()));

        this.addSellInformationItem();

        this.setItem(29, new SBItemBuilder(Material.WOOL)
                .withName(ChatColor.GREEN + "Oui")
                .withAllItemFlags()
                .withColor(ItemColor.GREEN)
                .toItemStack(), this.sellItem()
        );

        this.setItem(33, new SBItemBuilder(Material.WOOL)
                .withName(ChatColor.RED + "Non")
                .withAllItemFlags()
                .withColor(ItemColor.RED)
                .toItemStack(), this.no()
        );

        this.setFill(new SBItemBuilder(Material.STAINED_GLASS_PANE, 1, 15)
                .withName(" ")
                .toItemStack());
    }

    private void addSellInformationItem() {
        final ItemStack itemStack = SerializerUtils.stringToItemStack(this.item.getItemStack());

        this.setItem(13, new SBItemBuilder(itemStack.clone())
                .withName(ChatColor.YELLOW + "Récapitulatif")
                .withLore(
                        " ",
                        ChatColor.YELLOW + "Item : " + ChatColor.GOLD + itemStack.getType().name() + ChatColor.GRAY + " x" + itemStack.getAmount(),
                        ChatColor.YELLOW + "Prix d'achat : " + ChatColor.GOLD + this.item.getBuyingPrice() + "$",
                        ChatColor.YELLOW + "Prix de mise en vente : " + ChatColor.GOLD + this.sellPrice + "$"
                )
                .toItemStack());
    }

    private Consumer<InventoryClickEvent> sellItem() {
        return event -> {
            final SBMarketManager marketManager = this.skyblock.getMarketManager();

            marketManager.sellItem((Player) event.getWhoClicked(), this.item, this.slot, this.sellPrice);

            event.getWhoClicked().closeInventory();
        };
    }

    private Consumer<InventoryClickEvent> no() {
        return event -> {
            final Player player = (Player) event.getWhoClicked();

            player.sendMessage(ChatColor.GOLD + "Mise en vente annulée !");
            player.closeInventory();
        };
    }

}
