package fr.astfaster.skyblock.island.member;

public enum SBIslandMemberType {

    LEADER(0, "Chef"),
    DEPUTY(1, "Adjoint"),
    MEMBER(2, "Membre"),
    NEW(3, "Nouveau");

    private final int id;
    private final String name;

    SBIslandMemberType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static SBIslandMemberType getMemberTypeById(int id) {
        for (SBIslandMemberType memberType : values()) {
            if (memberType.getId() == id) {
                return memberType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
