package com.trynocs.gChallenges.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.trynocs.gChallenges.main;
import com.trynocs.gChallenges.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("challenges")
@CommandPermission("trynocs.challenges")
public class Challenges extends BaseCommand {

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
            Inventory inventory = Bukkit.createInventory(null, 9*6, main.prefix + "§5Challenges Menu");
            for (int i= 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§7").build());
            }

            player.openInventory(inventory);
        }
    }
}
