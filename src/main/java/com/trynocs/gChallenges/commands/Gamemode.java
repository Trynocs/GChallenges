package com.trynocs.gChallenges.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.trynocs.gChallenges.main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gm|gamemode")
@CommandPermission("gchallenges.gamemode")
public class Gamemode extends BaseCommand {

    @Default
    @CommandCompletion("@gamemode @players")
    public void onGamemodeChange(CommandSender sender, @Single String gamemode, @Optional String target) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(main.beplayer);
            return;
        }

        Player targetPlayer = target == null || target.isEmpty() ? player : Bukkit.getPlayer(target);
        if (targetPlayer == null) {
            player.sendMessage(main.noplayer);
            return;
        }

        GameMode mode = parseGameMode(gamemode);
        if (mode == null) {
            player.sendMessage(main.prefix + "§cUngültiger Spielmodus angegeben. Nutze survival, creative, adventure oder spectator.");
            return;
        }

        targetPlayer.setGameMode(mode);
        String modeName = mode.name().toUpperCase();
        targetPlayer.sendMessage(main.prefix + "§aDu bist nun im Spielmodus §6" + translateGameMode(modeName) + "§a.");
        if (!player.equals(targetPlayer)) {
            player.sendMessage(main.prefix + "§aDer Spieler §6" + targetPlayer.getName() + " §aist nun im Spielmodus §6" + translateGameMode(modeName) + "§a.");
        }
    }

    private GameMode parseGameMode(String gamemode) {
        switch (gamemode.toLowerCase()) {
            case "0":
            case "survival":
                return GameMode.SURVIVAL;
            case "1":
            case "creative":
                return GameMode.CREATIVE;
            case "2":
            case "abenteuer":
            case "adventure":
                return GameMode.ADVENTURE;
            case "3":
            case "zuschauer":
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }

    private String translateGameMode(String modeName) {
        switch (modeName) {
            case "SURVIVAL":
                return "SURVIVAL";
            case "CREATIVE":
                return "KREATIV";
            case "ADVENTURE":
                return "ABENTEUER";
            case "SPECTATOR":
                return "ZUSCHAUER";
            default:
                return modeName;
        }
    }
}
