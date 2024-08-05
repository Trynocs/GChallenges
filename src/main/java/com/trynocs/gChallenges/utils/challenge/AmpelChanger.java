package com.trynocs.gChallenges.utils.challenge;

import com.trynocs.gChallenges.main;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AmpelChanger {

    private static final String[] COLORS = {"green", "yellow", "red"};
    private final JavaPlugin plugin;
    private final NamespacedKey bossBarKey;
    private final Random random;
    private BukkitRunnable colorChangeTask;
    private BossBar bossBar;
    private int currentIndex;
    private FileConfiguration challenge;

    public AmpelChanger(JavaPlugin plugin) {
        this.plugin = plugin;
        this.bossBarKey = NamespacedKey.minecraft("ampel_bossbar");
        this.random = new Random();
        this.currentIndex = 0;
        this.challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");

        if (challenge.getBoolean("ampel-challenge.enabled", false)) {
            startColorChangeTask();
        }
    }

    public void startColorChangeTask() {
        // Cancel any existing color change task before scheduling a new one
        if (colorChangeTask != null) {
            colorChangeTask.cancel();
        }
        colorChangeTask = new BukkitRunnable() {
            @Override
            public void run() {
                changeColor();
            }
        };
        scheduleNextChange();
    }

    private void scheduleNextChange() {
        long delay = 500L + random.nextInt(800);
        colorChangeTask.runTaskLater(plugin, delay);
    }

    public void changeColor() {
        if (COLORS[currentIndex].equals("yellow")) {
            startRedCountdown();
        } else {
            currentIndex = random.nextInt(COLORS.length - 1); // Exclude red
            updateBossBar(COLORS[currentIndex]);
            plugin.saveConfig();
            startColorChangeTask(); // Schedule the next color change
        }
    }

    private void startRedCountdown() {
        new BukkitRunnable() {
            int countdown = 3;

            @Override
            public void run() {
                if (countdown > 0) {
                    if (bossBar != null) {
                        bossBar.setTitle("§6Rot in: §c" + countdown);
                    }
                    countdown--;
                } else {
                    currentIndex = 2; // Set to red
                    updateBossBar(COLORS[currentIndex]);
                    plugin.saveConfig();
                    startColorChangeTask(); // Schedule the next color change
                    cancel(); // Stop the countdown task
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Countdown every second
    }

    private void updateBossBar(String color) {
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(bossBarKey, "", org.bukkit.boss.BarColor.GREEN, org.bukkit.boss.BarStyle.SOLID);
        }

        switch (color) {
            case "green":
                bossBar.setTitle("§6AmpelFarbe: §aGrün");
                bossBar.setColor(org.bukkit.boss.BarColor.GREEN);
                break;
            case "yellow":
                bossBar.setTitle("§6AmpelFarbe: §eGelb");
                bossBar.setColor(org.bukkit.boss.BarColor.YELLOW);
                break;
            case "red":
                bossBar.setTitle("§6AmpelFarbe: §cRot");
                bossBar.setColor(org.bukkit.boss.BarColor.RED);
                break;
        }
        bossBar.setVisible(true);
    }

    public String getCurrentColor() {
        return challenge.getString("ampel-challenge.color", "green");
    }

    public void stopTask() {
        if (colorChangeTask != null) {
            colorChangeTask.cancel();
        }
        if (bossBar != null) {
            bossBar.setVisible(false);
        }
    }
}
