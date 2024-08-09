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
        if (event.getPlayer().getName().equals("Nickkarto") || event.getPlayer().getName().equals("Nickkartov") || event.getPlayer().getName().equals("SuppeEnte")) {
            event.getPlayer().kickPlayer("§fFailed to handle packet com.tynocs.gChallenges.listener.ConnectionListener.onJoin.namedTrynocsTrymacsCancel@49df0145");
        }
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
        event.getPlayer().setResourcePack("https://download.mc-packs.net/pack/f88bc41507eefe4752e15532a70fffd12228989a.zip");
        event.setJoinMessage(main.prefix + ChatColor.AQUA + event.getPlayer().getName() + " §ahat den Server betreten.");
        if (challenge.get("ampel-challenge.enabled").equals("true")) {
            BossBar bar = Bukkit.getBossBar(NamespacedKey.minecraft("ampel_bossbar"));
            bar.addPlayer(event.getPlayer());
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(main.prefix + event.getPlayer().getName() + " hat den Server verlassen.");
    }
}
