package fr.astfaster.skyblock.island;

import fr.astfaster.skyblock.island.member.SBIslandMember;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SBIsland {

    @BsonProperty(value = "_id")
    private String uuid;
    private String name;
    private String description;
    private double money;
    private String bank;
    @BsonProperty(value = "created_date")
    private long createdDate;
    private List<SBIslandMember> members;

    public SBIsland() {}

    public SBIsland(String uuid, String name, String description, double money, String bank, long createdDate) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.money = money;
        this.bank = bank;
        this.createdDate = createdDate;
        this.members = new ArrayList<>();
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getBank() {
        return this.bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public long getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public List<SBIslandMember> getMembers() {
        return this.members;
    }

    public void setMembers(List<SBIslandMember> members) {
        this.members = members;
    }

    public SBIslandMember getIslandMemberById(String uuid) {
        for (SBIslandMember member : this.members) {
            if (member.getUuid().equals(uuid)) return member;
        }
        return null;
    }

}
