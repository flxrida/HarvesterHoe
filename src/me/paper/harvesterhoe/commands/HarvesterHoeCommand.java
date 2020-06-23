package me.paper.harvesterhoe.commands;

import me.paper.harvesterhoe.HarvesterHoe;
import me.paper.harvesterhoe.enchants.EnchantManager;
import me.paper.harvesterhoe.harvester.Harvester;
import me.paper.harvesterhoe.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HarvesterHoeCommand implements CommandExecutor, Color {

    private FileConfiguration config = HarvesterHoe.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender.hasPermission("hh.give")) {

            if (args.length == 2) {

                if (args[0].equalsIgnoreCase("give")) {

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        if (target.getInventory().firstEmpty() != -1) {
                            ItemStack harvester = new Harvester(target).getHarvester(0,0,0,0);
                            target.getInventory().addItem(harvester);
                            sender.sendMessage(format(config.getString("messages.give.success").replaceAll("%target%", target.getName())));
                            return true;
                        }
                    }else {
                        sender.sendMessage(format(config.getString("messages.no_player").replaceAll("%target%", args[1])));
                        return false;
                    }

                }else {
                    sender.sendMessage(format(config.getString("messages.give.usage")));
                    return false;
                }

            }else {
                sender.sendMessage(format(config.getString("messages.give.usage")));
                return false;
            }

        }else {
            sender.sendMessage(format(config.getString("messages.no_permission")));
            return false;
        }

        return false;
    }
}
