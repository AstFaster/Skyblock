package fr.astfaster.skyblock.island.bank;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class SBBankSerializer {

    public static String bankToString(Inventory bank) {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream data = new BukkitObjectOutputStream(outputStream);

            data.writeInt(bank.getSize());
            data.writeUTF(bank.getName());

            for (int i = 0; i < bank.getSize(); i++) {
                data.writeObject(bank.getItem(i));
            }

            data.close();

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Inventory stringToBank(String bankData) {
        try {
            final byte[] bytes = Base64.getDecoder().decode(bankData);
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

}
