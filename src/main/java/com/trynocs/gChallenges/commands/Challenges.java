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
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.logging.Logger;

@CommandAlias("challenges")
@CommandPermission("trynocs.challenges")
public class Challenges extends BaseCommand implements Listener {

    private BukkitRunnable runnable;
    private int time;
    private static final Logger LOGGER = main.getPlugin().getLogger();

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
            inventory.setItem(10, new ItemBuilder(Material.BEACON).setName("§bAmpel Challenge").setLocalizedName("ampel-challenge").setLore("§8» §7In der Ampel-Challenge gibt es eine Bossbar in der eine Farbe angezeigt wird.", "§8» Kurz gesagt: Lauf bei Rot, du bist Tod.", "Klicken zum Spielen!").build());
            inventory.setItem(40, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§8» §5Einstellungen").setLocalizedName("settings").setCustomModelData(3).build());
            player.openInventory(inventory);
        }
    }

    @Subcommand("reset")
    @CommandPermission("trynocs.challenges.reset")
    public void onReset(CommandSender sender) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(main.prefix + "§cDie Welten werden zurückgesetzt, bitte warte einen Moment.");
        }

        // Warte 5 Sekunden, damit die Spieler gekickt werden können
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String[] worldNames = {"world", "world_nether", "world_the_end"};
        for (String worldName : worldNames) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                LOGGER.info("Welt " + worldName + " wurde nicht gefunden.");
                continue;
            }

            File worldFolder = world.getWorldFolder();
            LOGGER.info("Setze Welt zurück: " + worldName);

            // Welt entladen, mit wiederholtem Versuch
            boolean isUnloaded = false;
            while (!isUnloaded) {
                isUnloaded = Bukkit.unloadWorld(world, false);
                if (!isUnloaded) {
                    LOGGER.info("Fehler beim Entladen der Welt: " + worldName + ". Versuche es erneut...");
                    try {
                        Thread.sleep(5000); // Warte 5 Sekunden und versuche es erneut
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Weltverzeichnis löschen
            try {
                deleteWorldFolder(worldFolder.toPath());
            } catch (IOException e) {
                LOGGER.info("Fehler beim Löschen des Weltverzeichnisses: " + e.getMessage());
                continue;
            }

            // Neuen Seed generieren
            long newSeed = new Random().nextLong();
            WorldCreator creator = new WorldCreator(worldName).seed(newSeed);

            // Neue Welt erstellen
            World newWorld = Bukkit.createWorld(creator);
            if (newWorld != null) {
                LOGGER.info(main.prefix + "Welt " + worldName + " wurde mit einem neuen zufälligen Seed zurückgesetzt. Seed: " + newSeed);
            } else {
                LOGGER.info("Fehler beim Erstellen der Welt: " + worldName);
            }
        }

        // Spieler teleportieren und Nachricht senden
        World overworld = Bukkit.getWorld("world");
        if (overworld != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(overworld.getSpawnLocation());
                player.sendMessage(main.prefix + "§aDie Welten wurden zurückgesetzt.");
            }
        }

        // Server neustarten
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
    }

    @Subcommand("timer start")
    @CommandPermission("trynocs.challenges.timer.start")
    private void timer(CommandSender sender, String[] args) {
        runnable  = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(main.prefix + "§aTimer: " + shortInteger(time)));
                });
                time++;
            }
        };
        runnable.runTaskTimer(main.getPlugin(), 0, 20);
    }

    @Subcommand("timer stop")
    @CommandPermission("trynocs.challenges.timer.stop")
    private void stopTimer(CommandSender sender) {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
            Bukkit.broadcastMessage(main.prefix + "§cTimer wurde von §5" + sender.getName() + " §cgestoppt.");
            LOGGER.info("Timer wurde gestoppt.");
        } else {
            sender.sendMessage(main.prefix + "§cDer Timer läuft nicht oder wurde bereits gestoppt.");
        }
    }

    private void deleteWorldFolder(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            Files.list(path).forEach(file -> {
                try {
                    deleteWorldFolder(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        Files.delete(path);
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
