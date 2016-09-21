package net.agazed.help;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBase implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("help")) {

			if (!sender.hasPermission("help.help")) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return true;
			}

			if (args.length == 0) {
				Utils.sendHeader(sender, Utils.getWhichList(sender), 1);
				Utils.getPage(sender, Utils.getWhichList(sender), 1);
				Utils.sendFooter(sender, Utils.getWhichList(sender), 1);
				return true;
			}

			if (args.length == 1) {

				if (args[0].equalsIgnoreCase("reload")) {
					Utils.reloadConfig(sender);
					return true;
				}

				if (Utils.isNotAnInt(args[0])) {
					sender.sendMessage(ChatColor.RED + "Invalid page!");
					return true;
				}

				int page = Integer.parseInt(args[0]);

				if (Utils.isInvalid(sender, Utils.getWhichList(sender), page)) {
					sender.sendMessage(ChatColor.RED + "Invalid page!");
					return true;
				}

				Utils.sendHeader(sender, Utils.getWhichList(sender), page);
				Utils.getPage(sender, Utils.getWhichList(sender), page);
				Utils.sendFooter(sender, Utils.getWhichList(sender), page);
				return true;
			}

			if (args.length == 2 && args[0].equalsIgnoreCase("view")) {

				if (!sender.hasPermission("help.view")) {
					sender.sendMessage(ChatColor.RED + "No permission!");
					return true;
				}

				if (Utils.getHelpList(args[1]).isEmpty()) {
					sender.sendMessage(ChatColor.RED + "List does not exist!");
					return true;
				}

				Utils.sendHeader(sender, args[1], 1);
				Utils.getPage(sender, args[1], 1);
				Utils.sendFooter(sender, args[1], 1);
				return true;
			}

			if (args.length == 3 && args[0].equalsIgnoreCase("view")) {

				if (!sender.hasPermission("help.view")) {
					sender.sendMessage(ChatColor.RED + "No permission!");
					return true;
				}

				if (Utils.getHelpList(args[1]).isEmpty()) {
					sender.sendMessage(ChatColor.RED + "List does not exist!");
					return true;
				}

				if (Utils.isNotAnInt(args[2])) {
					sender.sendMessage(ChatColor.RED + "Invalid page!");
					return true;
				}

				int page = Integer.parseInt(args[2]);

				if (Utils.isInvalid(sender, args[1], page)) {
					sender.sendMessage(ChatColor.RED + "Invalid page!");
					return true;
				}

				Utils.sendHeader(sender, args[1], page);
				Utils.getPage(sender, args[1], page);
				Utils.sendFooter(sender, args[1], page);
				return true;
			}
			sender.sendMessage(ChatColor.RED + "Invalid argument(s)!");
			return true;
		}
		return true;
	}
}
