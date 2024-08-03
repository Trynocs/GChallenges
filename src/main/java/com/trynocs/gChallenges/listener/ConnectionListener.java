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
    private URL textures;
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        try {
            textures = new URL("https://download.mc-packs.net/pack/bf854d7986e51f7e8a5106533d73146dc38c60fb.zip");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        event.getPlayer().setResourcePack(String.valueOf(textures));
        event.setJoinMessage(main.prefix + ChatColor.GOLD + event.getPlayer().getName() + " §ahat den Server betreten.");
    }
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(main.prefix + ChatColor.GRAY + event.getPlayer().getName() + " hat den Server verlassen.");
    }
}
