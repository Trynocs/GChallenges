package com.trynocs.gChallenges.utils.challenge;

import com.trynocs.gChallenges.main;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AmpelChanger {

    private final JavaPlugin plugin;
    private final NamespacedKey bossBarKey;
    private final Random random;
    private BukkitRunnable colorChangeTask;
    private BossBar bossBar;
    private FileConfiguration challenge;

    public AmpelChanger(JavaPlugin plugin) {
        this.plugin = plugin;
        this.bossBarKey = NamespacedKey.minecraft("ampel_bossbar");
        this.random = new Random();
        this.challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");

        // Retrieve or create the BossBar
        this.bossBar = Bukkit.getBossBar(bossBarKey);
        if (this.bossBar == null) {
            this.bossBar = Bukkit.createBossBar(bossBarKey, "", BarColor.GREEN, org.bukkit.boss.BarStyle.SOLID);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }

        if ("true".equals(challenge.getString("ampel-challenge.enabled"))) {
            startColorChangeTask();
        }
    }

    public void setColor(String color) {
        challenge.set("ampel-challenge.color", color);
        main.getPlugin().getConfigManager().saveCustomConfig("challenge");
        updateBossBar(color);
        if ("true".equals(challenge.getString("ampel-challenge.enabled")) && "false".equals(challenge.getString("timer.paused"))) {
            scheduleNextChange();
        }
    }

    private void updateBossBar(String color) {
        switch (color.toLowerCase()) {
            case "green":
                bossBar.setTitle("§6Ampelfarbe: §aGrün");
                bossBar.setColor(BarColor.GREEN);
                break;
            case "yellow":
                bossBar.setTitle("§6Ampelfarbe: §eGelb");
                bossBar.setColor(BarColor.YELLOW);
                break;
            case "red":
                bossBar.setTitle("§6Ampelfarbe: §cRot");
                bossBar.setColor(BarColor.RED);
                break;
        }
        bossBar.setVisible(true);
        bossBar.setProgress(1.0);
        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }
    }

    public void startColorChangeTask() {
        if (colorChangeTask != null && !colorChangeTask.isCancelled()) {
            colorChangeTask.cancel();
        }
        colorChangeTask = new BukkitRunnable() {
            @Override
            public void run() {
                if ("false".equals(challenge.getString("timer.paused"))) {
                    changeColor();
                }
            }
        };
        colorChangeTask.runTaskLater(plugin, 20L * 60); // Change color every minute
    }

    private void scheduleNextChange() {
        if (colorChangeTask != null && !colorChangeTask.isCancelled()) {
            colorChangeTask.cancel();
        }
        colorChangeTask = new BukkitRunnable() {
            @Override
            public void run() {
                if ("false".equals(challenge.getString("timer.paused"))) {
                    changeColor();
                }
            }
        };
        long delay = 500 + random.nextInt(800);
        colorChangeTask.runTaskLater(plugin, delay);
    }

    public void changeColor() {
        switch (challenge.getString("ampel-challenge.color")) {
            case "green":
                setColor("yellow");
                break;
            case "yellow":
                if ("true".equals(challenge.getString("ampel-challenge.enabled"))) {
                    startRedCountdown();
                }
                break;
            case "red":
                setColor("green");
                break;
        }
    }

    private void startRedCountdown() {
        new BukkitRunnable() {
            int countdown = 3;

            @Override
            public void run() {
                if (countdown > 0) {
                    if (bossBar != null) {
                        bossBar.setColor(BarColor.RED);
                        bossBar.setTitle("§6Rot in: §c" + countdown);
                        bossBar.setProgress(countdown / 3.0);
                    }
                    countdown--;
                } else {
                    setColor("red");
                    cancel(); // Stop the countdown task
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Countdown every second
    }

    public void stopTask() {
        if (colorChangeTask != null) {
            colorChangeTask.cancel();
        }
        if (bossBar != null) {
            bossBar.setVisible(false);
        }
    }

    public String getCurrentColor() {
        return challenge.getString("ampel-challenge.color");
    }

    public NamespacedKey getBossBarKey() {
        return bossBarKey;
    }
}
