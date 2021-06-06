package fr.astfaster.skyblock.util.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SBInventoryManager implements Listener {

    public SBInventoryManager(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getInventory().getHolder() instanceof SBInventory) {
            final SBInventory inventory = (SBInventory) event.getInventory().getHolder();
            final int clickedSlot = event.getRawSlot();

            event.setCancelled(true);

            if (inventory.getClickConsumers().containsKey(clickedSlot)) {
                inventory.getClickConsumers().get(clickedSlot).accept(event);
            }
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof SBInventory) {
            final SBInventory inventory = (SBInventory) event.getInventory().getHolder();

            inventory.onOpen(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof SBInventory) {
            final SBInventory inventory = (SBInventory) event.getInventory().getHolder();

            inventory.onClose(event);
        }
    }

}
