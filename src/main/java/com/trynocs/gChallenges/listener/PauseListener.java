package com.trynocs.gChallenges.listener;

import com.trynocs.gChallenges.main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class PauseListener implements Listener {

    private FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (challenge.getString("timer.paused").equals("true")) {
            if (event.getPlayer().hasPermission("trynocs.challenges.timer.resume")) {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du kannst den Timer jetzt wieder fortsetzen, indem du /challenges timer resume in den Chat schreibt.");
            }else {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du musst warten bis ein Admin ihn fortsetzt.");
            }
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (challenge.getString("timer.paused").equals("true")) {
            if (event.getPlayer().hasPermission("trynocs.challenges.timer.resume")) {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du kannst den Timer jetzt wieder fortsetzen, indem du /challenges timer resume in den Chat schreibt.");
            }else {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du musst warten bis ein Admin ihn fortsetzt.");
            }
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInvInteract(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§6Challenges Menu §8» §bMain") || event.getView().getTitle().equals("§6Challenges Menu §8» §bSettings")) {
            if (challenge.getString("timer.paused").equals("true")) {
                if (event.getWhoClicked().hasPermission("trynocs.challenges.timer.resume")) {
                    event.getWhoClicked().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du kannst den Timer jetzt wieder fortsetzen, indem du /challenges timer resume in den Chat schreibt.");
                }else {
                    event.getWhoClicked().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du musst warten bis ein Admin ihn fortsetzt.");
                }
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onHotbarSwitch(PlayerItemHeldEvent event) {
        if (challenge.getString("timer.paused").equals("true")) {
            if (event.getPlayer().hasPermission("trynocs.challenges.timer.resume")) {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du kannst den Timer jetzt wieder fortsetzen, indem du /challenges timer resume in den Chat schreibt.");
            }else {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du musst warten bis ein Admin ihn fortsetzt.");
            }
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (challenge.getString("timer.paused").equals("true")) {
            if (event.getPlayer().hasPermission("trynocs.challenges.timer.resume")) {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du kannst den Timer jetzt wieder fortsetzen, indem du /challenges timer resume in den Chat schreibt.");
            }else {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du musst warten bis ein Admin ihn fortsetzt.");
            }
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (challenge.getString("timer.paused").equals("true")) {
            if (event.getPlayer().hasPermission("trynocs.challenges.timer.resume")) {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du kannst den Timer jetzt wieder fortsetzen, indem du /challenges timer resume in den Chat schreibt.");
            }else {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du musst warten bis ein Admin ihn fortsetzt.");
            }
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (challenge.getString("timer.paused").equals("true")) {
            if (event.getPlayer().hasPermission("trynocs.challenges.timer.resume")) {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du kannst den Timer jetzt wieder fortsetzen, indem du /challenges timer resume in den Chat schreibt.");
            }else {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du musst warten bis ein Admin ihn fortsetzt.");
            }
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (challenge.getString("timer.paused").equals("true")) {
            if (event.getWhoClicked().hasPermission("trynocs.challenges.timer.resume")) {
                event.getWhoClicked().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du kannst den Timer jetzt wieder fortsetzen, indem du /challenges timer resume in den Chat schreibt.");
            }else {
                event.getWhoClicked().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du musst warten bis ein Admin ihn fortsetzt.");
            }
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (challenge.getString("timer.paused").equals("true")) {
            if (event.getPlayer().hasPermission("trynocs.challenges.menu")) {
                return;
            }
            if (event.getPlayer().hasPermission("trynocs.challenges.timer.resume")) {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du kannst den Timer jetzt wieder fortsetzen, indem du /challenges timer resume in den Chat schreibt.");
            }else {
                event.getPlayer().sendMessage(main.prefix + "§cDer Timer wurde pausiert. Du musst warten bis ein Admin ihn fortsetzt.");
            }
            event.setCancelled(true);
        }
    }
}
