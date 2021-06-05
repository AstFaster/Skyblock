package fr.astfaster.skyblock.util.item;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SBItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private final Map<Class<? extends Event>, Consumer<Supplier<? extends Event>>> events;

    public SBItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.itemStack.getItemMeta();
        this.events = new HashMap<>();
    }

    public SBItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public SBItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public SBItemBuilder(Material material, int amount, int data) {
        this(new ItemStack(material, amount, (byte) data));
    }

    public SBItemBuilder(Potion potion, int amount) {
        this(potion.toItemStack(amount));
    }

    public SBItemBuilder(Potion potion) {
        this(potion, 1);
    }

    public SBItemBuilder withName(String name) {
        this.itemMeta.setDisplayName(name);

        return this;
    }

    public SBItemBuilder withLore(List<String> lore) {
        this.itemMeta.setLore(lore);

        return this;
    }

    public SBItemBuilder withLore(String... lore) {
        return this.withLore(Arrays.asList(lore));
    }

    public SBItemBuilder withSkullOwner(UUID uuid) {
        final OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        final SkullMeta skullMeta = (SkullMeta) this.itemMeta;

        skullMeta.setOwner(player.getName());

        this.itemStack.setItemMeta(skullMeta);

        return this;
    }

    public SBItemBuilder withItemFlags(ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);

        return this;
    }

    public SBItemBuilder withAllItemFlags() {
        return this.withItemFlags(ItemFlag.values());
    }

    public SBItemBuilder withEnchant(Enchantment enchant, int level, boolean show) {
        this.itemMeta.addEnchant(enchant, level, show);

        return this;
    }

    public SBItemBuilder withEnchant(Enchantment enchant, int level) {
        return this.withEnchant(enchant, level, true);
    }

    public SBItemBuilder withEnchant(Enchantment enchant) {
        return this.withEnchant(enchant, 1, true);
    }

    public SBItemBuilder withHidingEnchantments() {
        return this.withItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public SBItemBuilder withInfiniteDurability() {
        this.itemStack.setDurability(Short.MAX_VALUE);

        return this;
    }

    public SBItemBuilder withColor(ItemColor color) {
        this.itemStack.setDurability((short) color.getData());

        return this;
    }

    public SBItemBuilder withLeatherArmorColor(Color color) {
        final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.itemMeta;

        leatherArmorMeta.setColor(color);

        this.itemStack.setItemMeta(leatherArmorMeta);

        return this;
    }

    public SBItemBuilder withEvent(Class<? extends Event> eventClass, Consumer<Supplier<? extends Event>> eventConsumer) {
        this.events.put(eventClass, eventConsumer);

        return this;
    }

    public ItemStack toItemStack() {
        this.itemStack.setItemMeta(this.itemMeta);

        return this.itemStack;
    }

    public SBItem build() {
        final SBItem sbItem = new SBItem(this.toItemStack());

        sbItem.setEvents(this.events);

        return sbItem;
    }

    public Map<Class<? extends Event>, Consumer<Supplier<? extends Event>>> getEvents() {
        return this.events;
    }

}
