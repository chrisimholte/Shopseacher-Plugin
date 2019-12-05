package ktm6060.shopsearcher.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ktm6060.shopsearcher.Main;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.ui.MyShopUI;
import ktm6060.shopsearcher.utils.Utils;

public class MyShopCommand implements CommandExecutor {

	@SuppressWarnings("unused")
	private Main plugin;
	
	public MyShopCommand(Main plugin) {
		this.plugin = plugin;
		
		plugin.getCommand("myshop").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ConfirmCommand.reset();
		Player player = (Player) sender;
		ConfigManager plotConfig = Main.getPlotOwnersConfig();
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only a player can execute this command!");
			return true;
		}
		
		for (int i = 1; i <= plotConfig.getConfig().getInt("numFloors"); i++) {
			for (int j = 1; j <= plotConfig.getConfig().getInt("numPlots"); j++) {
				if (plotConfig.getConfig().getString("plots.floor" + i + ".plot" + j).equalsIgnoreCase(player.getDisplayName()) || plotConfig.getConfig().getString("plots.floor" + i + ".plot" + j).equals(player.getName())) {
					MyShopUI.setCurrPage(1);
					player.openInventory(MyShopUI.GUI(player));
					return true;
				}
			}
		}
		player.sendMessage(Utils.chat("&CYou do not own a shop plot."));
		return false;
	}
	
}