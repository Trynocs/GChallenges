package com.trynocs.gChallenges.listener;

import com.trynocs.gChallenges.main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class LevelUpListener implements Listener {
    private FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
    private Location location_nether;
    World world = Bukkit.getWorld("world");
    World world_nether = Bukkit.getWorld("world_nether");
    World world_the_end = Bukkit.getWorld("world_the_end");

    public WorldBorder worldBorder = (world != null) ? world.getWorldBorder() : null;
    public WorldBorder worldBorder_nether = (world_nether != null) ? world_nether.getWorldBorder() : null;
    public WorldBorder worldBorder_the_end = (world_the_end != null) ? world_the_end.getWorldBorder() : null;
    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        if (challenge.getString("level-border.enabled").equals("true")) {
            if (event.getAdvancement().getKey().getKey().equals("story/enter_the_nether")) {
                if (worldBorder_nether != null) {
                    setLocationConfig("world_nether", event.getPlayer().getLocation());
                    worldBorder_nether.setCenter(event.getPlayer().getLocation());
                    main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                } else {
                    event.getPlayer().sendMessage(main.prefix + "§cThe Nether world is not loaded.");
                }
            }
        }
    }

    @EventHandler
    public void onLevelUp(PlayerLevelChangeEvent event) {
        if (challenge.getString("level-border.enabled").equals("true")) {
            if (event.getOldLevel() < event.getNewLevel() && challenge.getString("level-border.enabled").equals("true") && challenge.getString("timer.paused").equals("false")) {
                World world = Bukkit.getWorld("world");
                World world_nether = Bukkit.getWorld("world_nether");
                World world_the_end = Bukkit.getWorld("world_the_end");

                if (world != null && worldBorder != null && worldBorder_nether != null && worldBorder_the_end != null) {
                    event.getPlayer().sendMessage(main.prefix + "§aDu bist nun Level " + event.getNewLevel() + "!");
                    event.getPlayer().sendMessage(main.prefix + "§aDie Border wird um 1 Block erweitert.");
                    challenge.set("level-border.blocks", challenge.getInt("level-border.blocks") + 1);
                    main.getPlugin().getConfigManager().saveCustomConfig("challenge");
                    if (!challenge.isSet("level-border.world_nether")) {
                        location_nether = world_nether.getSpawnLocation();
                    }
                    Location location = world.getSpawnLocation();
                    Location location_the_end = new Location(world_the_end, 100.5, 49, 0.5, 90, 0);
                    setLocationConfig("world", location);
                    setLocationConfig("world_nether", location_nether);
                    setLocationConfig("world_the_end", location_the_end);
                    worldBorder.setCenter(getLocationConfig("world"));
                    worldBorder_nether.setCenter(getLocationConfig("world_nether"));
                    worldBorder_the_end.setCenter(getLocationConfig("world_the_end"));

                    worldBorder.setSize(challenge.getInt("level-border.blocks"));
                    worldBorder_nether.setSize(challenge.getInt("level-border.blocks"));
                    worldBorder_the_end.setSize(challenge.getInt("level-border.blocks"));
                } else {
                    event.getPlayer().sendMessage(main.prefix + "§cOne or more worlds are not loaded.");
                }
            }
        }
    }

    private void setLocationConfig(String name, Location location) {
        challenge.set("level-border." + name + ".world", location.getWorld().getName());
        challenge.set("level-border." + name + ".x", location.getX());
        challenge.set("level-border." + name + ".y", location.getY());
        challenge.set("level-border." + name + ".z", location.getZ());
        challenge.set("level-border." + name + ".yaw", location.getYaw());
        challenge.set("level-border." + name + ".pitch", location.getPitch());
        main.getPlugin().getConfigManager().saveCustomConfig("challenge");
    }

    private Location getLocationConfig(String name) {
        String worldName = challenge.getString("level-border." + name + ".world");
        if (worldName == null) {
            Bukkit.getLogger().severe("World name is null for " + name);
            return null;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().severe("World not found: " + worldName);
            return null;
        }

        double x = challenge.getDouble("level-border." + name + ".x");
        double y = challenge.getDouble("level-border." + name + ".y");
        double z = challenge.getDouble("level-border." + name + ".z");
        float yaw = (float) challenge.getDouble("level-border." + name + ".yaw");
        float pitch = (float) challenge.getDouble("level-border." + name + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }
}
