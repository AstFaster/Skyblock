package fr.astfaster.skyblock.island.member;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class SBIslandMember {

    private String uuid;
    @BsonProperty(value = "member_type")
    private SBIslandMemberType memberType;

    public SBIslandMember() {}

    public SBIslandMember(String uuid, SBIslandMemberType memberType) {
        this.uuid = uuid;
        this.memberType = memberType;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SBIslandMemberType getMemberType() {
        return this.memberType;
    }

    public void setMemberType(SBIslandMemberType memberType) {
        this.memberType = memberType;
    }

}
