package ktm6060.shopsearcher.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ktm6060.shopsearcher.Main;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class ConfirmCommand implements CommandExecutor {
	
	private static boolean confirm = false;
	private static String playerName, cmdString;
	private static int floor, plot;
	
	@SuppressWarnings("unused")
	private Main plugin;
	
	public ConfirmCommand(Main plugin) {
		this.plugin = plugin;
		
		plugin.getCommand("confirm").setExecutor(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		ItemStack[] invItems = player.getInventory().getContents();
		ConfigManager plotConfig = Main.getPlotOwnersConfig();
		ItemMeta diamondMeta = null;
		
		if (!confirm || !playerName.equalsIgnoreCase(player.getDisplayName())) return true;
		
		if (cmdString.equals("buy")) {
			//find and take diamonds from player's inventory
			int amountTaken = 0;
			for (int i = 0; i < invItems.length; i++) {
				if (invItems[i] != null) {
					if (("" + invItems[i].getType()).equals("DIAMOND")) {
						
						//take diamonds
						if (invItems[i].getAmount() <= Tools.plotPrice(plot)-amountTaken) {
							player.getInventory().setItem(i, null);
							amountTaken += invItems[i].getAmount();
						} else {
							//Utils.createItem(player.getInventory(), "DIAMOND", invItems[i].getAmount()-(Tools.plotPrice(plot)-amountTaken), i, "&FDiamond");
							diamondMeta = invItems[i].getItemMeta();
							Utils.displayItem(player.getInventory(), "DIAMOND", invItems[i].getAmount()-(Tools.plotPrice(plot)-amountTaken), i, diamondMeta);
							amountTaken = Tools.plotPrice(plot);
						}
						
						if (amountTaken >= Tools.plotPrice(plot)) break;
					}
				}
			}
			
			//Get ender chest contents
			Player p = Bukkit.getServer().getPlayer(plotConfig.getConfig().getString("enderChest"));
			if (p != null)
				p = player;
			else
				p = Bukkit.getOfflinePlayer(plotConfig.getConfig().getString("enderChest")).getPlayer();
			invItems = p.getEnderChest().getContents();
			
			//Add diamonds to ender chest
			int amountToAdd = Tools.plotPrice(plot);
			for (int i = 0; i < invItems.length; i++) {
				if (invItems[i] == null) {
					Utils.displayItem(p.getEnderChest(), "DIAMOND", amountToAdd, i, diamondMeta);
					break;
				} else if (("" + invItems[i].getType()).equals("DIAMOND")) {
					if (invItems[i].getAmount()+amountToAdd < 64) {
						Utils.displayItem(p.getEnderChest(), "DIAMOND", invItems[i].getAmount()+amountToAdd, i, diamondMeta);
						break;
					} else if (invItems[i].getAmount() < 64) {
						 amountToAdd -= 64 - invItems[i].getAmount();
						 Utils.displayItem(p.getEnderChest(), "DIAMOND", 64, i, diamondMeta);
					}
				}
				if (amountToAdd <= 0) break;
			}
			
			//Update plotOwners config with new shop owner
			plotConfig.getConfig().set("plots.floor" + floor + ".plot" + plot, player.getDisplayName());
			plotConfig.saveConfig();
			plotConfig.reloadConfig();
			player.sendMessage(Utils.chat("&AShop successfully bought. You are plot " + plot + " on floor " + floor + "."));
			
		} else if (cmdString.equals("close")) {
			for (int i = 1; i <= plotConfig.getConfig().getInt("numFloors"); i++) {
				for (int j = 1; j <= plotConfig.getConfig().getInt("numPlots"); j++) {
					if (plotConfig.getConfig().getString("plots.floor" + i + ".plot" + j).equalsIgnoreCase(playerName)) {
						plotConfig.getConfig().set("plots.floor" + i + ".plot" + j, "");
						plotConfig.saveConfig();
						plotConfig.reloadConfig();
						player.sendMessage(Utils.chat("&AShop successfully closed."));
						break;
					}
				}
			}
		}
		
		reset();
		return false;
	}

	public static void queueCmd(String name, String command) {
		confirm = true;
		playerName = name;
		cmdString = command;
	}
	
	public static void queueCmd(String name, String command, int f, int p) {
		confirm = true;
		playerName = name;
		cmdString = command;
		floor = f;
		plot = p;
	}
	
	public static void reset() {
		confirm = false;
		playerName = null;
		cmdString = null;
		floor = 0;
		plot = 0;
	}
}
