package fr.astfaster.skyblock.listener;

import fr.astfaster.skyblock.Skyblock;
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
        this.skyblock.getPlayerManager().handleLogin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.skyblock.getPlayerManager().handleLogout(event.getPlayer());
    }

}
