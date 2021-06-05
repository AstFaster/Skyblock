package fr.astfaster.skyblock.command;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.inventory.SBCreateIslandInventory;
import fr.astfaster.skyblock.player.SBPlayer;
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
                final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());

                if (!sbPlayer.getIsland().isEmpty()) {

                } else {
                    player.openInventory(new SBCreateIslandInventory(player).getInventory());
                }
            }
        }
        return true;
    }
}
