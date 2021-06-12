package fr.astfaster.skyblock.command;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.island.inventory.SBIslandInventory;
import fr.astfaster.skyblock.island.member.SBIslandMember;
import fr.astfaster.skyblock.island.member.SBIslandMemberType;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.util.MoneyUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class IslandCommand extends Command {

    private final Skyblock skyblock;

    public IslandCommand(Skyblock skyblock) {
        super("is", "Island command", "/is <promote|kick|invite|disband>", Collections.singletonList("island"));
        this.skyblock = skyblock;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
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
                } else if (args[0].equalsIgnoreCase("invite:accept")) {
                    this.acceptInvite(player);
                } else if (args[0].equalsIgnoreCase("invite:deny")) {
                    this.denyInvite(player);
                } else if (args[0].equalsIgnoreCase("kick")) {
                    if (args.length > 1) {
                        this.kick(player, args[1]);
                    } else {
                        player.sendMessage(ChatColor.RED + "/is <kick> <player>");
                    }
                } else if (args[0].equalsIgnoreCase("money")) {
                    if (args.length > 1) {
                        this.money(player, args[1]);
                    } else {
                        player.sendMessage(ChatColor.RED + "/is <money> <amount>");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "/is <promote|invite|kick|money>");
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
                            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is invite:accept"));
                            yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Accepter").create()));
                            message.addExtra(yes);

                            final TextComponent no = new TextComponent(" [Non]");
                            no.setColor(net.md_5.bungee.api.ChatColor.RED);
                            no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is invite:deny"));
                            no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Refuser").create()));
                            message.addExtra(no);

                            this.skyblock.getPlayerManager().getIslandInvitations().put(targetSbPlayer.getUuid(), island.getUuid());

                            target.spigot().sendMessage(message);

                            player.sendMessage(ChatColor.GOLD + "La demande a été envoyé !");
                        }
                    }

                } else {
                    player.sendMessage(ChatColor.RED + "Ce joueur fait déjà partie d'une île !");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Tu n'as pas encore d'île !");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Impossible de trouver un joueur appelé '" + playerName + "' !");
        }
    }

    private void acceptInvite(Player player) {
        final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());
        final Map<String, String> islandInvitation = this.skyblock.getPlayerManager().getIslandInvitations();

        if (islandInvitation.containsKey(sbPlayer.getUuid())) {
            final SBIsland island = this.skyblock.getIslandManager().getIslandFromRedis(islandInvitation.get(sbPlayer.getUuid()));

            if (island.getMembers().size() < 25) {
                island.getMembers().add(new SBIslandMember(sbPlayer.getUuid(), SBIslandMemberType.NEW));
                sbPlayer.setIsland(island.getUuid());

                this.skyblock.getIslandManager().sendIslandToRedis(island);
                this.skyblock.getPlayerManager().sendPlayerToRedis(sbPlayer);

                islandInvitation.remove(sbPlayer.getUuid());

                for (SBIslandMember member : island.getMembers()) {
                    final Player p = Bukkit.getPlayer(UUID.fromString(member.getUuid()));

                    if (p.isOnline()) {
                        if (!p.getUniqueId().toString().equals(sbPlayer.getUuid())) {
                            p.sendMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.GOLD + " a rejoint votre île !");
                        } else {
                            p.sendMessage(ChatColor.GOLD + "Tu as rejoint l'île : " + ChatColor.YELLOW + island.getName());
                        }
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Cette île est déjà complète !");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Tu n'as reçu aucune invitation !");
        }
    }

    private void denyInvite(Player player) {
        final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());
        final Map<String, String> islandInvitation = this.skyblock.getPlayerManager().getIslandInvitations();

        if (islandInvitation.containsKey(sbPlayer.getUuid())) {
            final SBIsland island = this.skyblock.getIslandManager().getIslandFromRedis(islandInvitation.get(sbPlayer.getUuid()));

            islandInvitation.remove(sbPlayer.getUuid());

            for (SBIslandMember member : island.getMembers()) {
                final Player p = Bukkit.getPlayer(UUID.fromString(member.getUuid()));

                if (p.isOnline()) {
                    p.sendMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.GOLD + " a refusé de rejoindre votre île !");
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "Tu n'as reçu aucune invitation !");
        }
    }

    private void kick(Player sender, String targetName) {
        final Player target = Bukkit.getPlayer(targetName);

        if (target != null) {

            if (target.getUniqueId().equals(sender.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous auto-kick !");
                return;
            }

            final SBPlayer targetSbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(target.getUniqueId());
            final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(sender.getUniqueId());

            if (!sbPlayer.getIsland().isEmpty()) {
                final SBIsland island = this.skyblock.getIslandManager().getIslandFromRedis(sbPlayer.getIsland());

                for (SBIslandMember member : island.getMembers()) {
                    if (member.getUuid().equals(target.getUniqueId().toString())) {
                        final SBIslandMember senderMember = island.getIslandMemberById(sbPlayer.getUuid());

                        if (senderMember != null) {
                            if (!member.getMemberType().equals(SBIslandMemberType.LEADER)) {
                                if (senderMember.getMemberType().getId() < member.getMemberType().getId() && senderMember.getMemberType().getId() <= SBIslandMemberType.MEMBER.getId()) {
                                    target.closeInventory();

                                    this.skyblock.getIslandManager().removeIslandFromRedis(island);

                                    island.getMembers().remove(member);
                                    targetSbPlayer.setIsland("");

                                    this.skyblock.getPlayerManager().sendPlayerToRedis(targetSbPlayer);
                                    this.skyblock.getIslandManager().sendIslandToRedis(island);

                                    sender.sendMessage(ChatColor.GREEN + "'" + target.getDisplayName() + "' a été expulsé de votre île !");
                                    target.sendMessage(ChatColor.GREEN + "Tu as été expulsé de l'île : " + island.getName());
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Tu n'as pas les permissions requises !");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Ce joueur possède déjà le plus haut grade !");
                            }
                        }
                        return;
                    }
                }
                sender.sendMessage(ChatColor.RED + "'" + targetName + "' n'est pas un membre de ton île !");

            } else {
                sender.sendMessage(ChatColor.RED + "Tu n'as pas encore d'île !");
            }

        } else {
            sender.sendMessage(ChatColor.RED + "Impossible de trouver un joueur appelé '" + targetName + "' !");
        }
    }

    private void money(Player player, String amountString) {
        final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());

        if (!sbPlayer.getIsland().isEmpty()) {
            final SBIsland island = this.skyblock.getIslandManager().getIslandFromRedis(sbPlayer.getIsland());

            try {
                final double amount = MoneyUtils.roundMoney(amountString);

                if (sbPlayer.getMoney() >= amount) {
                    sbPlayer.setMoney(sbPlayer.getMoney() - amount);
                    island.setMoney(island.getMoney() + amount);

                    this.skyblock.getPlayerManager().sendPlayerToRedis(sbPlayer);
                    this.skyblock.getIslandManager().sendIslandToRedis(island);

                    player.sendMessage(ChatColor.GREEN + "Tu viens d'ajouter " + ChatColor.WHITE + amount + "$ " + ChatColor.GREEN + "à l'argent de ton île.");
                } else {
                    player.sendMessage(ChatColor.RED + "Tu n'as pas l'argent nécessaire.");
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED +  "'" + amountString + "' n'est pas un nombre valide !");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Tu n'as pas encore d'île !");
        }
    }

}
