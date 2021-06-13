package fr.astfaster.skyblock.command;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.shop.inventory.SBShopChooseCategoryInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ShopCommand extends Command {

    private final Skyblock skyblock;

    public ShopCommand(Skyblock skyblock) {
        super("shop", "Shop command", "/shop", new ArrayList<>());
        this.skyblock = skyblock;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;

            new SBShopChooseCategoryInventory(this.skyblock, player).open();
        }
        return true;
    }

}
