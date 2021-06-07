package fr.astfaster.skyblock.command;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.player.SBPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MoneyCommand extends Command {

    private final Skyblock skyblock;

    public MoneyCommand(Skyblock skyblock) {
        super("money", "Money command", "/money", new ArrayList<>());
        this.skyblock = skyblock;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());
            final DecimalFormat decimalFormat = new DecimalFormat("#.0");

            decimalFormat.setMaximumFractionDigits(1);
            decimalFormat.setMinimumFractionDigits(1);
            decimalFormat.setDecimalSeparatorAlwaysShown(true);

            final String amount = decimalFormat.format(sbPlayer.getMoney());

            player.sendMessage(ChatColor.GOLD + "Solde : " + ChatColor.YELLOW + amount + "$" + ChatColor.GOLD + ".");
        }
        return true;
    }
}
