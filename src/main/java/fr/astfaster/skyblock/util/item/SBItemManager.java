package fr.astfaster.skyblock.util.item;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SBItemManager {

    private final List<Listener> listeners;
    private final List<SBItem> items;

    private final JavaPlugin plugin;

    public SBItemManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.listeners = new ArrayList<>();
        this.items = new ArrayList<>();

        this.addListeners();
        this.registerListeners();
    }

    private void addListeners() {
        this.listeners.add(new Listener() {
            @EventHandler
            public void onInteract(PlayerInteractEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Player) {
                    execute(event);
                }
                event.setCancelled(true);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onDrop(PlayerDropItemEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemBreak(PlayerItemBreakEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemConsume(PlayerItemConsumeEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemDamage(PlayerItemDamageEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemPickup(PlayerPickupItemEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemEnchant(EnchantItemEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onItemDespawn(ItemDespawnEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onFurnaceBurn(FurnaceBurnEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onFurnaceSmelt(FurnaceSmeltEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onPlayerFish(PlayerFishEvent event) {
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onPotionSplash(PotionSplashEvent event){
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onPrepareItemEnchant(PrepareItemEnchantEvent event){
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onBlockPlace(BlockPlaceEvent event){
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onBlockBreak(BlockBreakEvent event){
                execute(event);
            }
        });
        this.listeners.add(new Listener() {
            @EventHandler
            public void onBlockDispense(BlockDispenseEvent event) {
                execute(event);
            }
        });
    }

    private void execute(Event event) {
        for (SBItem item :this.items) {
            if (item != null) {
                for (Map.Entry<Class<? extends Event>, Consumer<Supplier<? extends Event>>> entry : item.getEvents().entrySet()) {
                    if (entry.getKey() == event.getClass()) {
                        entry.getValue().accept(() -> event);
                    }
                }
            }
        }
    }

    private void registerListeners() {
        this.listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, this.plugin));
    }

    public void registerItemEvents(SBItem item) {
        this.items.add(item);
    }

    public List<SBItem> getItems() {
        return this.items;
    }
}
