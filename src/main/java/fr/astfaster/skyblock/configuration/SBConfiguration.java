package fr.astfaster.skyblock.configuration;

import fr.astfaster.skyblock.configuration.nested.BossConfiguration;
import fr.astfaster.skyblock.configuration.nested.MongoDBConfiguration;
import fr.astfaster.skyblock.configuration.nested.RedisConfiguration;
import fr.astfaster.skyblock.configuration.nested.SpawnConfiguration;

public class SBConfiguration {

    private MongoDBConfiguration mongoDBConfiguration;
    private RedisConfiguration redisConfiguration;
    private BossConfiguration bossConfiguration;
    private SpawnConfiguration spawnConfiguration;

    public SBConfiguration(MongoDBConfiguration mongoDBConfiguration, RedisConfiguration redisConfiguration, BossConfiguration bossConfiguration, SpawnConfiguration spawnConfiguration) {
        this.mongoDBConfiguration = mongoDBConfiguration;
        this.redisConfiguration = redisConfiguration;
        this.bossConfiguration = bossConfiguration;
        this.spawnConfiguration = spawnConfiguration;
    }

    public MongoDBConfiguration getMongoDBConfiguration() {
        return this.mongoDBConfiguration;
    }

    public void setMongoDBConfiguration(MongoDBConfiguration mongoDBConfiguration) {
        this.mongoDBConfiguration = mongoDBConfiguration;
    }

    public RedisConfiguration getRedisConfiguration() {
        return this.redisConfiguration;
    }

    public void setRedisConfiguration(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }

    public BossConfiguration getBossConfiguration() {
        return this.bossConfiguration;
    }

    public void setBossConfiguration(BossConfiguration bossConfiguration) {
        this.bossConfiguration = bossConfiguration;
    }

    public SpawnConfiguration getSpawnConfiguration() {
        return this.spawnConfiguration;
    }

    public void setSpawnConfiguration(SpawnConfiguration spawnConfiguration) {
        this.spawnConfiguration = spawnConfiguration;
    }

}
