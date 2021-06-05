package fr.astfaster.skyblock.configuration;

import fr.astfaster.skyblock.configuration.neested.MongoDBConfiguration;
import fr.astfaster.skyblock.configuration.neested.RedisConfiguration;

public class SBConfiguration {

    private MongoDBConfiguration mongoDBConfiguration;
    private RedisConfiguration redisConfiguration;

    public SBConfiguration(MongoDBConfiguration mongoDBConfiguration, RedisConfiguration redisConfiguration) {
        this.mongoDBConfiguration = mongoDBConfiguration;
        this.redisConfiguration = redisConfiguration;
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

}
