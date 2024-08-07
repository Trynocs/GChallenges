package com.trynocs.gChallenges.utils.config;

import com.trynocs.gChallenges.main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class LevelUpLocationManager {
    private FileConfiguration challenge = main.getPlugin().getConfigManager().getCustomConfig("challenge");
    public void setLocationConfig(String name, Location location) {
        challenge.set("level-border." + name + ".world", location.getWorld().getName());
        challenge.set("level-border." + name + ".x", location.getX());
        challenge.set("level-border." + name + ".y", location.getY());
        challenge.set("level-border." + name + ".z", location.getZ());
        challenge.set("level-border." + name + ".yaw", location.getYaw());
        challenge.set("level-border." + name + ".pitch", location.getPitch());
        main.getPlugin().getConfigManager().saveCustomConfig("challenge");
    }

    public Location getLocationConfig(String name) {
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

    public Location getLocationConfigByName(String worldName) {
        return getLocationConfig(worldName);
    }
}
