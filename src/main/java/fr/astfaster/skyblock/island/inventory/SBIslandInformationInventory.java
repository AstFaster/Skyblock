package fr.astfaster.skyblock.island.inventory;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.util.References;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SBIslandInformationInventory extends SBInventory {

    private final SBIsland island;

    private final Skyblock skyblock;

    public SBIslandInformationInventory(Skyblock skyblock, Player owner) {
        super(owner, ChatColor.RED + "Informations de l'île", 3 * 9);
        this.skyblock = skyblock;

        final SBPlayer player = this.skyblock.getPlayerManager().getPlayerFromRedis(owner.getUniqueId());

        this.island = this.skyblock.getIslandManager().getIslandFromRedis(player.getIsland());

        this.addNameItem();
        this.addDescriptionItem();
        this.addCreatedDateItem();
        this.addMoneyItem();
        this.addMembersItem();

        this.addGoBackItem();

        this.setFill(new SBItemBuilder(Material.STAINED_GLASS_PANE, 1, 15)
                .withName(" ")
                .toItemStack());
    }

    private void addNameItem() {
        this.setItem(11, new SBItemBuilder(Material.SIGN)
                .withName(ChatColor.YELLOW + "Nom")
                .withLore(ChatColor.GOLD + this.island.getName())
                .toItemStack());
    }

    private void addDescriptionItem() {
        this.setItem(12, new SBItemBuilder(Material.NAME_TAG)
                .withName(ChatColor.YELLOW + "Description")
                .withLore(ChatColor.GOLD + this.island.getDescription())
                .toItemStack());
    }

    private void addCreatedDateItem() {
        final String createdDate = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date(this.island.getCreatedDate()));

        this.setItem(13, new SBItemBuilder(Material.WATCH)
                .withName(ChatColor.YELLOW + "Date de création")
                .withLore(ChatColor.GOLD + createdDate)
                .toItemStack());
    }

    private void addMoneyItem() {
        this.setItem(14, new SBItemBuilder(Material.NAME_TAG)
                .withName(ChatColor.YELLOW + "Argent")
                .withLore(ChatColor.GOLD + String.valueOf(this.island.getMoney()))
                .toItemStack());
    }

    private void addMembersItem() {
        this.setItem(15, new SBItemBuilder(Material.DIAMOND_CHESTPLATE)
                .withName(ChatColor.YELLOW + "Membres")
                .withLore(ChatColor.GOLD + String.valueOf(this.island.getMembers().size()) + "/25")
                .toItemStack(),
                event -> new SBIslandMembersInventory(skyblock, (Player) event.getWhoClicked()).open());
    }

    private void addGoBackItem() {
        this.setItem(18, new SBItemBuilder(Material.SKULL_ITEM, 1, 3)
                        .withName(ChatColor.GRAY + "Revenir en arrière")
                        .withCustomHead(References.GO_BACK_HEAD)
                        .toItemStack(),
                event -> new SBIslandInventory(skyblock, (Player) event.getWhoClicked()).open());
    }

}
