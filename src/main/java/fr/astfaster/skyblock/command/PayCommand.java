package fr.astfaster.skyblock.command;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.player.SBPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PayCommand extends Command {

    private final Skyblock skyblock;

    public PayCommand(Skyblock skyblock) {
        super("pay", "Pay command", "/pay <player> <amount>", new ArrayList<>());
        this.skyblock = skyblock;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;

            if (args.length > 1) {
                final Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {
                    final SBPlayerManager playerManager = this.skyblock.getPlayerManager();
                    final SBPlayer sbPlayer = playerManager.getPlayerFromRedis(player.getUniqueId());
                    final SBPlayer targetSbPlayer = playerManager.getPlayerFromMongo(target.getUniqueId());
                    final DecimalFormat decimalFormat = new DecimalFormat("#.0");

                    decimalFormat.setMaximumFractionDigits(1);
                    decimalFormat.setMinimumFractionDigits(1);
                    decimalFormat.setDecimalSeparatorAlwaysShown(true);

                    final String amountString = decimalFormat.format(Double.parseDouble(args[1]));
                    final double amount = Double.parseDouble(amountString);

                    if (sbPlayer.getMoney() >= amount) {
                        sbPlayer.setMoney(sbPlayer.getMoney() - amount);
                        targetSbPlayer.setMoney(targetSbPlayer.getMoney() + amount);

                        playerManager.sendPlayerToRedis(sbPlayer);
                        playerManager.sendPlayerToRedis(targetSbPlayer);

                        player.sendMessage(ChatColor.WHITE + "" + amount + "$ " + ChatColor.GREEN + "ont été envoyé à " + ChatColor.WHITE + target.getDisplayName() + ChatColor.GREEN + ".");
                        target.sendMessage(ChatColor.GREEN + "Tu as reçu " + ChatColor.WHITE + amount + "$ " + ChatColor.GREEN + "de la part de " + ChatColor.WHITE + player.getDisplayName() + ChatColor.GREEN + ".");
                    } else {
                        player.sendMessage(ChatColor.RED + "Tu n'as pas l'argent nécessaire !");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Impossible de trouver un joueur appelé '" + args[1] + "' !");
                }
            } else {
                player.sendMessage(ChatColor.RED + "/pay <player> <amount>");
            }

        }
        return true;
    }
}
