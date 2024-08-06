package com.trynocs.gChallenges.listener;

import com.trynocs.gChallenges.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
        event.getPlayer().setResourcePack("https://download.mc-packs.net/pack/3aa556ba78807626e375d43b64fc56df6c27abb5.zip");
        event.setJoinMessage(main.prefix + ChatColor.GOLD + event.getPlayer().getName() + " §ahat den Server betreten.");
        if (challenge.get("ampel-challenge.enabled").equals("true")) {
            BossBar bar = Bukkit.getBossBar(NamespacedKey.minecraft("ampel_bossbar"));
            bar.addPlayer(event.getPlayer());
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(main.prefix + ChatColor.GRAY + event.getPlayer().getName() + " hat den Server verlassen.");
    }
}
