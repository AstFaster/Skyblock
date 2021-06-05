package fr.astfaster.skyblock.configuration.neested;

public class RedisConfiguration {

    private String redisIp;
    private int redisPort;
    private String redisPassword;

    public RedisConfiguration(String redisIp, int redisPort, String redisPassword) {
        this.redisIp = redisIp;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
    }

    public String getRedisIp() {
        return this.redisIp;
    }

    public void setRedisIp(String redisIp) {
        this.redisIp = redisIp;
    }

    public int getRedisPort() {
        return this.redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return this.redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

}
