package com.trynocs.gChallenges.listener;

import com.trynocs.gChallenges.main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setResourcePack("https://download.mc-packs.net/pack/e29cfd0e18293e3741dc184d6434f48fb31dd62c.zip");
        event.setJoinMessage(main.prefix + ChatColor.GOLD + event.getPlayer().getName() + " §ahat den Server betreten.");
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(main.prefix + ChatColor.GRAY + event.getPlayer().getName() + " hat den Server verlassen.");
    }
}
