package net.agazed.help;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBase implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		String g = "groups.";
		String p = "pages.";

		if (cmd.getName().equalsIgnoreCase("help")) {

			if (!sender.hasPermission("help.help")) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return true;
			}

			if (args.length == 0) {
				Utils.sendList(sender, g + Utils.getListGroup(sender), 1);
				return true;
			}

			if (args.length == 1) {

				if (args[0].equalsIgnoreCase("reload")) {
					Utils.reloadConfig(sender);
					return true;
				}

				if (Utils.isNotAnInt(args[0])) {

					if (!sender.hasPermission("help.page." + args[0])) {
						sender.sendMessage(ChatColor.RED + "No permission!");
						return true;
					}

					if (Utils.getList(p + args[0]).isEmpty()) {
						sender.sendMessage(ChatColor.RED + "List is empty does not exist.");
						return true;
					}

					Utils.sendList(sender, p + args[0], 1);
					return true;
				}

				int page = Integer.parseInt(args[0]);

				if (Utils.isInvalid(g + Utils.getListGroup(sender), page)) {
					sender.sendMessage(ChatColor.RED + "Invalid page.");
					return true;
				}

				Utils.sendList(sender, g + Utils.getListGroup(sender), page);
				return true;
			}

			if (args.length == 2) {

				if (args[0].equalsIgnoreCase("view")) {

					if (!sender.hasPermission("help.view")) {
						sender.sendMessage(ChatColor.RED + "No permission!");
						return true;
					}

					if (Utils.getList(g + args[1]).isEmpty()) {
						sender.sendMessage(ChatColor.RED + "List is empty does not exist!");
						return true;
					}

					Utils.sendList(sender, g + args[1], 1);
					return true;
				}

				if (!sender.hasPermission("help.page." + args[0])) {
					sender.sendMessage(ChatColor.RED + "No permission!");
					return true;
				}

				if (Utils.getList(p + args[0]).isEmpty()) {
					sender.sendMessage(ChatColor.RED + "List is empty does not exist!");
					return true;
				}

				if (Utils.isNotAnInt(args[1])) {
					sender.sendMessage(ChatColor.RED + "Invalid page! Not an integer?");
					return true;
				}

				int page = Integer.parseInt(args[1]);

				if (Utils.isInvalid(p + args[0], page)) {
					sender.sendMessage(ChatColor.RED + "Invalid page!");
					return true;
				}

				Utils.sendList(sender, p + args[0], page);
				return true;
			}

			if (args.length == 3 && args[0].equalsIgnoreCase("view")) {

				if (!sender.hasPermission("help.view")) {
					sender.sendMessage(ChatColor.RED + "No permission!");
					return true;
				}

				if (Utils.getList(g + args[1]).isEmpty()) {
					sender.sendMessage(ChatColor.RED + "List is empty does not exist!");
					return true;
				}

				if (Utils.isNotAnInt(args[2])) {
					sender.sendMessage(ChatColor.RED + "Invalid page! Not an integer?");
					return true;
				}

				int page = Integer.parseInt(args[2]);

				if (Utils.isInvalid(g + args[1], page)) {
					sender.sendMessage(ChatColor.RED + "Invalid page!");
					return true;
				}

				Utils.sendList(sender, g + args[1], page);
				return true;

			}
			sender.sendMessage(ChatColor.RED + "Invalid arguments!");
			return true;
		}
		return true;
	}
}
