package fr.astfaster.skyblock.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class SerializerUtils {

    public static String inventoryToString(Inventory inventory) {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream data = new BukkitObjectOutputStream(outputStream);

            data.writeInt(inventory.getSize());
            data.writeUTF(inventory.getName());

            for (int i = 0; i < inventory.getSize(); i++) {
                data.writeObject(inventory.getItem(i));
            }

            data.close();

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Inventory stringToInventory(String string) {
        try {
            final byte[] bytes = Base64.getDecoder().decode(string);
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            final BukkitObjectInputStream data = new BukkitObjectInputStream(inputStream);
            final Inventory inventory = Bukkit.createInventory(null, data.readInt(), data.readUTF());

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) data.readObject());
            }

            data.close();

            return inventory;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String itemStackToString(ItemStack itemStack) {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream data = new BukkitObjectOutputStream(outputStream);

            data.writeObject(itemStack);
            data.close();

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ItemStack stringToItemStack(String string) {
        try {
            final byte[] bytes = Base64.getDecoder().decode(string);
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            final BukkitObjectInputStream data = new BukkitObjectInputStream(inputStream);
            final ItemStack itemStack = (ItemStack) data.readObject();

            data.close();

            return itemStack;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
