package fr.astfaster.skyblock.shop;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SBShopItem implements Cloneable {

    private ItemStack itemStack;
    private String name;
    private int slot;
    private double buyingPrice;
    private double sellingPrice;
    private boolean stackable;
    private Consumer<InventoryClickEvent> action;

    public SBShopItem(ItemStack itemStack, String name, int slot, double buyingPrice, double sellingPrice, boolean stackable, Consumer<InventoryClickEvent> action) {
        this.itemStack = itemStack;
        this.name = name;
        this.slot = slot;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.stackable = stackable;
        this.action = action;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public double getBuyingPrice() {
        return this.buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getSellingPrice() {
        return this.sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public boolean isStackable() {
        return this.stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public Consumer<InventoryClickEvent> getAction() {
        return this.action;
    }

    public void setAction(Consumer<InventoryClickEvent> action) {
        this.action = action;
    }

    public SBShopItem clone() {
        try {
            return (SBShopItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

}
