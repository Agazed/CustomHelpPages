package net.agazed.help;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {

	private static Help help;

	public Utils(Help help) {
		Utils.help = help;
	}

	public static List<String> getHelpList() {
		return help.getConfig().getStringList("help-messages");
	}

	public static int getSplitInterval() {
		return help.getConfig().getInt("messages-per-page");
	}

	public static int getTotalPages() {
		return (int) Math.ceil((double) getHelpList().size() / (double) getSplitInterval());
	}

	public static int getPossiblePages(int page) {
		return page * getSplitInterval();
	}

	public static boolean isInvalid(CommandSender sender, String arg) {
		int page;
		try {
			page = Integer.parseInt(arg);
		} catch (NumberFormatException e) {
			return true;
		}
		if (getHelpList().size() + getSplitInterval() <= getPossiblePages(page) || page < 1) {
			return true;
		}
		return false;
	}

	public static void reloadConfig(CommandSender sender) {
		if (!sender.hasPermission("help.reload")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}

		help.reloadConfig();
		sender.sendMessage(ChatColor.GREEN + "Successfully reloaded config!");
		return;
	}

	public static void sendHeader(CommandSender sender, int page) {
		if (help.getConfig().getBoolean("display-header")) {
			String header = ChatColor.translateAlternateColorCodes('&',
					help.getConfig().getString("header").replace("%CURRENTPAGE", Integer.toString(page)).replace("%TOTALPAGES", Integer.toString(getTotalPages())));
			sender.sendMessage(header);
		} else {
			return;
		}
	}

	public static void sendFooter(CommandSender sender, int page) {
		if (help.getConfig().getBoolean("display-footer")) {
			String footer = ChatColor.translateAlternateColorCodes('&',
					help.getConfig().getString("footer").replace("%CURRENTPAGE", Integer.toString(page)).replace("%TOTALPAGES", Integer.toString(getTotalPages())));
			sender.sendMessage(footer);
		} else {
			return;
		}
	}

	public static void getPage(CommandSender sender, int page) {

		String pm = "";

		if (getHelpList().size() <= getPossiblePages(page)) {
			int modulus = getPossiblePages(page) - getHelpList().size();
			List<String> sublist = getHelpList().subList(getPossiblePages(page) - getSplitInterval(), getPossiblePages(page) - modulus);

			for (int i = 0; i < sublist.size(); i++) {
				pm = ChatColor.translateAlternateColorCodes('&', sublist.get(i));
				sender.sendMessage(pm);
			}
			return;
		}

		List<String> sublist = getHelpList().subList(getPossiblePages(page) - getSplitInterval(), getPossiblePages(page));
		for (int i = 0; i < sublist.size(); i++) {
			pm = ChatColor.translateAlternateColorCodes('&', sublist.get(i));
			sender.sendMessage(pm);
		}
		return;
	}

}
