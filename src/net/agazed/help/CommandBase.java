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
				Utils.sendHeader(sender, 1);
				Utils.getPage(sender, 1);
				Utils.sendFooter(sender, 1);
				return true;
			}

			if (args.length == 1) {

				if (args[0].equalsIgnoreCase("reload")) {
					Utils.reloadConfig(sender);
					return true;
				}

				if (Utils.isInvalid(sender, args[0])) {
					sender.sendMessage(ChatColor.RED + "Invalid page!");
					return true;
				}

				int page = Integer.parseInt(args[0]);

				Utils.sendHeader(sender, page);
				Utils.getPage(sender, page);
				Utils.sendFooter(sender, page);
				return true;
			}
		}
		return true;
	}
}
