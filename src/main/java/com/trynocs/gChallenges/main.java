package com.trynocs.gChallenges;

import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandCompletions;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.Values;
import com.trynocs.gChallenges.commands.Challenges;
import com.trynocs.gChallenges.commands.Gamemode;
import com.trynocs.gChallenges.listener.*;
import com.trynocs.gChallenges.utils.challenge.AmpelChanger;
import com.trynocs.gChallenges.utils.config.Configmanager;
import com.trynocs.gChallenges.utils.config.LevelUpLocationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import co.aikar.commands.annotation.CommandCompletion;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public final class main extends JavaPlugin {

    public static main plugin;

    public static String prefix;
    public static String noperm;
    public static String beplayer;
    public static String noplayer;
    public static String discord;

    private Configmanager configManager;
    private PaperCommandManager commandManager;
    private PluginManager pluginManager;
    private AmpelChanger ampelChanger;
    private LevelUpListener levelUpListener;
    private LevelUpLocationManager levelUpLocationManager;

    @Override
    public void onEnable() {
        getLogger().info("Plugin wird aktiviert...");
        plugin = this;
        commandManager = new PaperCommandManager(this);
        configManager = new Configmanager(this);
        configManager.saveDefaultConfig();
        FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
        if (challenge.getString("settings.reset").equals("true")) {
            for (String worldName : Bukkit.getWorlds().stream().map(World::getName).toList()) {
                if (Bukkit.getWorld(worldName) != null) {
                    Bukkit.unloadWorld(worldName, false);
                }

                deleteWorldFolder(Bukkit.getWorldContainer().toPath().resolve(worldName).toFile());
            }
            challenge.set("settings.reset", "false");
            main.getPlugin().getConfigManager().saveCustomConfig("challenge");
        }
        ampelChanger = new AmpelChanger(this);
        levelUpListener = new LevelUpListener();
        levelUpLocationManager = new LevelUpLocationManager();
        pluginManager = main.getPlugin().getServer().getPluginManager();
        if (main.getPlugin().getServer().getWorld("world") != null) {
            main.getPlugin().getLevelUpLocationManager().setLocationConfig("world", Bukkit.getWorlds().get(0).getSpawnLocation());
        }
        loadConfigValues();
        registerCommands();
        pluginManager.registerEvents(new ConnectionListener(), this);
        pluginManager.registerEvents(new AmpelListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new PauseListener(), this);
        pluginManager.registerEvents(new LevelUpListener(), this);
        Bukkit.getServer().setMotd(main.prefix + "Version: " + getDescription().getVersion() + "\n" + "§bBy Trynocs");
        if (main.getPlugin().getConfigManager().getCustomConfig("challenge").getString("ampel-challenge.enabled").equals("true")) {
            ampelChanger.setColor(main.getPlugin().getConfigManager().getCustomConfig("challenge").getString("ampel-challenge.color"));
            ampelChanger.startColorChangeTask();
        }
        if (challenge.getString("level-border.enabled").equals("true") && main.getPlugin().getServer().getWorld("world") != null && main.getPlugin().getServer().getWorld("world_nether") != null && main.getPlugin().getServer().getWorld("world_the_end") != null) {
            main.getPlugin().getLevelUpListener().worldBorder.setSize(challenge.getInt("level-border.blocks"), 30);
            main.getPlugin().getLevelUpListener().worldBorder_nether.setSize(challenge.getInt("level-border.blocks"), 30);
            main.getPlugin().getLevelUpListener().worldBorder_the_end.setSize(challenge.getInt("level-border.blocks"), 30);
        }else {
            main.getPlugin().getLevelUpListener().worldBorder.setSize(59999968);
            main.getPlugin().getLevelUpListener().worldBorder_nether.setSize(59999968);
            main.getPlugin().getLevelUpListener().worldBorder_the_end.setSize(59999968);
        }
        getLogger().info("Plugin wurde aktiviert.");
    }


    private void deleteWorldFolder(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorldFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
            path.delete();
        }
    }

    private void registerCommands() {
        Challenges challenges = new Challenges();
        commandManager.registerCommand(challenges);
    }

    private void registerCompletions() {
        Gamemode gamemode = new Gamemode();
        commandManager.getCommandCompletions().registerAsyncCompletion("gamemode", c -> {
            return List.of("survival", "creative", "adventure", "spectator", "0", "1", "2", "3");
        });
        
    }

    @Override
    public void onDisable() {
        if (ampelChanger != null) {
            ampelChanger.stopTask();
        }
        if (Bukkit.getBossBars() != null) {
            for (Iterator<KeyedBossBar> it = Bukkit.getBossBars(); it.hasNext(); ) {
                BossBar bar = it.next();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    bar.removePlayer(player);
                }
            }
        }
        getLogger().info("Plugin wurde deaktiviert");
    }

    private void loadConfigValues() {
        String prefix2 = configManager.getConfig().getString("messages.prefix");
        String noperm2 = configManager.getConfig().getString("messages.no-perm");
        String beplayer2 = configManager.getConfig().getString("messages.not-player");
        String noplayer2 = configManager.getConfig().getString("messages.no-player");
        String discord2 = configManager.getConfig().getString("messages.discord");
        prefix = ChatColor.translateAlternateColorCodes('&', prefix2);
        noperm = main.prefix + ChatColor.translateAlternateColorCodes('&', noperm2);
        beplayer = main.prefix + ChatColor.translateAlternateColorCodes('&', beplayer2);
        noplayer = main.prefix + ChatColor.translateAlternateColorCodes('&', noplayer2);
        discord = ChatColor.translateAlternateColorCodes('&', discord2);
    }

    public static main getPlugin() {
        return plugin;
    }

    public Configmanager getConfigManager() {
        return configManager;
    }
    public LevelUpListener getLevelUpListener() {
        return levelUpListener;
    }
    public LevelUpLocationManager getLevelUpLocationManager() {
        return levelUpLocationManager;
    }

    public static String translateColors(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> translateColors(List<String> texts) {
        return texts.stream().map(main::translateColors).toList();
    }
}
