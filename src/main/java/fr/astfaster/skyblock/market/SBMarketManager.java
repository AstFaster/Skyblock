package fr.astfaster.skyblock.market;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.market.inventory.SBMarketInventory;
import fr.astfaster.skyblock.player.SBPlayer;
import fr.astfaster.skyblock.player.SBPlayerManager;
import fr.astfaster.skyblock.util.References;
import fr.astfaster.skyblock.util.SerializerUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import redis.clients.jedis.Jedis;

import java.util.*;

public class SBMarketManager {

    private final Map<Player, SBMarketInventory> marketsOpen;

    private final List<String> items;

    private final MongoCollection<SBMarketItem> marketCollection;

    private final Skyblock skyblock;

    public SBMarketManager(Skyblock skyblock) {
        this.skyblock = skyblock;
        this.marketCollection = this.skyblock.getSkyblockDatabase().getCollection("market", SBMarketItem.class);
        this.items = new ArrayList<>();
        this.marketsOpen = new HashMap<>();
    }

    /**
     * Manage
     */

    public void loadItems() {
        final FindIterable<SBMarketItem> items = this.marketCollection.find();

        for (SBMarketItem item : items) {
            this.items.add(item.getUuid());
            this.sendItemToRedis(item);
        }
    }

    public void saveItems() {
        for (String itemId : this.items) {
            final SBMarketItem item = this.getItemFromRedis(itemId);

            this.updateItemInMongo(item);
            this.removeItemFromRedis(item);
        }

        this.items.clear();
    }

