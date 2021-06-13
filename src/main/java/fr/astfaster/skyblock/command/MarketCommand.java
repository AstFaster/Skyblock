package fr.astfaster.skyblock.command;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.market.SBMarketItem;
import fr.astfaster.skyblock.market.SBMarketManager;
import fr.astfaster.skyblock.market.inventory.SBConfirmSellInventory;
import fr.astfaster.skyblock.market.inventory.SBMarketInventory;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.player.SBPlayerManager;
import fr.astfaster.skyblock.util.MoneyUtils;
import fr.astfaster.skyblock.util.SerializerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.UUID;

public class MarketCommand extends Command {

    private final Skyblock skyblock;

    public MarketCommand(Skyblock skyblock) {
        super("market", "Market command", "/market", Collections.singletonList("hdv"));
        this.skyblock = skyblock;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;

            if (args.length == 0) {
                new SBMarketInventory(this.skyblock, player).open();
            } else if (args[0].equalsIgnoreCase("sell")) {
                if (args.length > 1 ) {
                    final ItemStack itemStack = player.getItemInHand();

                    if (!itemStack.getType().equals(Material.AIR)) {
                        try {
                            final String itemId = "item-" + UUID.randomUUID().toString().split("-")[0];
                            final double price = MoneyUtils.roundMoney(args[1]);
                            final SBMarketItem item = new SBMarketItem(itemId, SerializerUtils.itemStackToString(itemStack), price, player.getUniqueId().toString());
                            final int slot = player.getInventory().first(itemStack);

                            new SBConfirmSellInventory(this.skyblock, item, slot, player).open();
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "'" + args[1] + "' n'est pas un nombre valide !");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Tu dois avoir un ou des items dans la main !");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "/market sell <price>");
                }
            }

        }
        return true;
    }

}
