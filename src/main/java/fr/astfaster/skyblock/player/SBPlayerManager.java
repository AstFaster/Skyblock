package fr.astfaster.skyblock.player;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.util.References;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import redis.clients.jedis.Jedis;

import java.util.*;

public class SBPlayerManager {

    private final Map<String, String> islandInvitations;
    private final List<String> players;

    private final MongoCollection<SBPlayer> playersCollection;

    private final Skyblock skyblock;

    public SBPlayerManager(Skyblock skyblock) {
        this.skyblock = skyblock;
        this.playersCollection = this.skyblock.getSkyblockDatabase().getCollection("players", SBPlayer.class);
        this.players = new ArrayList<>();
        this.islandInvitations = new HashMap<>();
    }

    /**
     * Manage
     */

    public void savePlayers() {
        for (String playerId : this.players) {
            final SBPlayer player = this.getPlayerFromRedis(UUID.fromString(playerId));

            if (this.getPlayerFromMongo(UUID.fromString(playerId)) != null) {
                this.updatePlayerInMongo(player);
            } else {
                this.sendPlayerToMongo(player);
            }

            this.removePlayerFromRedis(player);
        }

        this.players.clear();
    }

    /**
     * Handle player
     */

    public void handleLogin(Player player) {
        SBPlayer sbPlayer = this.getPlayerFromMongo(player.getUniqueId());

        if (sbPlayer == null) {
            sbPlayer = new SBPlayer(player.getUniqueId().toString(), player.getName(), "", 100.0F);

            this.giveStartingInventory(player);
        }

        this.sendPlayerToRedis(sbPlayer);

        this.players.add(sbPlayer.getUuid());
    }

    private void giveStartingInventory(Player player) {
        final PlayerInventory inventory = player.getInventory();

        inventory.setItem(0, new ItemStack(Material.STONE_SWORD));
        inventory.setItem(1, new ItemStack(Material.STONE_AXE));
        inventory.setItem(2, new ItemStack(Material.STONE_PICKAXE));
        inventory.setItem(4, new ItemStack(Material.COOKED_BEEF, 32));

        inventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
        inventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));
    }

    public void handleLogout(Player player) {
        final SBPlayer sbPlayer = this.getPlayerFromRedis(player.getUniqueId());

        this.removePlayerFromRedis(sbPlayer);

        if (this.getPlayerFromMongo(player.getUniqueId()) != null) {
            this.updatePlayerInMongo(sbPlayer);
        } else {
            this.sendPlayerToMongo(sbPlayer);
        }

        this.players.remove(sbPlayer.getUuid());
    }

    /**
     * Mongo
     */

    public void sendPlayerToMongo(SBPlayer player) {
        this.playersCollection.insertOne(player);
    }

    public void updatePlayerInMongo(SBPlayer player) {
        this.playersCollection.findOneAndReplace(new Document("_id", player.getUuid()), player, new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER));
    }

    public void removePlayerFromMongo(UUID uuid) {
        this.playersCollection.deleteOne(new Document("_id", uuid.toString()));
    }

    public SBPlayer getPlayerFromMongo(UUID uuid) {
        return this.playersCollection.find(Filters.eq("_id", uuid.toString())).first();
    }

    /**
     * Redis
     */

    public void sendPlayerToRedis(SBPlayer player) {
        final Jedis jedis = this.skyblock.getRedisConnection().getJedis();
        final String hash = References.NAME.toLowerCase() + ":players:" + player.getUuid() + ":";

        jedis.hmset(hash, this.getValuesFromPlayer(player));
    }

    public void removePlayerFromRedis(SBPlayer player) {
        final Jedis jedis = this.skyblock.getRedisConnection().getJedis();
        final String hash = References.NAME.toLowerCase() + ":players:" + player.getUuid() + ":";

        for (Map.Entry<String, String> entry : this.getValuesFromPlayer(player).entrySet()) jedis.hdel(hash, entry.getKey());
    }

    public SBPlayer getPlayerFromRedis(UUID uuid) {
        final Jedis jedis = this.skyblock.getRedisConnection().getJedis();
        final String hash = References.NAME.toLowerCase() + ":players:" + uuid + ":";

        return this.getPlayerFromValues(jedis.hgetAll(hash));
    }

    private Map<String, String> getValuesFromPlayer(SBPlayer player) {
        final Map<String, String> values = new HashMap<>();

        values.put("uuid", player.getUuid());
        values.put("name", player.getName());
        values.put("island", player.getIsland());
        values.put("money", String.valueOf(player.getMoney()));

        return values;
    }

    private SBPlayer getPlayerFromValues(Map<String, String> values) {
        return new SBPlayer(values.get("uuid"), values.get("name"), values.get("island"), Double.parseDouble(values.get("money")));
    }

    public Map<String, String> getIslandInvitations() {
        return this.islandInvitations;
    }
}
