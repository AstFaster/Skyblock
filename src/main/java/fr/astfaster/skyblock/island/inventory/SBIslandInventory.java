package fr.astfaster.skyblock.island.inventory;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SBIslandInventory extends SBInventory {

    private final SBPlayer player;

    private final Skyblock skyblock;

    public SBIslandInventory(Skyblock skyblock, Player owner) {
        super(owner, ChatColor.RED + "Menu de l'île", 3 * 9);
        this.skyblock = skyblock;
        this.player = this.skyblock.getPlayerManager().getPlayerFromRedis(owner.getUniqueId());

        if (player.getIsland().isEmpty()) {
            this.addCreateIslandItem();
        } else {
            this.addIslandInformationItem();
        }

        this.setFill(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
    }

    private void addCreateIslandItem() {
        this.setItem(4, new SBItemBuilder(Material.SAPLING).withName(ChatColor.YELLOW + "Créer son île").toItemStack(), event -> {
            final SBCreateIslandInventory createIslandInventory = new SBCreateIslandInventory(this.skyblock, (Player) event.getWhoClicked());
            createIslandInventory.open();
        });
    }

    private void addIslandInformationItem() {
        final String islandHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDUyOGVkNDU4MDI0MDBmNDY1YjVjNGUzYTZiN2E5ZjJiNmE1YjNkNDc4YjZmZDg0OTI1Y2M1ZDk4ODM5MWM3ZCJ9fX0=";

        this.setItem(4, new SBItemBuilder(Material.SKULL_ITEM, 1, 3)
                .withName(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Informations de l'île")
                .withCustomHead(islandHead)
                .toItemStack(),
                event -> new SBIslandInformationInventory(skyblock, (Player) event.getWhoClicked()).open());
    }

}
