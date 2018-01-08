package net.agazed.help;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Utils {

	private static Help help;

	public static List<String> getList(String list) {
		return help.getConfig().getStringList(list + ".messages");
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

	public static int getMaxMessages(String list, int page) {
		return page * getSplit(list);
	}

	public static void getPage(CommandSender sender, String list, int page) {

		String message = "";

		if (getList(list).size() <= getMaxMessages(list, page)) {
			int remainder = getMaxMessages(list, page) - getList(list).size();
			List<String> sublist = getList(list).subList(getMaxMessages(list, page) - getSplit(list), getMaxMessages(list, page) - remainder);

			for (int i = 0; i < sublist.size(); i++) {
				message = ChatColor.translateAlternateColorCodes('&', sublist.get(i));
				sender.sendMessage(message);
			}
			return;
		}

		List<String> sublist = getList(list).subList(getMaxMessages(list, page) - getSplit(list), getMaxMessages(list, page));
		for (int i = 0; i < sublist.size(); i++) {
			message = ChatColor.translateAlternateColorCodes('&', sublist.get(i));
			sender.sendMessage(message);
		}
		return;
	}

	public static int getSplit(String list) {
		return help.getConfig().getInt(list + ".messages-per-page");
	}

	public static int getTotalPages(String list) {
		return (int) Math.ceil((double) getList(list).size() / (double) getSplit(list));
	}

	public static boolean isInvalid(String list, int page) {
		if (getList(list).size() + getSplit(list) <= getMaxMessages(list, page) || page < 1) {
			return true;
		}
		return false;
	}

	public static boolean isNotAnInt(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
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

	public static void sendCaption(CommandSender sender, String list, int page, String type) {
		if (help.getConfig().getBoolean(list + ".display-" + type)) {
			if (help.getConfig().getBoolean(list + ".show-" + type + "-on-last-page") == false && isInvalid(list, page + 1)) {
				return;
			}
			String caption = ChatColor.translateAlternateColorCodes('&', help.getConfig().getString(list + "." + type).replace("%CURRENTPAGE", Integer.toString(page)).replace("%TOTALPAGES", Integer.toString(getTotalPages(list))).replace("%NEXTPAGE", Integer.toString(page + 1)));
			sender.sendMessage(caption);
		} else {
			return;
		}
	}

	public static void sendList(CommandSender sender, String list, int page) {
		sendCaption(sender, list, page, "header");
		getPage(sender, list, page);
		sendCaption(sender, list, page, "footer");
	}

	public Utils(Help help) {
		Utils.help = help;
	}

}
