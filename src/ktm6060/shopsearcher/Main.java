package ktm6060.shopsearcher;

import org.bukkit.plugin.java.JavaPlugin;

import ktm6060.shopsearcher.commands.AdminCommands;
import ktm6060.shopsearcher.commands.ConfirmCommand;
import ktm6060.shopsearcher.commands.FloorCommand;
import ktm6060.shopsearcher.commands.MyShopCommand;
import ktm6060.shopsearcher.commands.PlotCommands;
import ktm6060.shopsearcher.commands.ShopCommand;
import ktm6060.shopsearcher.listeners.InventoryClickListener;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.ui.ItemSearchUI;
import ktm6060.shopsearcher.ui.MyShopUI;
import ktm6060.shopsearcher.ui.PlotSearchUI;
import ktm6060.shopsearcher.ui.PlotUI;
import ktm6060.shopsearcher.ui.ShopSearchMenuUI;

public class Main extends JavaPlugin {
	
	private static ConfigManager configManager;
	
	@Override
	public void onEnable() {
		registerCommands();
		registerListeners();
		registerUI();
		loadConfigManager();
	}
	
	public void registerCommands() {
		new ShopCommand(this);
		new AdminCommands(this);
		new PlotCommands(this);
		new FloorCommand(this);
		new MyShopCommand(this);
		new ConfirmCommand(this);
	}
	
	public void registerListeners() {
		new InventoryClickListener(this);
	}
	
	public void registerUI() {
		ShopSearchMenuUI.initialize();
		PlotSearchUI.initialize();
		ItemSearchUI.initialize();
		MyShopUI.initialize();
		PlotUI.initialize();
	}

	public void loadConfigManager() {
		//Setup standard configuration
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		configManager = new ConfigManager();
		configManager.setupPlots();
		createPlotOwnersConfig();
		configManager.getConfig().options().copyDefaults(true);
		configManager.saveConfig();
	}
	
	public void createPlotOwnersConfig() {
		int floors = getConfig().getInt("plots.numFloors");
		int plots = getConfig().getInt("plots.numPlots");
		
		configManager.getConfig().addDefault("enderChest", "ktm6060");
		configManager.getConfig().addDefault("numFloors", floors);
		configManager.getConfig().addDefault("numPlots", plots);
		configManager.getConfig().addDefault("ownerTextColor", "&5");
		
		for (int i = 1; i <= floors; i++) {
			for (int j = 1; j <= plots; j++) {
				configManager.getConfig().addDefault("plots.floor" + i + ".plot" + j, "");
			}
		}
	}
	
	public static ConfigManager getPlotOwnersConfig() {
		return configManager;
	}	
}