    public void buyItem(Player player, SBMarketItem item) {
        final SBPlayerManager playerManager = this.skyblock.getPlayerManager();
        final SBPlayer sbPlayer = playerManager.getPlayerFromRedis(player.getUniqueId());
        final ItemStack itemStack = SerializerUtils.stringToItemStack(item.getItemStack());
        final UUID sellerUuid = UUID.fromString(item.getOwnerUuid());
        final OfflinePlayer seller = Bukkit.getOfflinePlayer(sellerUuid);

        if (sbPlayer.getMoney() >= item.getBuyingPrice()) {
            if (itemStack != null) {
                if (!this.hasInventoryFull(player)) {
                    final int amount = itemStack.getAmount();

                    sbPlayer.setMoney(sbPlayer.getMoney() - item.getBuyingPrice());

                    this.items.remove(item.getUuid());

                    player.getInventory().addItem(itemStack);

                    playerManager.sendPlayerToRedis(sbPlayer);

                    if (seller.isOnline()) {
                        final SBPlayer sellerSbPlayer = playerManager.getPlayerFromRedis(sellerUuid);

                        sellerSbPlayer.setMoney(sellerSbPlayer.getMoney() + item.getBuyingPrice());

                        playerManager.sendPlayerToRedis(sellerSbPlayer);

                        seller.getPlayer().sendMessage(ChatColor.RED + player.getName() +
                                ChatColor.GOLD + " vient d'acheter " +
                                ChatColor.RED + itemStack.getType().name() +
                                ChatColor.GRAY + " x" + amount +
                                ChatColor.GOLD + " pour " +
                                ChatColor.RED + item.getBuyingPrice() + "$" +
                                ChatColor.GOLD + "."
                        );
                    } else {
                        final SBPlayer sellerSbPlayer = playerManager.getPlayerFromMongo(sellerUuid);

                        sellerSbPlayer.setMoney(sellerSbPlayer.getMoney() + item.getBuyingPrice());

                        playerManager.updatePlayerInMongo(sellerSbPlayer);
                    }

                    this.removeItemFromRedis(item);
                    this.removeItemFromMongo(item.getUuid());

                    player.sendMessage(ChatColor.GOLD + "Vous venez d'acheter " +
                            ChatColor.RED + itemStack.getType().name() +
                            ChatColor.GRAY + " x" + amount +
                            ChatColor.GOLD + " pour " +
                            ChatColor.RED + item.getBuyingPrice() + "$" +
                            ChatColor.GOLD + "."
                    );

                    for (Player p : this.marketsOpen.keySet()) {
                        this.marketsOpen.get(p).update();
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Tu n'as pas assez de place dans ton inventaire !");
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "Tu n'as pas l'argent nécessaire !");
        }
    }

    public void sellItem(Player player, SBMarketItem item, int slot, double sellPrice) {
        final SBPlayerManager playerManager = this.skyblock.getPlayerManager();
        final SBPlayer sbPlayer = playerManager.getPlayerFromRedis(player.getUniqueId());
        final ItemStack itemStack = SerializerUtils.stringToItemStack(item.getItemStack());

        if (itemStack != null) {
            if (sbPlayer.getMoney() >= sellPrice) {
                this.sendItemToRedis(item);
                this.sendItemToMongo(item);

                this.items.add(item.getUuid());

                player.getInventory().setItem(slot, null);

                sbPlayer.setMoney(sbPlayer.getMoney() - sellPrice);

                playerManager.sendPlayerToRedis(sbPlayer);

                player.sendMessage(ChatColor.GOLD + "Vous avez correctement mis en vente " +
                        ChatColor.RED + itemStack.getType().name() +
                        ChatColor.GRAY + " x" + itemStack.getAmount() +
                        ChatColor.GOLD + " pour " +
                        ChatColor.RED + item.getBuyingPrice() + "$" +
                        ChatColor.GOLD + "."
                );

                for (Player p : this.marketsOpen.keySet()) {
                    this.marketsOpen.get(p).update();
                }
            } else {
                player.sendMessage(ChatColor.RED + "Tu n'as pas l'argent nécessaire !");
            }
        }
    }

    public SBMarketItem getItemByStack(ItemStack itemStack) {
        for (String itemId : this.items) {
            final SBMarketItem item = this.getItemFromRedis(itemId);
            final ItemStack stack = SerializerUtils.stringToItemStack(item.getItemStack());

            if (stack != null) {
                if (itemStack.getType().equals(stack.getType())) {
                    if (!itemStack.getItemMeta().hasDisplayName() && !stack.getItemMeta().hasDisplayName()) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    private boolean hasInventoryFull(Player player) {
        final Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Mongo
     */

    public void sendItemToMongo(SBMarketItem item) {
        this.marketCollection.insertOne(item);
    }

    public void updateItemInMongo(SBMarketItem item) {
        this.marketCollection.findOneAndReplace(new Document("_id", item.getUuid()), item, new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER));
    }

    public void removeItemFromMongo(String uuid) {
        this.marketCollection.deleteOne(new Document("_id", uuid));
    }

    public SBMarketItem getItemFromMongo(String uuid) {
        return this.marketCollection.find(Filters.eq("_id", uuid)).first();
    }

    /**
     * Redis
     */

    public void sendItemToRedis(SBMarketItem item) {
        final Jedis jedis = this.skyblock.getRedisConnection().getJedis();
        final String hash = References.NAME.toLowerCase() + ":market:" + item.getUuid() + ":";

        jedis.hmset(hash, this.getValuesFromItem(item));
    }

    public void removeItemFromRedis(SBMarketItem item) {
        final Jedis jedis = this.skyblock.getRedisConnection().getJedis();
        final String hash = References.NAME.toLowerCase() + ":market:" + item.getUuid() + ":";

        for (Map.Entry<String, String> entry : this.getValuesFromItem(item).entrySet()) jedis.hdel(hash, entry.getKey());
    }

    public SBMarketItem getItemFromRedis(String uuid) {
        final Jedis jedis = this.skyblock.getRedisConnection().getJedis();
        final String hash = References.NAME.toLowerCase() + ":market:" + uuid + ":";

        return this.getItemFromValues(jedis.hgetAll(hash));
    }

    private Map<String, String> getValuesFromItem(SBMarketItem item) {
        final Map<String, String> values = new HashMap<>();

        values.put("uuid", item.getUuid());
        values.put("item_stack", item.getItemStack());
        values.put("buying_price", String.valueOf(item.getBuyingPrice()));
        values.put("owner_uuid", item.getOwnerUuid());

        return values;
    }

    private SBMarketItem getItemFromValues(Map<String, String> values) {
        final SBMarketItem item = new SBMarketItem();

        item.setUuid(values.get("uuid"));
        item.setItemStack(values.get("item_stack"));
        item.setBuyingPrice(Double.parseDouble(values.get("buying_price")));
        item.setOwnerUuid(values.get("owner_uuid"));

        return item;
    }

    public List<String> getItems() {
        return this.items;
    }

    public Map<Player, SBMarketInventory> getMarketsOpen() {
        return this.marketsOpen;
    }

    public double getPercentage() {
        return 0.04D;
    }

}
