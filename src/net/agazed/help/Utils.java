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

	public static List<String> getList(String list) {
		return help.getConfig().getStringList(list + ".messages");
	}

	public static int getSplitInterval(String list) {
		return help.getConfig().getInt(list + ".messages-per-page");
	}

	public static int getTotalPages(String list) {
		return (int) Math.ceil((double) getList(list).size() / (double) getSplitInterval(list));
	}

	public static int getPossibleMessages(String list, int page) {
		return page * getSplitInterval(list);
	}

	public static String getListGroup(CommandSender sender) {
		String list = null;
		for (PermissionAttachmentInfo perm : sender.getEffectivePermissions()) {
			if (perm.getPermission().toString().startsWith("help.group.")) {
				if (list != null) {
					return "default";
				}
				list = perm.getPermission().toString().replace("help.group.", "");
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
		if (getList(list).size() + getSplitInterval(list) <= getPossibleMessages(list, page) || page < 1) {
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

	public static void sendHeader(CommandSender sender, String list, int page, String name) {
		if (help.getConfig().getBoolean(list + ".display-header")) {
			if (help.getConfig().getBoolean(list + ".show-header-on-last-page") == false && isInvalid(list, page + 1)) {
				return;
			}
			String header = ChatColor.translateAlternateColorCodes('&', help.getConfig().getString(list + ".header").replace("%CURRENTPAGE", Integer.toString(page)).replace("%TOTALPAGES", Integer.toString(getTotalPages(list))).replace("%NEXTPAGE", Integer.toString(page + 1)).replace("%LIST", name));
			sender.sendMessage(header);
		} else {
			return;
		}
	}

	public static void sendFooter(CommandSender sender, String list, int page, String name) {
		if (help.getConfig().getBoolean(list + ".display-footer")) {
			if (help.getConfig().getBoolean(list + ".show-footer-on-last-page") == false && isInvalid(list, page + 1)) {
				return;
			}
			String footer = ChatColor.translateAlternateColorCodes('&', help.getConfig().getString(list + ".footer").replace("%CURRENTPAGE", Integer.toString(page)).replace("%TOTALPAGES", Integer.toString(getTotalPages(list))).replace("%NEXTPAGE", Integer.toString(page + 1)).replace("%LIST", name));
			sender.sendMessage(footer);
		} else {
			return;
		}
	}

	public static void getPage(CommandSender sender, String list, int page) {

		String message = "";

		if (getList(list).size() <= getPossibleMessages(list, page)) {
			int modulus = getPossibleMessages(list, page) - getList(list).size();
			List<String> sublist = getList(list).subList(getPossibleMessages(list, page) - getSplitInterval(list), getPossibleMessages(list, page) - modulus);

			for (int i = 0; i < sublist.size(); i++) {
				message = ChatColor.translateAlternateColorCodes('&', sublist.get(i));
				sender.sendMessage(message);
			}
			return;
		}

		List<String> sublist = getList(list).subList(getPossibleMessages(list, page) - getSplitInterval(list), getPossibleMessages(list, page));
		for (int i = 0; i < sublist.size(); i++) {
			message = ChatColor.translateAlternateColorCodes('&', sublist.get(i));
			sender.sendMessage(message);
		}
		return;
	}

	public static void sendList(CommandSender sender, String list, String type, int page) {
		sendHeader(sender, type + list, page, list);
		getPage(sender, type + list, page);
		sendFooter(sender, type + list, page, list);
	}

}
