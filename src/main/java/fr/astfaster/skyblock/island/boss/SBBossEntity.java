package fr.astfaster.skyblock.island.boss;

import fr.astfaster.skyblock.Skyblock;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public abstract class SBBossEntity {

    protected double damage;
    protected double maxHealth;
    protected String name;

    protected Skyblock skyblock;

    public SBBossEntity(double damage, double maxHealth, String name, Skyblock skyblock) {
        this.damage = damage;
        this.maxHealth = maxHealth;
        this.name = name;
        this.skyblock = skyblock;
    }

    public SBBossEntity(String name, double maxHealth, double damage) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.damage = damage;
    }

    public abstract void spawn(World world);

    protected void onDeath(EntityDeathEvent event) {
        final SBBossManager bossManager = this.skyblock.getBossManager();

        for (Player player : bossManager.getPlayers()) {
            if (player.isOnline()) {
                player.sendMessage(ChatColor.GOLD + "Vous avez vaincu " + ChatColor.YELLOW + bossManager.getBoss().getEntity().getName() + ChatColor.GOLD + " !");
            }
        }
    }

    protected void onDamage(EntityDamageByEntityEvent event) {}

    protected void onPlayerDamage(EntityDamageByEntityEvent event) {
        event.setDamage(this.damage);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public Skyblock getSkyblock() {
        return this.skyblock;
    }

    public void setSkyblock(Skyblock skyblock) {
        this.skyblock = skyblock;
    }
}
