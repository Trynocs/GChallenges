package com.trynocs.gChallenges.listener;

import com.trynocs.gChallenges.main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AmpelListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
        if (challenge.getString("ampel-challenge.enabled").equals("true") && challenge.getString("ampel-challenge.color").equals("red") && challenge.getString("timer.paused").equals("false")) {
            if (challenge.getString("settings.kill-all").equals("true")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() != GameMode.SPECTATOR)  {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getName());
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }else {
                if (event.getPlayer().getGameMode() != GameMode.SPECTATOR) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + event.getPlayer().getName());
                    event.getPlayer().setGameMode(GameMode.SPECTATOR);
                }
            }
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
        if (challenge.getString("ampel-challenge.enabled").equals("true") && challenge.getString("settings.die").equals("true") && challenge.getString("timer.paused").equals("false")) {
            if (challenge.getString("settings.kill-all").equals("true")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() != GameMode.SPECTATOR)  {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + player.getName());
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }else {
                if (event.getEntity().getGameMode() != GameMode.SPECTATOR) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill " + event.getEntity().getName());
                    event.getEntity().setGameMode(GameMode.SPECTATOR);
                }
            }
        }
    }
}