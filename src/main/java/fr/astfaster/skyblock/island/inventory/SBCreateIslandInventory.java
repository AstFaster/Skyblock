package fr.astfaster.skyblock.island.inventory;

import fr.astfaster.skyblock.util.inventory.SBInventory;
import fr.astfaster.skyblock.util.item.ItemColor;
import fr.astfaster.skyblock.util.item.SBItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class SBCreateIslandInventory extends SBInventory {

    public SBCreateIslandInventory(Player owner) {
        super(owner, "Créer son île", 3 *9);

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
        final Consumer<InventoryClickEvent> consumer = event -> {
            // TODO
        };
        return consumer;
    }

    private Consumer<InventoryClickEvent> no() {
        return event -> event.getWhoClicked().closeInventory();
    }

}
