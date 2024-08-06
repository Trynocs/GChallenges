package com.trynocs.gChallenges.listener;

import com.trynocs.gChallenges.main;
import com.trynocs.gChallenges.utils.ItemBuilder;
import com.trynocs.gChallenges.utils.challenge.AmpelChanger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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
        this.time = challenge.getInt("ampel-challenge.timer", 0); // Load saved time or 0 if not present
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
        if (event.getCurrentItem() == null) return;
        if (event.getView().getTitle().equals("§6Challenges Menu §8» §bMain") || event.getView().getTitle().equals("§6Challenges Menu §8» §bSettings")) {
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
                        } else {
                            AmpelChanger ampelChanger = new AmpelChanger(main.getPlugin());
                            Bukkit.broadcastMessage(main.prefix + "§6" + player.getName() + " §7hat die Ampel Challenge gestartet!");
                            challenge.set("ampel-challenge.enabled", true);
                            challenge.set("ampel-challenge.color", "green");
                            challenge.set("timer.time", time);
                            ampelChanger.setColor("green"); // Set initial color and start task
                            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                        }
                        break;
                    case "settings":
                        player.closeInventory();
                        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§6Challenges Menu §8» §bSettings");
                        for (int i = 0; i < inventory.getSize(); i++) {
                            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setCustomModelData(1).setName("§7").build());
                        }
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
                        } else {
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
}
