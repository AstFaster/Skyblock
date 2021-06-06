package fr.astfaster.skyblock.island.inventory;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.island.member.SBIslandMember;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.util.References;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SBIslandMembersInventory extends SBInventory {

    private final SBIsland island;

    private final Skyblock skyblock;

    public SBIslandMembersInventory(Skyblock skyblock, Player owner) {
        super(owner, ChatColor.RED + "Membres de l'île", 4 * 9);
        this.skyblock = skyblock;

        final SBPlayer player = this.skyblock.getPlayerManager().getPlayerFromRedis(owner.getUniqueId());

        this.island = this.skyblock.getIslandManager().getIslandFromRedis(player.getIsland());

        this.addMembersItems();

        this.addGoBackItem();

        this.setFill(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
    }

    private void addMembersItems() {
        int i = 0;
        for (SBIslandMember member : this.island.getMembers()) {
            final UUID playerUuid = UUID.fromString(member.getUuid());

            this.setItem(i, new SBItemBuilder(Material.SKULL_ITEM, 1, 3)
                    .withName(ChatColor.YELLOW + Bukkit.getOfflinePlayer(playerUuid).getName())
                    .withLore(ChatColor.YELLOW + "Grade : " + ChatColor.GOLD + member.getMemberType().getName())
                    .withSkullOwner(playerUuid)
                    .toItemStack());

            i++;
        }
    }

    private void addGoBackItem() {
        this.setItem(27, new SBItemBuilder(Material.SKULL_ITEM, 1, 3)
                        .withName(ChatColor.GRAY + "Revenir en arrière")
                        .withCustomHead(References.GO_BACK_HEAD)
                        .toItemStack(),
                event -> new SBIslandInformationInventory(skyblock, (Player) event.getWhoClicked()).open());
    }

}
