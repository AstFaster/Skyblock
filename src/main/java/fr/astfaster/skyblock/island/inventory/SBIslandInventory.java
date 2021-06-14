package fr.astfaster.skyblock.island.inventory;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.market.inventory.SBMarketInventory;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.shop.inventory.SBShopChooseCategoryInventory;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SBIslandInventory extends SBInventory {

    private final SBPlayer player;

    private final Skyblock skyblock;

    public SBIslandInventory(Skyblock skyblock, Player owner) {
        super(owner, ChatColor.RED + "Menu de l'île", 4 * 9);
        this.skyblock = skyblock;
        this.player = this.skyblock.getPlayerManager().getPlayerFromRedis(owner.getUniqueId());

        if (player.getIsland().isEmpty()) {
            this.addCreateIslandItem();
        } else {
            this.addIslandInformationItem();
        }

        this.addMarketItem();
        this.addShopItem();
        this.addBankItem();

        this.setFill(new SBItemBuilder(Material.STAINED_GLASS_PANE, 1, 15)
                .withName(" ")
                .toItemStack());
    }

    private void addCreateIslandItem() {
        this.setItem(13, new SBItemBuilder(Material.SAPLING).withName(ChatColor.YELLOW + "Créer son île").toItemStack(), event -> {
            final SBCreateIslandInventory createIslandInventory = new SBCreateIslandInventory(this.skyblock, (Player) event.getWhoClicked());
            createIslandInventory.open();
        });
    }

    private void addIslandInformationItem() {
        final String islandHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyOGVkNDU4MDI0MDBmNDY1YjVjNGUzYTZiN2E5ZjJiNmE1YjNkNDc4YjZmZDg0OTI1Y2M1ZDk4ODM5MWM3ZCJ9fX0=";

        this.setItem(13, new SBItemBuilder(Material.SKULL_ITEM, 1, 3)
                .withName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Informations de l'île")
                .withCustomHead(islandHead)
                .toItemStack(),
                event -> new SBIslandInformationInventory(skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addMarketItem() {
        this.setItem(21, new SBItemBuilder(Material.GOLD_INGOT)
                .withName(ChatColor.YELLOW + "Hôtel des ventes")
                .withLore(ChatColor.GOLD + "Vend et achète des items", ChatColor.GOLD + "aux autres joueurs")
                .toItemStack(),
                event -> new SBMarketInventory(this.skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addShopItem() {
        this.setItem(22, new SBItemBuilder(Material.EMERALD)
                .withName(ChatColor.YELLOW + "Shop")
                .withLore(ChatColor.GOLD + "Vend et achète des ressources")
                .toItemStack(),
                event -> new SBShopChooseCategoryInventory(this.skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addBankItem() {
        this.setItem(23, new SBItemBuilder(Material.CHEST)
                .withName(ChatColor.YELLOW + "Banque")
                .withLore(ChatColor.GOLD + "Stocke tes items dans le coffre", ChatColor.GOLD + "de ton île")
                .toItemStack(),
                event -> {
                    if (!this.player.getIsland().isEmpty()) {
                        ((Player) event.getWhoClicked()).performCommand("bank");
                    } else {
                        event.getWhoClicked().sendMessage(ChatColor.RED + "Tu dois avoir une île pour accéder à la banque d'île !");
                    }
                });
    }

}
