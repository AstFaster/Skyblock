package fr.astfaster.skyblock.util.item;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SBItem {

    private Map<Class<? extends Event>, Consumer<Supplier<? extends Event>>> events;

    private final ItemStack itemStack;

    public SBItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.events = new HashMap<>();
    }

    public void register(SBItemManager itemManager) {
        itemManager.registerItemEvents(this);
    }

    public Map<Class<? extends Event>, Consumer<Supplier<? extends Event>>> getEvents() {
        return this.events;
    }

    public void setEvents(Map<Class<? extends Event>, Consumer<Supplier<? extends Event>>> events) {
        this.events = events;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}
