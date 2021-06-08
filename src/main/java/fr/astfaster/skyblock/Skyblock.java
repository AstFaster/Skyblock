package fr.astfaster.skyblock;

import com.mongodb.client.MongoDatabase;
import fr.astfaster.skyblock.command.IslandCommand;
import fr.astfaster.skyblock.command.MoneyCommand;
import fr.astfaster.skyblock.command.PayCommand;
import fr.astfaster.skyblock.configuration.SBConfiguration;
import fr.astfaster.skyblock.configuration.SBConfigurationManager;
import fr.astfaster.skyblock.island.SBIslandManager;
import fr.astfaster.skyblock.listener.PlayerListener;
import fr.astfaster.skyblock.mongodb.MongoDBConnection;
import fr.astfaster.skyblock.player.SBPlayerManager;
import fr.astfaster.skyblock.redis.RedisConnection;
import fr.astfaster.skyblock.util.inventory.SBInventoryManager;
import fr.astfaster.skyblock.util.item.SBItemManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collections;

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

    /** Island */
    private SBIslandManager islandManager;

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

        this.islandManager = new SBIslandManager(this);

        this.itemManager = new SBItemManager(this);
        this.inventoryManager = new SBInventoryManager(this);

        this.islandManager.loadIslands();

        this.registerListeners();
        this.registerCommands();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void registerCommands() {
        CommandMap commandMap = null;
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);

            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (commandMap != null) {
            commandMap.register("is", new IslandCommand(this));
            commandMap.register("money", new MoneyCommand(this));
            commandMap.register("pay", new PayCommand(this));
        }
    }

    @Override
    public void onDisable() {
        this.playerManager.savePlayers();
        this.islandManager.saveIslands();
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

    public SBIslandManager getIslandManager() {
        return this.islandManager;
    }

    public SBItemManager getItemManager() {
        return this.itemManager;
    }

    public SBInventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

}
