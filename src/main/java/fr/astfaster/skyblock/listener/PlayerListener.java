package fr.astfaster.skyblock.listener;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.island.boss.SBBossManager;
import fr.astfaster.skyblock.player.SBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final Skyblock skyblock;

    public PlayerListener(Skyblock skyblock) {
        this.skyblock = skyblock;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.skyblock.getPlayerManager().handleLogin(event.getPlayer());

        player.teleport(this.skyblock.getSpawnLocation());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.skyblock.getPlayerManager().handleLogout(player);
    }

}
