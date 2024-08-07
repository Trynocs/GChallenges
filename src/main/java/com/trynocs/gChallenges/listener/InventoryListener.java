package com.trynocs.gChallenges.listener;

import com.trynocs.gChallenges.main;
import com.trynocs.gChallenges.utils.ItemBuilder;
import com.trynocs.gChallenges.utils.challenge.AmpelChanger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
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

    // Constructor to load the saved time
    public InventoryListener() {
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
        if (event.getCurrentItem() == null) return;
        if (event.getView().getTitle().equals("§6Challenges Menu §8» §bMain") || event.getView().getTitle().equals("§6Challenges Menu §8» §bSettings")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem().getItemMeta().hasLocalizedName()) {
                switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                    case "ampel-challenge":
                        player.closeInventory();
                        if (challenge.getString("ampel-challenge.enabled").equals("true")) {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat die Ampel Challenge beendet!");
                            challenge.set("ampel-challenge.enabled", "false");
                            challenge.set("ampel-challenge.color", "green");
                            challenge.set("timer.time", 0);
                            Bukkit.getBossBar(NamespacedKey.minecraft("ampel_bossbar")).removeAll();
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat die Ampel Challenge beendet!", "", 0, 100, 0);
                            }
                        } else {
                            AmpelChanger ampelChanger = new AmpelChanger(main.getPlugin());
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat die Ampel Challenge gestartet!");
                            challenge.set("ampel-challenge.enabled", "true");
                            challenge.set("ampel-challenge.color", "green");
                            challenge.set("timer.time", time);
                            ampelChanger.setColor("green"); // Set initial color and start task
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat die Ampel Challenge gestartet!", "", 0, 100, 0);
                            }
                        }
                        break;
                    case "settings":
                        player.closeInventory();
                        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§6Challenges Menu §8» §bSettings");
                        for (int i = 0; i < inventory.getSize(); i++) {
                            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setCustomModelData(1).setName("§7").build());
                        }
                        if (challenge.getString("settings.kill-all").equals("true")) {
                            inventory.setItem(10, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setLocalizedName("killall").setCustomModelData(2).setName("§8» §cKill-All").setLore("§7", "§8» §aAktiviert.", "§7", "§8» §7Einer für alle und alle für einen.").build());
                        }else {
                            inventory.setItem(10, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setLocalizedName("killall").setCustomModelData(2).setName("§8» §cKill-All").setLore("§7", "§8» §cDeaktiviert.", "§7", "§8» §7Einer für alle und alle für einen.").build());
                        }
                        if (challenge.getString("settings.normal-death-end").equals("true")) {
                            inventory.setItem(12, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setLocalizedName("normal-death-end").setCustomModelData(3).setName("§8» §4Normal-Death-End").setLore("§7", "§8» §aAktiviert.", "§7", "§8» §7Ein Tod wird das ende bedeuten, egal wie er passiert.").build());
                        }else {
                            inventory.setItem(12, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setLocalizedName("normal-death-end").setCustomModelData(3).setName("§8» §4Normal-Death-End").setLore("§7", "§8» §cDeaktiviert.", "§7", "§8» §7Ein Tod wird das ende bedeuten, egal wie er passiert.").build());
                        }
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
                        } else {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat die Kill-All Setting aktiviert!");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat die Kill-All Setting aktiviert!", "", 0, 100, 0);
                            }
                            challenge.set("settings.kill-all", "true");
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        }
                        break;
                    case "normal-death-end":
                        player.closeInventory();
                        if (challenge.getString("settings.normal-death-end").equals("true")) {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat die Normal-Death-End Setting deaktiviert!");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat die Normal-Death-End Setting deaktiviert!", "", 0, 100, 0);
                            }
                            challenge.set("settings.normal-death-end", "false");
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        } else {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat die Normal-Death-End Setting aktiviert!");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat die Normal-Death-End Setting aktiviert!", "", 0, 100, 0);
                            }
                            challenge.set("settings.normal-death-end", "true");
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        }
                        break;
                    case "border":
                        player.closeInventory();
                        if (challenge.getString("level-border.enabled").equals("true")) {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat Level = Border beendet!");
                            challenge.set("level-border.enabled", "false");
                            challenge.set("timer.time", 0);
                            challenge.set("level-border.blocks", 1);
                            main.getPlugin().getLevelUpListener().worldBorder.setSize(59999968);
                            main.getPlugin().getLevelUpListener().worldBorder_nether.setSize(59999968);
                            main.getPlugin().getLevelUpListener().worldBorder_the_end.setSize(59999968);
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat Level = Border beendet!", "", 0, 100, 0);
                            }
                        } else {
                            Bukkit.broadcastMessage(main.prefix + "§b" + player.getName() + " §7hat Level = Border gestartet!");
                            challenge.set("level-border.enabled", "true");
                            challenge.set("level-border.blocks", 1);
                            challenge.set("timer.time", time);
                            Location location = main.getPlugin().getLevelUpLocationManager().getLocationConfig(player.getWorld().getName());
                            main.getPlugin().getLevelUpListener().worldBorder.setSize(1);
                            main.getPlugin().getLevelUpListener().worldBorder_nether.setSize(1);
                            main.getPlugin().getLevelUpListener().worldBorder_the_end.setSize(1);
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle("§b" + player.getName() + " §7hat Level = Border gestartet!", "", 0, 100, 0);
                                players.teleport(location);
                            }
                            if (challenge.getString("ampel-challenge.enabled").equals("true")) {
                                challenge.set("ampel-challenge.enabled", "false");
                            }
                        }
                        break;
                }
            }
        }
    }
}
