package fr.astfaster.skyblock.island.inventory;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.island.member.SBIslandMember;
import fr.astfaster.skyblock.island.member.SBIslandMemberType;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.player.SBPlayerManager;
import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.ItemColor;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class SBCreateIslandInventory extends SBInventory {

    private String islandName;

    private final Skyblock skyblock;

    public SBCreateIslandInventory(Skyblock skyblock, Player owner) {
        super(owner, "Créer son île", 3 * 9);
        this.skyblock = skyblock;

        this.setItem(11, new SBItemBuilder(Material.WOOL)
                .withName(ChatColor.GREEN + "Oui")
                .withAllItemFlags()
                .withColor(ItemColor.GREEN)
                .toItemStack(), this.yes()
        );

        this.setItem(15, new SBItemBuilder(Material.WOOL)
                .withName(ChatColor.RED + "Non")
                .withAllItemFlags()
                .withColor(ItemColor.RED)
                .toItemStack(), this.no()
        );
    }

    private Consumer<InventoryClickEvent> yes() {
        return event -> {
            final Player player = (Player) event.getWhoClicked();

            player.sendMessage(ChatColor.GOLD + "Veuillez rentrer le nom de votre île (" + ChatColor.RED + "cancel " + ChatColor.GOLD + "pour annuler).");

            player.closeInventory();

            skyblock.getServer().getPluginManager().registerEvents(new Listener() {

                @EventHandler
                public void onChat(AsyncPlayerChatEvent e) {
                    if (!e.getMessage().equals("cancel")) {
                        islandName = e.getMessage();

                        confirm(player);
                    } else {
                        player.sendMessage(ChatColor.GOLD + "Création de votre île annulée.");
                    }

                    e.setCancelled(true);

                    HandlerList.unregisterAll(this);
                }

            }, skyblock);
        };
    }

    private void confirm(Player player) {
        player.sendMessage(ChatColor.GOLD + "Vote île s'appellera: " + ChatColor.WHITE + this.islandName + ChatColor.GOLD + ". Voulez-vous continuer ?");

        final SBInventory confirmInventory = new SBInventory(player, "Confirmer la création de l'île", 3 * 9);

        confirmInventory.setItem(11, new SBItemBuilder(Material.WOOL)
                .withName(ChatColor.GREEN + "Oui")
                .withAllItemFlags()
                .withColor(ItemColor.GREEN)
                .toItemStack(), this.createIsland()
        );

        confirmInventory.setItem(15, new SBItemBuilder(Material.WOOL)
                .withName(ChatColor.RED + "Non")
                .withAllItemFlags()
                .withColor(ItemColor.RED)
                .toItemStack(), this.no()
        );

        confirmInventory.open();
    }

    private Consumer<InventoryClickEvent> createIsland() {
        return event -> {
            final Player player = (Player) event.getWhoClicked();

            player.closeInventory();

            final SBIsland island = new SBIsland("", this.islandName, "" , 0.0F, System.currentTimeMillis());
            final String islandId = "island-" + UUID.randomUUID().toString().split("-")[0];

            island.setUuid(islandId);
            island.getMembers().add(new SBIslandMember(player.getUniqueId().toString(), SBIslandMemberType.LEADER));

            this.skyblock.getIslandManager().createIsland(island);

            final SBPlayerManager playerManager = this.skyblock.getPlayerManager();
            final SBPlayer sbPlayer = playerManager.getPlayerFromRedis(player.getUniqueId());

            sbPlayer.setIsland(islandId);

            playerManager.sendPlayerToRedis(sbPlayer);

            player.sendMessage(ChatColor.GOLD + "Votre île a correctement été créée !");
        };
    }

    private Consumer<InventoryClickEvent> no() {
        return event -> {
            final Player player = (Player) event.getWhoClicked();

            player.sendMessage(ChatColor.GOLD + "Création de votre île annulée.");
            player.closeInventory();
        };
    }

}
