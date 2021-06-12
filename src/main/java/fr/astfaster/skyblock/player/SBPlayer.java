package fr.astfaster.skyblock.player;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class SBPlayer {

    @BsonProperty(value = "_id")
    private String uuid;
    private String name;
    private String island;
    private double money;

    public SBPlayer() {}

    public SBPlayer(String uuid, String name, String island, double money) {
        this.uuid = uuid;
        this.name = name;
        this.island = island;
        this.money = money;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsland() {
        return this.island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

}
