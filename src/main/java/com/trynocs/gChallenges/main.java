package com.trynocs.gChallenges;

import co.aikar.commands.PaperCommandManager;
import com.trynocs.gChallenges.commands.Challenges;
import com.trynocs.gChallenges.listener.ConnectionListener;
import com.trynocs.gChallenges.listener.InventoryListener;
import com.trynocs.gChallenges.listener.WalkListener;
import com.trynocs.gChallenges.utils.config.Configmanager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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

    @Override
    public void onEnable() {
        getLogger().info("Plugin wird aktiviert...");
        plugin = this;

        commandManager = new PaperCommandManager(this);
        configManager = new Configmanager(this);
        configManager.saveDefaultConfig();
        pluginManager = main.getPlugin().getServer().getPluginManager();
        loadConfigValues();
        registerCommands();
        pluginManager.registerEvents(new ConnectionListener(), this);
        pluginManager.registerEvents(new WalkListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        Bukkit.getServer().setMotd(main.prefix + "Version: " + getDescription().getVersion() + "\n" + "§bBy Trynocs");

        getLogger().info("Plugin wurde aktiviert.");
    }

    private void registerCommands() {
        Challenges challenges = new Challenges();
        commandManager.registerCommand(challenges);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin wurde deaktiviert");
    }

    private void loadConfigValues() {
        String prefix2 = configManager.getConfig().getString("messages.prefix", "&b&lBlockEngine &8» &7");
        String noperm2 = configManager.getConfig().getString("messages.no-perm", "Dazu hast du keine Rechte.");
        String beplayer2 = configManager.getConfig().getString("messages.not-player", "Du musst ein Spieler sein um diesen Command auszuführen.");
        String noplayer2 = configManager.getConfig().getString("messages.no-player", "Dieser Spieler ist offline oder existiert nicht.");
        String discord2 = configManager.getConfig().getString("messages.discord", "DEIN INVITE LINK");
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
