package fr.astfaster.skyblock.command;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.island.bank.SBBankInventory;
import fr.astfaster.skyblock.island.bank.SBBankSerializer;
import fr.astfaster.skyblock.player.SBPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class BankCommand extends Command {

    private final Skyblock skyblock;

    public BankCommand(Skyblock skyblock) {
        super("bank", "Bank command", "/bank", new ArrayList<>());
        this.skyblock = skyblock;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final SBPlayer sbPlayer = this.skyblock.getPlayerManager().getPlayerFromRedis(player.getUniqueId());

            if (!sbPlayer.getIsland().isEmpty()) {
                final SBIsland island = this.skyblock.getIslandManager().getIslandFromRedis(sbPlayer.getIsland());
                final Inventory inventory = SBBankSerializer.stringToBank(island.getBank());

                if (inventory != null) {
                    if (!this.skyblock.getIslandManager().getIslandsBankOpen().contains(island.getUuid())) {
                        final SBBankInventory bank = SBBankInventory.buildFromInventory(this.skyblock, island, inventory);

                        player.openInventory(bank.getInventory());
                    } else {
                        player.sendMessage(ChatColor.RED + "Un joueur utilise déjà la banque d'île !");
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Tu dois avoir une île pour accéder à la banque d'île !");
            }
        }
        return true;
    }
}
