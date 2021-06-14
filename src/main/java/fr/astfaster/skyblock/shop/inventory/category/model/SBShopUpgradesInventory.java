package fr.astfaster.skyblock.shop.inventory.category.model;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.island.SBIslandManager;
import fr.astfaster.skyblock.island.bank.SBBankUpgrade;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.player.SBPlayerManager;
import fr.astfaster.skyblock.shop.SBShopCategory;
import fr.astfaster.skyblock.shop.SBShopItem;
import fr.astfaster.skyblock.shop.SBShopManager;
import fr.astfaster.skyblock.shop.inventory.category.SBShopCategoryInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SBShopUpgradesInventory extends SBShopCategoryInventory {

    public SBShopUpgradesInventory(Skyblock skyblock, Player owner) {
        super(skyblock, owner, SBShopCategory.UPGRADES);

        this.addItems();
    }

    @Override
    protected void addItems() {
        final SBShopManager shopManager = this.skyblock.getShopManager();

        shopManager.addItem(this.category, this.addTier1BankUpgradeItem());
        shopManager.addItem(this.category, this.addTier2BankUpgradeItem());
        shopManager.addItem(this.category, this.addTier3BankUpgradeItem());

        super.addItems();
    }

    private SBShopItem addTier1BankUpgradeItem() {
        final ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
        final SBShopItem item = new SBShopItem(itemStack, SBBankUpgrade.TIER_1.getDisplayName() + " Bank Upgrade", 0, 2000.0D, 0.0D, false, null);

        item.setAction(event -> this.addBankUpgradeOnClick(event, SBBankUpgrade.TIER_1, item));

        return item;
    }

    private SBShopItem addTier2BankUpgradeItem() {
        final ItemStack itemStack = new ItemStack(Material.GOLD_INGOT, 2);
        final SBShopItem item = new SBShopItem(itemStack, SBBankUpgrade.TIER_2.getDisplayName() + " Bank Upgrade", 1, 5000.0d, 0.0D, false, null);

        item.setAction(event -> this.addBankUpgradeOnClick(event, SBBankUpgrade.TIER_2, item));

        return item;
    }

    private SBShopItem addTier3BankUpgradeItem() {
        final ItemStack itemStack = new ItemStack(Material.GOLD_INGOT, 3);
        final SBShopItem item = new SBShopItem(itemStack, SBBankUpgrade.TIER_3.getDisplayName() + " Bank Upgrade", 2, 10000.0d, 0.0D, false, null);

        item.setAction(event -> this.addBankUpgradeOnClick(event, SBBankUpgrade.TIER_3, item));

        return item;
    }

    private void addBankUpgradeOnClick(InventoryClickEvent event, SBBankUpgrade upgrade, SBShopItem item) {
        final Player player = (Player) event.getWhoClicked();
        final SBPlayerManager playerManager = this.skyblock.getPlayerManager();
        final SBPlayer sbPlayer = playerManager.getPlayerFromRedis(player.getUniqueId());

        if (!sbPlayer.getIsland().isEmpty()) {
            final SBIslandManager islandManager = this.skyblock.getIslandManager();
            final SBIsland island = islandManager.getIslandFromRedis(sbPlayer.getIsland());

            if (island.getBankUpgrade().getId() < upgrade.getId()) {
                if (island.getBankUpgrade().getId() == upgrade.getId() - 1) {
                    if (sbPlayer.getMoney() >= item.getBuyingPrice()) {
                        island.setBankUpgrade(upgrade);
                        sbPlayer.setMoney(sbPlayer.getMoney() - item.getBuyingPrice());

                        if (islandManager.getIslandsBankOpen().containsKey(island.getUuid())) {
                            islandManager.getIslandsBankOpen().get(island.getUuid()).closeInventory();
                        }

                        islandManager.sendIslandToRedis(island);
                        playerManager.sendPlayerToRedis(sbPlayer);

                        player.sendMessage(ChatColor.GOLD + "Achat effectué avec succès !");
                    } else {
                        player.sendMessage(ChatColor.RED + "Tu n'as pas l'argent nécessaire !");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Tu dois d'abord posséder l'amélioration de tier inférieur !");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Ton île possède déjà cette amélioration !");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Tu dois d'abord avoir accès à la banque avant de pouvoir acheter cette amélioration !");
        }
    }

}
