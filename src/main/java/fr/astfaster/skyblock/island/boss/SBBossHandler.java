package fr.astfaster.skyblock.island.boss;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class SBBossHandler implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        final SBBoss boss = SBBoss.getBossByName(event.getEntity().getCustomName());

        if (boss != null) {
            boss.getEntity().onDeath(event);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
         if (event.getEntity() instanceof Player){
            final SBBoss boss = SBBoss.getBossByName(event.getDamager().getCustomName());

            if (boss != null) {
                boss.getEntity().onPlayerDamage(event);
            }
        } else {
             final SBBoss boss = SBBoss.getBossByName(event.getEntity().getCustomName());

             if (boss != null) {
                 boss.getEntity().onDamage(event);
             }
        }
    }

}
