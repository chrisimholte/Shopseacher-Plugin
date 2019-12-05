package ktm6060.shopsearcher.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.Main;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.ui.ShopSearchMenuUI;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class ShopCommand implements CommandExecutor, TabExecutor {
	
	@SuppressWarnings("unused")
	private Main plugin;
	
	public ShopCommand(Main plugin) {
		this.plugin = plugin;
		
		plugin.getCommand("shop").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only a player can execute this command!");
			return true;
		}
		
		Player player = (Player) sender;
		String msg = null;
		ItemStack[] invItems = player.getInventory().getContents();
		ConfigManager plotConfig = Main.getPlotOwnersConfig();
		
		if (args.length == 0) {
			player.openInventory(ShopSearchMenuUI.GUI(player));
			ConfirmCommand.reset();
			return true;
		}
		else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("close")) {
				//check if player doesn't own a shop
				for (int i = 1; i <= plotConfig.getConfig().getInt("numFloors"); i++) {
					for (int j = 1; j <= plotConfig.getConfig().getInt("numPlots"); j++) {
						if (plotConfig.getConfig().getString("plots.floor" + i + ".plot" + j).equalsIgnoreCase(player.getDisplayName())) {
							player.sendMessage(Utils.chat("&6Are you sure you want to close your shop? Type /confirm to confirm"));
							ConfirmCommand.queueCmd(player.getDisplayName(), "close");
							return true;
						}
					}
				}
				
				player.sendMessage(Utils.chat("&CYou don't own a shop!"));
				ConfirmCommand.reset();
				return true;
			} else if (args[0].equalsIgnoreCase("price")) {
				msg = Utils.chat("&BShop Plot Prices:\nCorner:  16 Diamonds\nRegular: 32 Diamonds\nLarge:   48 Diamonds");
			} else
				msg = "&CInvalid command arguments.";
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("buy")) {
				if (Integer.parseInt(args[1]) < 1 || Integer.parseInt(args[1]) > plotConfig.getConfig().getInt("numFloors"))
					msg = "&CInvalid command arguments. Use /shop buy <floor> <plot>";
				else {
					if (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > plotConfig.getConfig().getInt("numPlots"))
						msg = "&CInvalid command arguments. Use /shop buy <floor> <plot>";
					else {
						//check if player already owns a shop
						for (int i = 1; i <= plotConfig.getConfig().getInt("numFloors"); i++) {
							for (int j = 1; j <= plotConfig.getConfig().getInt("numPlots"); j++) {
								if (plotConfig.getConfig().getString("plots.floor" + i + ".plot" + j).equalsIgnoreCase(player.getDisplayName())) {
									player.sendMessage(Utils.chat("&CYou already own a shop!"));
									ConfirmCommand.reset();
									return true;
								}
							}
						}
						
						//check if plot is available
						if (!plotConfig.getConfig().getString("plots.floor" + args[1] + ".plot" + args[2]).equals("")) {
							player.sendMessage(Utils.chat("&CSorry, but that plot is taken."));
							ConfirmCommand.reset();
							return true;
						}
						
						//confirm that player has enough diamonds in inv
						int plotPrice = Tools.plotPrice(Integer.parseInt(args[2]));
						int amountFound = 0;
						for (int i = 0; i < invItems.length; i++) {
							if (invItems[i] != null) {
								if (("" + invItems[i].getType()).equals("DIAMOND")) {
									amountFound += invItems[i].getAmount();
									if (amountFound >= plotPrice) break;
								}
							}
						}
						
						if (amountFound < plotPrice) {
							player.sendMessage(Utils.chat("&CYou do not have enough diamonds to purchase this plot. Please put at least " + plotPrice + " diamonds in your inventory." + amountFound));
							ConfirmCommand.reset();
							return true;
						}
						
						//buy shop
						player.sendMessage(Utils.chat("&6Are you sure you want to buy this shop for " + plotPrice + " diamonds? Type /confirm to confirm"));
						ConfirmCommand.queueCmd(player.getDisplayName(), "buy", Integer.parseInt(args[1]), Integer.parseInt(args[2]));
						return true;
					}
				}
			} else
				msg = "&CInvalid command arguments.";
		}
		else
			msg = "&CInvalid command arguments.";
			
		if (sender instanceof Player)
			player.sendMessage(Utils.chat(msg));
		else
			Bukkit.getConsoleSender().sendMessage(Utils.chat(msg));
		
		ConfirmCommand.reset();
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("shop")) {
			if (args.length == 1) {
				ArrayList<String> cmds = new ArrayList<String>();
				cmds.add("buy");
				cmds.add("close");
				cmds.add("price");
				Collections.sort(cmds);
				return cmds;
			}
		}
		return null;
	}

}
