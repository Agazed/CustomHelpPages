package net.agazed.help;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public class Help extends JavaPlugin {

	public void onEnable() {
		saveDefaultConfig();

		new Utils(this);

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			getLogger().info("Failed to submit metrics!");
		}

		getCommand("help").setExecutor(new CommandBase());
	}
}
