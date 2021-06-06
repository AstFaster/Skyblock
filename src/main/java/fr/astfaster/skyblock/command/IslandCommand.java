package fr.astfaster.skyblock.command;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.island.inventory.SBIslandInventory;
import fr.astfaster.skyblock.island.member.SBIslandMember;
import fr.astfaster.skyblock.island.member.SBIslandMemberType;
import fr.astfaster.skyblock.player.SBPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class IslandCommand extends Command {

    private final Skyblock skyblock;

    public IslandCommand(Skyblock skyblock, String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
        this.skyblock = skyblock;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (label.equalsIgnoreCase("is")) {
            if (sender instanceof Player) {
                final Player player = (Player) sender;

                if (args.length == 0) {
                    final SBIslandInventory islandInventory = new SBIslandInventory(this.skyblock, player);

                    islandInventory.open();
                } else {
                    if (args[0].equalsIgnoreCase("promote")) {
                        if (args.length > 1) {
                            this.promote(player, args[1]);
                        } else {
                            player.sendMessage(ChatColor.RED + "/is <promote> <player>");
                        }
                    } else if (args[0].equalsIgnoreCase("invite")) {
                        if (args.length > 1) {
                            this.invite(player, args[1]);
                        } else {
                            player.sendMessage(ChatColor.RED + "/is <invite> <player>");
                        }
                    }
                }
            }
        }
        return true;
    }

    private void promote(Player player, String playerName) {
        final Player target = Bukkit.getPlayer(playerName);

        if (target != null) {

            if (target.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous auto-promote !");
                return;
            }

            final SBPlayer targetSbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(target.getUniqueId());
            final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());

            if (!sbPlayer.getIsland().isEmpty()) {
                final SBIsland island = this.skyblock.getIslandManager().getIslandFromRedis(sbPlayer.getIsland());

                for (SBIslandMember member : island.getMembers()) {
                    if (member.getUuid().equals(target.getUniqueId().toString())) {
                        final SBIslandMember senderMember = island.getIslandMemberById(sbPlayer.getUuid());

                        if (senderMember != null) {
                            if (!member.getMemberType().equals(SBIslandMemberType.LEADER)) {
                                if (senderMember.getMemberType().getId() < member.getMemberType().getId()) {
                                    member.setMemberType(SBIslandMemberType.getMemberTypeById(member.getMemberType().getId() - 1));

                                    this.skyblock.getPlayerManager().sendPlayerToRedis(targetSbPlayer);
                                    this.skyblock.getIslandManager().sendIslandToRedis(island);

                                    player.sendMessage(ChatColor.GREEN + "'" + target.getDisplayName() + "' a été promu : " + ChatColor.WHITE + member.getMemberType().getName());
                                } else {
                                    player.sendMessage(ChatColor.RED + "Tu n'as pas les permissions requises !");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "Ce joueur possède déjà le plus haut grade !");
                            }
                        }
                        return;
                    }
                }
                player.sendMessage(ChatColor.RED + "'" + playerName + "' n'est pas un membre de ton île !");

            } else {
                player.sendMessage(ChatColor.RED + "Tu n'as pas encore d'île !");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Impossible de trouver un joueur appelé '" + playerName + "' !");
        }

    }

    private void invite(Player player, String playerName) {
        final Player target = Bukkit.getPlayer(playerName);

        if (target != null) {

            if (target.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous inviter vous même !");
                return;
            }

            final SBPlayer targetSbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(target.getUniqueId());
            final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());

            if (!sbPlayer.getIsland().isEmpty()) {
                if (targetSbPlayer.getIsland().isEmpty()) {
                    final SBIsland island = this.skyblock.getIslandManager().getIslandFromRedis(sbPlayer.getIsland());
                    final SBIslandMember senderMember = island.getIslandMemberById(sbPlayer.getUuid());

                    if (senderMember != null) {
                        if (senderMember.getMemberType().getId() <= SBIslandMemberType.DEPUTY.getId()) {
                            final TextComponent message = new TextComponent(net.md_5.bungee.api.ChatColor.YELLOW + player.getDisplayName()
                                    + net.md_5.bungee.api.ChatColor.GOLD + " vous invite sur l'île : "
                                    + net.md_5.bungee.api.ChatColor.YELLOW + island.getName()
                                    + net.md_5.bungee.api.ChatColor.GOLD + ".\n" + ChatColor.GOLD + "Rejoindre ?");

                            final TextComponent yes = new TextComponent(" [Oui]");
                            yes.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is invite accept"));
                            yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Accepter").create()));
                            message.addExtra(yes);

                            final TextComponent no = new TextComponent(" [Non]");
                            no.setColor(net.md_5.bungee.api.ChatColor.RED);
                            no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is invite deny"));
                            no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Refuser").create()));
                            message.addExtra(no);

                            target.spigot().sendMessage(message);
                        }
                    }

                } else {
                    player.sendMessage("Ce joueur fait déjà partie d'une île !");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Tu n'as pas encore d'île !");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Impossible de trouver un joueur appelé '" + playerName + "' !");
        }
    }

}
