package fr.astfaster.skyblock.command;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.island.boss.SBBoss;
import fr.astfaster.skyblock.island.boss.SBBossManager;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class FightCommand extends Command {

    private final Skyblock skyblock;

    public FightCommand(Skyblock skyblock) {
        super("fight", "Fight boss command", "/fight", new ArrayList<>());
        this.skyblock = skyblock;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());

            if (args.length == 1) {
                final SBBoss boss = this.getBoss(args[0]);

                if (boss != null) {
                    if (!sbPlayer.getIsland().isEmpty()) {
                        final SBIsland island = this.skyblock.getIslandManager().getIslandFromRedis(sbPlayer.getIsland());
                        final SBIslandMember senderMember = island.getIslandMemberById(sbPlayer.getUuid());

                        if (senderMember.getMemberType().equals(SBIslandMemberType.LEADER)) {
                            final SBBossManager bossManager = this.skyblock.getBossManager();

                            if (bossManager.canCreate()) {
                                bossManager.setCanCreate(false);
                                bossManager.setIsland(island);
                                bossManager.setBoss(boss);

                                this.sendInvites(island, boss);

                                bossManager.waitPlayers();
                            } else {
                                player.sendMessage(ChatColor.RED + "Une autre île a déjà engagé un combat !");
                            }

                        } else {
                            player.sendMessage(ChatColor.RED + "Tu dois être le chef de ton île pour lancer un combat !");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Tu dois avoir une île pour lancer un combat !");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Impossible de trouver un boss appelé '" + args[0] + "'");
                    player.sendMessage(ChatColor.RED + "Liste des boss disponibles : " + this.getBossList());
                }

            } else if (args.length > 1) {
                if (args[0].equalsIgnoreCase("invite")) {
                    if (args[1].equalsIgnoreCase("accept")) {
                        this.accept(player);
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "/fight <boss>");
            }
        }
        return true;
    }

    private void accept(Player player) {
        final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());

        if (!sbPlayer.getIsland().isEmpty()) {
            final SBIsland island = this.skyblock.getIslandManager().getIslandFromRedis(sbPlayer.getIsland());
            final SBBossManager bossManager = this.skyblock.getBossManager();

            if (bossManager.getIsland() != null) {
                if (bossManager.getIsland().getUuid().equals(island.getUuid())) {
                    if (!bossManager.isPlaying()) {
                        if (!bossManager.getPlayers().contains(player)) {
                            bossManager.getPlayers().add(player);
                            bossManager.getSbPlayers().add(sbPlayer);

                            for (Player p : bossManager.getPlayers()) {
                                if (!p.getUniqueId().equals(player.getUniqueId())) {
                                    p.sendMessage(ChatColor.GREEN + "'" + player.getDisplayName() + "' a rejoint le combat !");
                                } else {
                                    p.sendMessage(ChatColor.GREEN + "Tu as rejoint le combat contre le boss ! Merci d'attendre le temps que tout le monde rejoigne.");
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Tu as déjà rejoint le combat !");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Le combat a déjà commencé !");
                    }

                } else {
                    player.sendMessage(ChatColor.RED + "Tu n'as pas engagé de combat contre un boss !");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Il n'y a pas de combat en cours !");
            }
        }
    }

    private void sendInvites(SBIsland island, SBBoss boss) {
        for (SBIslandMember member : island.getMembers()) {
            final Player player = Bukkit.getPlayer(UUID.fromString(member.getUuid()));

            if (player.isOnline()) {
                final TextComponent message = new TextComponent(net.md_5.bungee.api.ChatColor.GOLD + "Un combat contre '"
                        + net.md_5.bungee.api.ChatColor.YELLOW + boss.getEntity().getName()
                        + net.md_5.bungee.api.ChatColor.GOLD + "' vient d'être engagé.\n"
                        + net.md_5.bungee.api.ChatColor.GOLD + "Rejoindre ?");

                final TextComponent yes = new TextComponent(" [Oui]");
                yes.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fight invite accept"));
                yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Accepter").create()));
                message.addExtra(yes);

                player.spigot().sendMessage(message);
            }
        }
    }

    private SBBoss getBoss(String input) {
        for (SBBoss boss : SBBoss.values()) {
            if (boss.name().equals(input.toUpperCase())) {
                return boss;
            }
        }
        return null;
    }

    private String getBossList() {
        StringBuilder result = new StringBuilder();
        for (SBBoss boss : SBBoss.values()) {
            result.append(boss.name()).append("|");
        }
        return result.substring(0, result.length() - 1);
    }

}
