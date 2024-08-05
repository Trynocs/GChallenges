package com.trynocs.gChallenges.listener;

import com.trynocs.gChallenges.main;
import com.trynocs.gChallenges.utils.ItemBuilder;
import com.trynocs.gChallenges.utils.challenge.AmpelChanger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
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
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
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
        if (event.getView().getTitle() == "§6Challenges Menu §8» §bMain" || event.getView().getTitle() == "§6Challenges Menu §8» §bSettings") {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem().getItemMeta().hasLocalizedName()) {
                switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                    case "ampel-challenge":
                        player.closeInventory();
                        if (challenge.getString("ampel-challenge.enabled").equals("true")) {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat die Ampel Challenge beendet!");
                            challenge.set("ampel-challenge.enabled", "false");
                            challenge.set("ampel-challenge.color", "green");
                            runnable.cancel();
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        } else {
                            Bukkit.broadcastMessage(main.prefix + "§6" + player.getName() + " §7hat die Ampel Challenge gestartet!");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat die Ampel Challenge gestartet!", "", 0, 100, 0);
                            }
                            challenge.set("ampel-challenge.enabled", true);
                            challenge.set("ampel-challenge.color", "green");
                            AmpelChanger ampelChanger = new AmpelChanger(main.getPlugin());
                            ampelChanger.startColorChangeTask();
                            BossBar bar = Bukkit.createBossBar("§6Ampelfarbe: §aGrün", org.bukkit.boss.BarColor.GREEN, org.bukkit.boss.BarStyle.SOLID);
                            bar.setVisible(true);
                            runnable.runTaskTimer(main.getPlugin(), 0, 20);
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        }
                        break;
                    case "settings":
                        player.closeInventory();
                        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§6Challenges Menu §8» §bSettings");
                        for (int i = 0; i < inventory.getSize(); i++) {
                            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setCustomModelData(1).setName("§7").build());
                        };
                        inventory.setItem(10, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setLocalizedName("killall").setCustomModelData(2).setName("§8» §cKill-All").build());
                        player.openInventory(inventory);
                        break;
                    case "killall":
                        player.closeInventory();
                        if (challenge.getString("settings.kill-all").equals("true")) {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat die Kill-All Setting deaktiviert!");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat die Kill-All Setting deaktiviert!", "", 0, 100, 0);
                            }
                            challenge.set("settings.kill-all", "false");
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        }else {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat die Kill-All Setting aktiviert!");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat die Kill-All Setting aktiviert!", "", 0, 100, 0);
                            }
                            challenge.set("settings.kill-all", "true");
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        }
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
