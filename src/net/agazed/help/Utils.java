package net.agazed.help;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Utils {

	private static Help help;

	public Utils(Help help) {
		Utils.help = help;
	}

	public static List<String> getHelpList(String list) {
		return help.getConfig().getStringList(list);
	}

	public static int getSplitInterval() {
		return help.getConfig().getInt("messages-per-page");
	}

	public static int getTotalPages(String list) {
		return (int) Math.ceil((double) getHelpList(list).size() / (double) getSplitInterval());
	}

	public static int getPossibleMessages(int page) {
		return page * getSplitInterval();
	}

	public static String getWhichList(CommandSender sender) {
		String list = null;
		for (PermissionAttachmentInfo perm : sender.getEffectivePermissions()) {
			if (perm.getPermission().toString().startsWith("help.list.")) {
				if (list != null) {
					return "default";
				}
				list = perm.getPermission().toString().replace("help.list.", "");
				if (getHelpList(list).isEmpty()) {
					return "default";
				}
			}
		}
		if (list == null) {
			return "default";
		}
		return list;
	}

	public static boolean isNotAnInt(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return true;
		}
		return false;
	}

	public static boolean isInvalid(String list, int page) {
		if (getHelpList(list).size() + getSplitInterval() <= getPossibleMessages(page) || page < 1) {
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

	public static void sendHeader(CommandSender sender, String list, int page) {
		if (help.getConfig().getBoolean("display-header")) {
			if (help.getConfig().getBoolean("show-header-on-last-page") == false && isInvalid(list, page + 1)) {
				return;
			}
			String header = ChatColor.translateAlternateColorCodes('&', help.getConfig().getString("header").replace("%CURRENTPAGE", Integer.toString(page)).replace("%TOTALPAGES", Integer.toString(getTotalPages(list))).replace("%NEXTPAGE", Integer.toString(page + 1)));
			sender.sendMessage(header);
		} else {
			return;
		}
	}

	public static void sendFooter(CommandSender sender, String list, int page) {
		if (help.getConfig().getBoolean("display-footer")) {
			if (help.getConfig().getBoolean("show-footer-on-last-page") == false && isInvalid(list, page + 1)) {
				return;
			}
			String footer = ChatColor.translateAlternateColorCodes('&', help.getConfig().getString("footer").replace("%CURRENTPAGE", Integer.toString(page)).replace("%TOTALPAGES", Integer.toString(getTotalPages(list))).replace("%NEXTPAGE", Integer.toString(page + 1)));
			sender.sendMessage(footer);
		} else {
			return;
		}
	}

	public static void getPage(CommandSender sender, String list, int page) {

		String message = "";

		if (getHelpList(list).size() <= getPossibleMessages(page)) {
			int modulus = getPossibleMessages(page) - getHelpList(list).size();
			List<String> sublist = getHelpList(list).subList(getPossibleMessages(page) - getSplitInterval(), getPossibleMessages(page) - modulus);

			for (int i = 0; i < sublist.size(); i++) {
				message = ChatColor.translateAlternateColorCodes('&', sublist.get(i));
				sender.sendMessage(message);
			}
			return;
		}

		List<String> sublist = getHelpList(list).subList(getPossibleMessages(page) - getSplitInterval(), getPossibleMessages(page));
		for (int i = 0; i < sublist.size(); i++) {
			message = ChatColor.translateAlternateColorCodes('&', sublist.get(i));
			sender.sendMessage(message);
		}
		return;
	}

}
