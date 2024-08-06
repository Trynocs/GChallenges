package com.trynocs.gChallenges;

import co.aikar.commands.PaperCommandManager;
import com.trynocs.gChallenges.commands.Challenges;
import com.trynocs.gChallenges.listener.ConnectionListener;
import com.trynocs.gChallenges.listener.InventoryListener;
import com.trynocs.gChallenges.listener.PauseListener;
import com.trynocs.gChallenges.listener.WalkListener;
import com.trynocs.gChallenges.utils.challenge.AmpelChanger;
import com.trynocs.gChallenges.utils.config.Configmanager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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

    @Override
    public void onEnable() {
        getLogger().info("Plugin wird aktiviert...");
        plugin = this;
        commandManager = new PaperCommandManager(this);
        configManager = new Configmanager(this);
        configManager.saveDefaultConfig();
        ampelChanger = new AmpelChanger(this);
        pluginManager = main.getPlugin().getServer().getPluginManager();
        loadConfigValues();
        registerCommands();
        pluginManager.registerEvents(new ConnectionListener(), this);
        pluginManager.registerEvents(new WalkListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new PauseListener(), this);
        Bukkit.getServer().setMotd(main.prefix + "Version: " + getDescription().getVersion() + "\n" + "§bBy Trynocs");

        if (main.getPlugin().getConfigManager().getCustomConfig("challenge").getString("ampel-challenge.enabled").equals("true")) {
            ampelChanger.setColor(main.getPlugin().getConfigManager().getCustomConfig("challenge").getString("ampel-challenge.color"));
            ampelChanger.startColorChangeTask();
        }

        getLogger().info("Plugin wurde aktiviert.");
    }

    private void registerCommands() {
        Challenges challenges = new Challenges();
        commandManager.registerCommand(challenges);
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

    public static String translateColors(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> translateColors(List<String> texts) {
        return texts.stream().map(main::translateColors).toList();
    }
}
