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

import ktm6060.shopsearcher.Main;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.utils.Utils;

public class PlotCommands implements CommandExecutor, TabExecutor {
	
	@SuppressWarnings("unused")
	private Main plugin;
	
	public PlotCommands(Main plugin) {
		this.plugin = plugin;
		
		plugin.getCommand("plot").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ConfirmCommand.reset();
		Player player = (Player) sender;
		String msg;
		ConfigManager plotConfig = Main.getPlotOwnersConfig();
		
		if (!sender.hasPermission("shopsearcher.staff")) {
			player.sendMessage(Utils.chat("&CYou do not have permission to execute this command."));
			return true;
		}
		
		
		if (args.length == 4) {				// Command: /plot set <floor> <plot> <owner>
			if (args[0].equalsIgnoreCase("set")) {
				plotConfig.getConfig().set("plots.floor" + args[1] + ".plot" + args[2], args[3]);
				plotConfig.saveConfig();
				plotConfig.reloadConfig();
				msg = "%ASuccessfully set " + args[3] + " as owner of plot " + args[2] + " on floor " + args[1];
			} else {
				msg = "&CInvalid command arguments.";
			}
		} else if (args.length == 3) {		// Command: /plot clear <floor> <plot>
			if (args[0].equalsIgnoreCase("clear")) {
				if (Integer.parseInt(args[1]) < 1 || Integer.parseInt(args[1]) > plotConfig.getConfig().getInt("numFloors"))
					msg = "&CInvalid command arguments.";
				else {
					if (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > plotConfig.getConfig().getInt("numPlots"))
						msg = "&CInvalid command arguments.";
					else {
						plotConfig.getConfig().set("plots.floor" + args[1] + ".plot" + args[2], "");
						plotConfig.saveConfig();
						plotConfig.reloadConfig();
						msg = "%ASuccessfully cleared plot " + args[2] + " on floor " + args[1];
					}
				}
			} else {
				msg = "&CInvalid command arguments.";
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("clear")) {
				if (args[1].equalsIgnoreCase("all")) {		// Command: /plot clear all (admins only)
					if (player.hasPermission("shopsearcher.admin") || player.isOp()) {
						for (int i = 1; i <= plotConfig.getConfig().getInt("numFloors"); i++) {
							for (int j = 1; j <= plotConfig.getConfig().getInt("numPlots"); j++) {
								plotConfig.getConfig().set("plots.floor" + i + ".plot" + j, "");
								plotConfig.saveConfig();
								plotConfig.reloadConfig();
							}
						}
						msg = "%ASuccessfully cleared all plots";
					} else
						msg = "&COnly admins can perform this command.";
					
				} else {							// Command: /plot clear <floor>
					if (Integer.parseInt(args[1]) < 1 || Integer.parseInt(args[1]) > plotConfig.getConfig().getInt("numFloors"))
						msg = "&CInvalid command arguments.";
					else {
						for (int i = 1; i <= plotConfig.getConfig().getInt("numPlots"); i++) {
							plotConfig.getConfig().set("plots.floor" + args[1] + ".plot" + i, "");
							plotConfig.saveConfig();
							plotConfig.reloadConfig();
						}
						msg = "%ASuccessfully cleared all plots on floor " + args[1];
					}
				}
			} else {
				msg = "&CInvalid command arguments.";
			}
		} else {
			msg = "&4Please enter all values. /plot set <floor> <plot> <owner>";
		}
		
		if (sender instanceof Player)
			player.sendMessage(Utils.chat(msg));
		else
			Bukkit.getConsoleSender().sendMessage(Utils.chat(msg));
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("plot")) {
			if (args.length == 1) {
				ArrayList<String> cmds = new ArrayList<String>();
				cmds.add("clear");
				cmds.add("set");
				Collections.sort(cmds);
				return cmds;
			}
		}
		return null;
	}
}
