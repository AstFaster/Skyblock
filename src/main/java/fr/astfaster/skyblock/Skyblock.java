package fr.astfaster.skyblock;

import com.mongodb.client.MongoDatabase;
import fr.astfaster.skyblock.configuration.SBConfiguration;
import fr.astfaster.skyblock.configuration.SBConfigurationManager;
import fr.astfaster.skyblock.mongodb.MongoDBConnection;
import fr.astfaster.skyblock.player.SBPlayerManager;
import fr.astfaster.skyblock.redis.RedisConnection;
import fr.astfaster.skyblock.util.inventory.SBInventoryManager;
import fr.astfaster.skyblock.util.item.SBItemManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Skyblock extends JavaPlugin {

    /** Configuration */
    private SBConfigurationManager configurationManager;
    private SBConfiguration configuration;

    /** MongoDB */
    private MongoDBConnection mongoConnection;
    private MongoDatabase skyblockDatabase;

    /** Redis */
    private RedisConnection redisConnection;

    /** Player */
    private SBPlayerManager playerManager;

    /** Util */
    private SBItemManager itemManager;
    private SBInventoryManager inventoryManager;

    public void onEnable() {
        this.configurationManager = new SBConfigurationManager(this, true);
        this.configuration = this.configurationManager.getConfiguration();

        this.mongoConnection = new MongoDBConnection(this.configuration.getMongoDBConfiguration().getUrl(), true);
        this.skyblockDatabase = this.mongoConnection.getMongoClient().getDatabase(this.configuration.getMongoDBConfiguration().getDatabase());

        this.redisConnection = new RedisConnection(this.configuration.getRedisConfiguration().getRedisIp(), this.configuration.getRedisConfiguration().getRedisPort(), this.configuration.getRedisConfiguration().getRedisPassword());

        this.playerManager = new SBPlayerManager(this);
    }

    @Override
    public void onDisable() {

    }

    public SBConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public SBConfiguration getConfiguration() {
        return this.configuration;
    }

    public MongoDBConnection getMongoConnection() {
        return this.mongoConnection;
    }

    public MongoDatabase getSkyblockDatabase() {
        return this.skyblockDatabase;
    }

    public RedisConnection getRedisConnection() {
        return this.redisConnection;
    }

    public SBPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public SBItemManager getItemManager() {
        return this.itemManager;
    }

    public SBInventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

}
