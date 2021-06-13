package fr.astfaster.skyblock.market;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class SBMarketItem {

    @BsonProperty(value = "_id")
    private String uuid;
    @BsonProperty(value = "item_stack")
    private String itemStack;
    @BsonProperty(value = "buying_price")
    private double buyingPrice;
    @BsonProperty(value = "owner_uuid")
    private String ownerUuid;

    public SBMarketItem() {}

    public SBMarketItem(String uuid, String itemStack, double buyingPrice, String ownerUuid) {
        this.uuid = uuid;
        this.itemStack = itemStack;
        this.buyingPrice = buyingPrice;
        this.ownerUuid = ownerUuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(String itemStack) {
        this.itemStack = itemStack;
    }

    public double getBuyingPrice() {
        return this.buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getOwnerUuid() {
        return this.ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }
}
