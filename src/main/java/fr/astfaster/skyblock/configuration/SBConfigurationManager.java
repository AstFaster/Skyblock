package fr.astfaster.skyblock.configuration;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.configuration.nested.BossConfiguration;
import fr.astfaster.skyblock.configuration.nested.MongoDBConfiguration;
import fr.astfaster.skyblock.configuration.nested.RedisConfiguration;
import fr.astfaster.skyblock.configuration.nested.SpawnConfiguration;
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
        final String bossPath = "boss";
        final String spawnPath = "spawn";

        final ConfigurationSection mongoDBSection = this.fileConfiguration.getConfigurationSection(mongoDBPath);
        final MongoDBConfiguration mongoDBConfiguration = new MongoDBConfiguration(mongoDBSection.getString("url"), mongoDBSection.getString("database"));

        final ConfigurationSection redisSection = this.fileConfiguration.getConfigurationSection(redisPath);
        final RedisConfiguration redisConfiguration = new RedisConfiguration(redisSection.getString("ip"), redisSection.getInt("port"), redisSection.getString("password"));

        final ConfigurationSection bossSection = this.fileConfiguration.getConfigurationSection(bossPath);
        final BossConfiguration bossConfiguration = new BossConfiguration(bossSection.getDouble("arenaX"), bossSection.getDouble("arenaY"), bossSection.getDouble("arenaZ"));

        final ConfigurationSection spawnSection = this.fileConfiguration.getConfigurationSection(spawnPath);
        final SpawnConfiguration spawnConfiguration = new SpawnConfiguration(spawnSection.getDouble("x"), spawnSection.getDouble("y"), spawnSection.getDouble("z"));

        this.configuration = new SBConfiguration(mongoDBConfiguration, redisConfiguration, bossConfiguration, spawnConfiguration);
    }

    private void createConfiguration() {
        if (!new File(this.skyblock.getDataFolder(), "config.yml").exists()) {
            final String mongoDBPath = "mongodb";
            final String redisPath = "redis";
            final String bossPath = "boss";
            final String spawnPath = "spawn";

            this.fileConfiguration.createSection(mongoDBPath);
            this.fileConfiguration.createSection(redisPath);
            this.fileConfiguration.createSection(bossPath);
            this.fileConfiguration.createSection(spawnPath);

            final ConfigurationSection mongoDBSection = this.fileConfiguration.getConfigurationSection(mongoDBPath);

            mongoDBSection.set("url", "mongodb://localhost:27017");
            mongoDBSection.set("database", "skyblock");

            final ConfigurationSection redisSection = this.fileConfiguration.getConfigurationSection(redisPath);

            redisSection.set("ip", "127.0.0.1");
            redisSection.set("port", 6379);
            redisSection.set("password", "");

            final ConfigurationSection bossSection = this.fileConfiguration.getConfigurationSection(bossPath);

            bossSection.set("arenaX", 0.0D);
            bossSection.set("arenaY", 0.0D);
            bossSection.set("arenaZ", 0.0D);

            final ConfigurationSection spawnSection = this.fileConfiguration.getConfigurationSection(spawnPath);

            spawnSection.set("x", 0.0D);
            spawnSection.set("y", 0.0D);
            spawnSection.set("z", 0.0D);

            this.skyblock.saveConfig();
        }
    }

    public SBConfiguration getConfiguration() {
        return this.configuration;
    }
}
