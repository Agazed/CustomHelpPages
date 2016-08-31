package net.agazed.help;

import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Help extends JavaPlugin {

	public void onEnable() {
		saveDefaultConfig();
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			getLogger().info("Failed to submit metrics!");
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (!sender.hasPermission("help.help")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("help")) {
			if (args.length == 0) {
				getPage(sender, 1);
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("help.reload")) {
					sender.sendMessage(ChatColor.RED + "No permission!");
					return true;
				}
				this.reloadConfig();
				sender.sendMessage(ChatColor.GREEN
						+ "Successfully reloaded config!");
				return true;
			}
			if (args.length == 1) {
				int page;
				try {
					page = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "Invalid page!");
					return true;
				}
				getPage(sender, page);
				return true;
			}
		}
		return true;
	}

	public void getPage(CommandSender sender, int page) {
		List<String> helpmessages = getConfig().getStringList("help-messages");
		int messages = helpmessages.size();
		int messagesperpage = getConfig().getInt("messages-per-page");
		int pages = (int) Math.ceil((double) messages
				/ (double) messagesperpage);
		String message = "";
		int fullpage = page * messagesperpage;
		if (messages + messagesperpage <= fullpage || page < 1) {
			sender.sendMessage(ChatColor.RED + "Invalid page!");
			return;
		}
		String toppagecounter = ChatColor.translateAlternateColorCodes(
				'&',
				getConfig().getString("top-page-counter")
						.replace("%CURRENTPAGE", Integer.toString(page))
						.replace("%PAGES", Integer.toString(pages)));
		String bottompagecounter = ChatColor.translateAlternateColorCodes(
				'&',
				getConfig().getString("bottom-page-counter")
						.replace("%CURRENTPAGE", Integer.toString(page))
						.replace("%PAGES", Integer.toString(pages)));

		if (getConfig().getBoolean("display-top-page-counter") == true) {
			sender.sendMessage(toppagecounter);
		}
		if (messages <= fullpage) {
			int modulus = fullpage - messages;
			List<String> pagemessages = helpmessages.subList(fullpage
					- messagesperpage, fullpage - modulus);
			for (int messagenumber = 0; messagenumber < (messagesperpage - modulus); messagenumber++) {
				message = ChatColor.translateAlternateColorCodes('&',
						pagemessages.get(messagenumber));
				sender.sendMessage(message);
			}
			if (getConfig().getBoolean("display-bottom-page-counter") == true) {
				sender.sendMessage(bottompagecounter);
			}
			return;
		}
		List<String> pagemessages = helpmessages.subList(fullpage
				- messagesperpage, fullpage);
		for (int messagenumber = 0; messagenumber < messagesperpage; messagenumber++) {
			message = ChatColor.translateAlternateColorCodes('&',
					pagemessages.get(messagenumber));
			sender.sendMessage(message);
		}
		if (getConfig().getBoolean("display-bottom-page-counter") == true) {
			sender.sendMessage(bottompagecounter);
		}
		return;
	}

}
