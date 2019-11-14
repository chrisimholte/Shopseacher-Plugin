package ktm6060.shopsearcher.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import ktm6060.shopsearcher.Main;

public class ConfigManager {

	private Main plugin = Main.getPlugin(Main.class);
	
	//Files
	public static FileConfiguration plotConfig;
	public static File plotOwnersFile;
	
	public void setupPlots() {
		//create plugin directory
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		
		plotOwnersFile = new File(plugin.getDataFolder(), "plotOwners.yml");
		
		if (!plotOwnersFile.exists()) {
			try {
				plotOwnersFile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage("[ShopSearcher] Could not create plotOwners.yml file.");
			}
		}
		
		plotConfig = YamlConfiguration.loadConfiguration(plotOwnersFile);
		Bukkit.getServer().getConsoleSender().sendMessage("[ShopSearcher] plotOwners.yml file created successfully.");
	}
	
	public FileConfiguration getConfig() {
		return plotConfig;
	}
	
	public void saveConfig() {
		try {
			plotConfig.save(plotOwnersFile);
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("[ShopSearcher] Could not save plotOwners.yml file.");
		}
	}
	
	public void reloadConfig() {
		plotConfig = YamlConfiguration.loadConfiguration(plotOwnersFile);
	}
}
