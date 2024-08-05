package com.trynocs.gChallenges.listener;

import com.trynocs.gChallenges.main;
import com.trynocs.gChallenges.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryListener implements Listener {
    private int time;
    private BukkitRunnable runnable;
    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        runnable  = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(main.prefix + "§aTimer: " + shortInteger(time)));
                });
                time++;
            }
        };
        if (event.getCurrentItem() == null) return;
        if (event.getView().getTitle() == "§6Challenges Menu §8» §bMain") {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem().getItemMeta().hasLocalizedName()) {
                switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                    case "ampel-challenge":
                        player.closeInventory();
                        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
                        if (challenge.getString("ampel-challenge.enabled").equals("true")) {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat die Ampel Challenge beendet!");
                            challenge.set("ampel-challenge.enabled", "false");
                            challenge.set("ampel-challenge.color", "green");
                            runnable.cancel();
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        }else {
                            Bukkit.broadcastMessage(main.prefix + "§6" + player.getName() + " §7hat die Ampel Challenge gestartet!");
                            challenge.set("ampel-challenge.enabled", true);
                            challenge.set("ampel-challenge.color", "green");
                            runnable.runTaskTimer(main.getPlugin(), 0, 20);
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        }
                        break;
                    case "settings":
                        player.closeInventory();
                        Inventory inventory = Bukkit.createInventory(null, 9*3, "§6Challenges Menu §8» §bSettings");
                        for (int i = 0; i < inventory.getSize(); i++) {
                            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setCustomModelData(1).setName("§7").build());
                        };
                        player.openInventory(inventory);
                        break;
                }
            }
        }
    }

    public static String shortInteger(int duration) {
        String string = "";

        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        if (duration / 60 / 60 >= 1) {
            hours = duration / 60 / 60;
            duration = duration - ((duration / 60 / 60) * 60 * 60);
        }

        if (duration / 60 >= 1) {
            minutes = duration / 60;
            duration = duration - ((duration / 60) * 60);
        }

        if (duration >= 1) {
            seconds = duration;
        }


        if (hours <= 9) {
            string = string + "0" + hours + ":";
        } else {
            string = string + hours + ":";
        }


        if (minutes <= 9) {
            string = string + "0" + minutes + ":";
        } else {
            string = string + minutes + ":";
        }


        if (seconds <= 9) {
            string = string + "0" + seconds;
        } else {
            string = string + seconds;
        }

        return string;

    }
}
