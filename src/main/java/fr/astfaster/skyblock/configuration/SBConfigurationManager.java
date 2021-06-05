package fr.astfaster.skyblock.configuration;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.configuration.neested.MongoDBConfiguration;
import fr.astfaster.skyblock.configuration.neested.RedisConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class SBConfigurationManager {

    private SBConfiguration configuration;

    private final FileConfiguration fileConfiguration;

    private final Skyblock skyblock;

    public SBConfigurationManager(Skyblock skyblock, boolean load) {
        this.skyblock = skyblock;
        this.fileConfiguration = this.skyblock.getConfig();

        if (load) this.loadConfiguration();
    }

    public void loadConfiguration() {
        this.createConfiguration();

        final String mongoDBPath = "mongodb";
        final String redisPath = "redis";

        final ConfigurationSection mongoDBSection = this.fileConfiguration.getConfigurationSection(mongoDBPath);
        final MongoDBConfiguration mongoDBConfiguration = new MongoDBConfiguration(mongoDBSection.getString("url"), mongoDBSection.getString("database"));

        final ConfigurationSection redisSection = this.fileConfiguration.getConfigurationSection(redisPath);
        final RedisConfiguration redisConfiguration = new RedisConfiguration(redisSection.getString("ip"), redisSection.getInt("port"), redisSection.getString("password"));

        this.configuration = new SBConfiguration(mongoDBConfiguration, redisConfiguration);
    }

    private void createConfiguration() {
        if (!new File(this.skyblock.getDataFolder(), "config.yml").exists()) {
            final String mongoDBPath = "mongodb";
            final String redisPath = "redis";

            this.fileConfiguration.createSection(mongoDBPath);
            this.fileConfiguration.createSection(redisPath);

            final ConfigurationSection mongoDBSection = this.fileConfiguration.getConfigurationSection(mongoDBPath);

            mongoDBSection.set("url", "mongodb://localhost:27017");
            mongoDBSection.set("database", "skyblock");

            final ConfigurationSection redisSection = this.fileConfiguration.getConfigurationSection(redisPath);

            redisSection.set("ip", "127.0.0.1");
            redisSection.set("port", 6379);
            redisSection.set("password", "");

            this.skyblock.saveConfig();
        }
    }

    public SBConfiguration getConfiguration() {
        return this.configuration;
    }
}
