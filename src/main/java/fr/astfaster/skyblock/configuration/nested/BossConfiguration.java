package fr.astfaster.skyblock.configuration.nested;

public class BossConfiguration {

    private double arenaX;
    private double arenaY;
    private double arenaZ;

    public BossConfiguration(double arenaX, double arenaY, double arenaZ) {
        this.arenaX = arenaX;
        this.arenaY = arenaY;
        this.arenaZ = arenaZ;
    }

    public double getArenaX() {
        return this.arenaX;
    }

    public void setArenaX(double arenaX) {
        this.arenaX = arenaX;
    }

    public double getArenaY() {
        return this.arenaY;
    }

    public void setArenaY(double arenaY) {
        this.arenaY = arenaY;
    }

    public double getArenaZ() {
        return this.arenaZ;
    }

    public void setArenaZ(double arenaZ) {
        this.arenaZ = arenaZ;
    }

}
