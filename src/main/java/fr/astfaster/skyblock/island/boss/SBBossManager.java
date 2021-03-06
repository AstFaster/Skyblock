package fr.astfaster.skyblock.island.boss;

import fr.astfaster.skyblock.Skyblock;
import fr.astfaster.skyblock.configuration.nested.BossConfiguration;
import fr.astfaster.skyblock.island.SBIsland;
import fr.astfaster.skyblock.player.SBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SBBossManager {

    private boolean playing = false;
    private boolean create = true;

    private SBBoss boss;
    private SBIsland island;

    private final List<SBPlayer> sbPlayers;
    private final List<Player> players;

    private final Skyblock skyblock;

    public SBBossManager(Skyblock skyblock) {
        this.skyblock = skyblock;
        this.sbPlayers = new ArrayList<>();
        this.players = new ArrayList<>();

        this.skyblock.getServer().getPluginManager().registerEvents(new SBBossHandler(), this.skyblock);
    }

    public void waitPlayers() {
        new BukkitRunnable() {
            @Override
            public void run() {
                start();
            }
        }.runTaskLater(this.skyblock, 120L);
        // 1200L
    }

    public void start() {
        this.playing = true;

        this.sendMessageToPlayers(ChatColor.GOLD + "Le combat commence !");

        if (this.checkOnlinePlayers()) {
            this.teleportPlayers();
            this.spawnBoss();
        }
    }

    public void stop() {
        this.island = null;
        this.boss = null;

        this.sbPlayers.clear();
        this.players.clear();

        this.playing = false;
        this.create = true;
    }

    private boolean checkOnlinePlayers() {
        final List<Player> disconnectedPlayers = new ArrayList<>();

        for (Player player : this.players) {
            if (!player.isOnline()) {
                disconnectedPlayers.add(player);
            }
        }

        if (disconnectedPlayers.size() == this.players.size()) {
            this.stop();
            return false;
        }
        return true;
    }

    private void teleportPlayers() {
        final BossConfiguration bossConfiguration = this.skyblock.getConfiguration().getBossConfiguration();

        this.sendMessageToPlayers(ChatColor.GOLD + "T??l??portation...");

        for (Player player : this.players) {
            if (player.isOnline()) {
                player.teleport(new Location(player.getWorld(), bossConfiguration.getArenaX(), bossConfiguration.getArenaY(), bossConfiguration.getArenaZ()));
            }
        }
    }

    private void spawnBoss() {
        new BukkitRunnable() {
            int timeRemaining = 5;

            @Override
            public void run() {
                if (timeRemaining != 0) {
                    final String secondsFormat = this.timeRemaining == 1 ? " seconde" : " secondes";

                    sendMessageToPlayers(ChatColor.GOLD + "Le boss va appara??tre dans " + ChatColor.RED + this.timeRemaining + ChatColor.GOLD + secondsFormat + " !");

                    this.timeRemaining--;
                } else {
                    final SBBossEntity entity = boss.getEntity();

                    entity.setSkyblock(skyblock);
                    entity.spawn(Bukkit.getWorld("world"));

                    sendMessageToPlayers(ChatColor.RED + "Le boss est apparu ! Bonne chance !");
                    this.cancel();
                }
            }
        }.runTaskTimer(this.skyblock, 0, 20L);
    }

    private void sendMessageToPlayers(String message) {
        for (Player player : this.players) {
            if (player.isOnline()) {
                player.sendMessage(message);
            }
        }
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<SBPlayer> getSbPlayers() {
        return this.sbPlayers;
    }

    public SBIsland getIsland() {
        return this.island;
    }

    public void setIsland(SBIsland island) {
        this.island = island;
    }

    public SBBoss getBoss() {
        return this.boss;
    }

    public void setBoss(SBBoss boss) {
        this.boss = boss;
    }

    public boolean canCreate() {
        return this.create;
    }

    public void setCanCreate(boolean create) {
        this.create = create;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
