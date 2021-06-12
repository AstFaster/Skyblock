package fr.astfaster.skyblock.island.boss.model;

import fr.astfaster.skyblock.configuration.nested.BossConfiguration;
import fr.astfaster.skyblock.island.boss.SBBossEntity;
import fr.astfaster.skyblock.player.SBPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SBZombieBoss extends SBBossEntity {

    private final double coinsWin = 450.0D;

    public SBZombieBoss() {
        super(ChatColor.RED + "Zombie boss", 100.0D, 3.0D);
    }

    @Override
    public void spawn(World world) {
        final BossConfiguration bossConfiguration = this.skyblock.getConfiguration().getBossConfiguration();
        final WorldServer craftWorld = ((CraftWorld) world).getHandle();
        final EntityGiantZombie entityZombie = new EntityGiantZombie(craftWorld);
        final Giant craftZombie = (Giant) entityZombie.getBukkitEntity();

        craftZombie.setMaxHealth(this.maxHealth);
        craftZombie.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));

        entityZombie.setHealth((float) this.maxHealth);
        entityZombie.setCustomName(this.name);
        entityZombie.setCustomNameVisible(true);
        entityZombie.setPosition(bossConfiguration.getArenaX(), bossConfiguration.getArenaY(), bossConfiguration.getArenaZ());

        entityZombie.goalSelector.a(2, new PathfinderGoalMeleeAttack(entityZombie, 1.0, true));
        entityZombie.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(entityZombie, EntityPlayer.class, 0, true, false, null));

        craftWorld.addEntity(entityZombie);
    }

    @Override
    protected void onDeath(EntityDeathEvent event) {
        super.onDeath(event);

        for (SBPlayer sbPlayer : this.skyblock.getBossManager().getSbPlayers()) {
            sbPlayer.setMoney(sbPlayer.getMoney() + this.coinsWin);

            this.skyblock.getPlayerManager().sendPlayerToRedis(sbPlayer);

            final Player player = Bukkit.getPlayer(UUID.fromString(sbPlayer.getUuid()));

            player.sendMessage(
                            ChatColor.GOLD + "Vous avez reçu "
                            + ChatColor.YELLOW + this.coinsWin + "$"
                            + ChatColor.GOLD + " pour avoir vaincu "
                            + ChatColor.YELLOW + this.name
            );

            player.sendMessage(ChatColor.GOLD + "Vous allez être re-téléporté au spawn...");

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(skyblock.getSpawnLocation());
                }
            }.runTaskLaterAsynchronously(this.skyblock, 100L);

        }

        new BukkitRunnable() {
            @Override
            public void run() {
                skyblock.getBossManager().stop();
            }
        }.runTaskLaterAsynchronously(this.skyblock, 100L);
    }

    @Override
    protected void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            final Player player = (Player) event.getDamager();
            player.sendMessage("Boss health : " + ((LivingEntity) event.getEntity()).getHealth());
        }
    }

}
