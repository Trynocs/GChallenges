package com.trynocs.gChallenges.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.trynocs.gChallenges.main;
import com.trynocs.gChallenges.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

@CommandAlias("challenges")
@CommandPermission("trynocs.challenges")
public class Challenges extends BaseCommand implements Listener {

    private BukkitRunnable runnable;
    private int time;
    private static final Logger LOGGER = main.getPlugin().getLogger();
    private FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");

    @Default
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(main.prefix + "§5GChallenges von §bTrynocs §5für §bTheGalitube");
        sender.sendMessage(main.prefix + "§5Version des Plugins: §b" + main.plugin.getDescription().getVersion());
        sender.sendMessage(main.prefix + "§5Nutze /challenges help, um mehr zu erfahren");
    }

    @Subcommand("help")
    @CommandPermission("trynocs.challenges.help")
    public void help(CommandSender sender, String[] args) {
        sender.sendMessage(main.prefix + "§6Alle Commands");
        sender.sendMessage(main.prefix + "§b/challenges §8- §bPlugin Information");
        sender.sendMessage(main.prefix + "§b/challenges help §8- §bHilfetext");
        sender.sendMessage(main.prefix + "§b/challenges menu/gui §8- §bChallenges Menu");
    }

    @Subcommand("menu|gui")
    @CommandPermission("trynocs.challenges.menu")
    public void menu(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§6Challenges Menu §8» §bMain");
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setCustomModelData(1).setName("§7").build());
            }
            if (challenge.getString("ampel-challenge.enabled").equals("true")) {
                inventory.setItem(10, new ItemBuilder(Material.BEACON).setName("§2A§em§cp§2e§el §cC§2h§ea§cl§2l§ee§cn§2g§ee").setLocalizedName("ampel-challenge").setLore("§7", "§8» §aAktiv.", "§7", "§8» §7In der Ampel-Challenge gibt es eine Bossbar in der eine Farbe angezeigt wird.", "§8» §7Kurz gesagt: Lauf bei §cRot§f, und du bist §4Tod§7.", "§7", "§8» §aKlicken zum Spielen!").build());
            }else {
                inventory.setItem(10, new ItemBuilder(Material.BEACON).setName("§2A§em§cp§2e§el §cC§2h§ea§cl§2l§ee§cn§2g§ee").setLocalizedName("ampel-challenge").setLore("§7", "§8» §cInaktiv.", "§7", "§8» §7In der Ampel-Challenge gibt es eine Bossbar in der eine Farbe angezeigt wird.", "§8» Kurz gesagt: Lauf bei Rot, du bist Tod.", "§7", "§8» §aKlicken zum Spielen!").build());
            }
            if (challenge.getString("level-border.enabled").equals("true")) {
                inventory.setItem(12, new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName("§eLevel = Border").setLocalizedName("border").setLore("§7", "§8» §aAktiv.", "§7", "§8» §7In Level = Border wird die Border so groß wie dein Erfahrungs-Level ist.", "§8» §7z.b. bei Level 100 ist die Border 100x100 Blöcke groß.", "§7", "§8» §aKlicken zum Spielen!").build());
            }else {
                inventory.setItem(12, new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName("§eLevel = Border").setLocalizedName("border").setLore("§7", "§8» §cInaktiv.", "§7", "§8» §7In Level = Border wird die Border so groß wie dein Erfahrungs-Level ist.", "§8» §7z.b. bei Level 100 ist die Border 100x100 Blöcke groß.", "§7", "§8» §aKlicken zum Spielen!").build());
            }
            inventory.setItem(40, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§8» §5Einstellungen").setLocalizedName("settings").setCustomModelData(3).build());
            player.openInventory(inventory);
        }
    }

    @Subcommand("timer start")
    @CommandPermission("trynocs.challenges.timer.start")
    private void timer(CommandSender sender, String[] args) {
        time = challenge.getInt("timer.time", 0);

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                time++;
                challenge.set("timer.time", time);
                main.getPlugin().getConfigManager().saveCustomConfig("challenge");

                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(main.prefix + "§aTimer: " + shortInteger(time)));
                });
            }
        };
        runnable.runTaskTimer(main.getPlugin(), 0, 20);
        challenge.set("timer.paused", false);
        main.getPlugin().getConfigManager().saveCustomConfig("challenge");
    }

    @Subcommand("timer pause")
    @CommandPermission("trynocs.challenges.timer.pause")
    private void stopTimer(CommandSender sender) {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
            FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
            challenge.set("timer.paused", true);
            challenge.set("timer.time", time);
            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
            Bukkit.broadcastMessage(main.prefix + "§cTimer wurde von §5" + sender.getName() + " §cpausiert.");
            LOGGER.info("Timer wurde pausiert.");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule mobGriefing false");
        } else {
            sender.sendMessage(main.prefix + "Der Timer läuft nicht oder wurde bereits gestoppt.");
        }
    }

    @Subcommand("timer resume")
    @CommandPermission("trynocs.challenges.timer.resume")
    public void resumeTimer(CommandSender sender) {
        if (runnable == null || runnable.isCancelled()) {
            FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
            time = challenge.getInt("timer.time", 0); // Initialize the time variable

            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    time++;
                    challenge.set("timer.time", time);
                    main.getPlugin().getConfigManager().saveCustomConfig("challenge");

                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(main.prefix + "§aTimer: " + shortInteger(time)));
                    });
                }
            };
            runnable.runTaskTimer(main.getPlugin(), 0, 20);
            challenge.set("timer.paused", false);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule mobGriefing false");
            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
            Bukkit.broadcastMessage(main.prefix + "§aTimer wurde von §5" + sender.getName() + " §afortgesetzt.");
            LOGGER.info("Timer wurde fortgesetzt.");
        } else {
            sender.sendMessage(main.prefix + "Der Timer läuft bereits.");
        }
    }

    @Subcommand("reset")
    @CommandPermission("trynocs.challenges.reset")
    public void resetTimer(CommandSender sender) {
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
        challenge.set("settings.reset", true);
        main.getPlugin().getConfigManager().saveCustomConfig("challenge");
        if (sender instanceof Player player) {
            player.kickPlayer(main.prefix + "§cDie Welten werden zurückgesetzt.");
            LOGGER.info("Die Welten werden zurückgesetzt.");
            return;
        }
        sender.sendMessage(main.prefix + "Timer wurde zurückgesetzt.");
        LOGGER.info("Die Welten werden zurückgesetzt.");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
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
