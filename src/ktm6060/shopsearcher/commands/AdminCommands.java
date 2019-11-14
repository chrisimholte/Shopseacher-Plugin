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

public class AdminCommands implements CommandExecutor, TabExecutor {

	private Main plugin;
	
	public AdminCommands(Main plugin) {
		this.plugin = plugin;
		
		plugin.getCommand("shopsearcher").setExecutor(this);
		plugin.getCommand("ss").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ConfirmCommand.reset();
		Player player = (Player) sender;
		String msg;
		ConfigManager plotConfig = Main.getPlotOwnersConfig();
		
		//----- Admin Commands -----
		
		if (!sender.hasPermission("shopsearcher.admin")) {
			player.sendMessage(Utils.chat("&CYou do not have permission to execute this command."));
			return true;
		}
		
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				plugin.reloadConfig();
				plugin.saveConfig();
				plotConfig.reloadConfig();
				plotConfig.saveConfig();
				msg = "&AShopSearcher plugin reloaded!";
				if (sender instanceof Player)
					player.sendMessage(Utils.chat(msg));
				else
					Bukkit.getConsoleSender().sendMessage(Utils.chat(msg));
			}
			else if (args[0].equalsIgnoreCase("enderChest") || args[0].equalsIgnoreCase("eChest")) {
				msg = "Plot diamonds are being sent to " + plotConfig.getConfig().getString("enderChest") + "'s ender chest.";
				if (sender instanceof Player)
					player.sendMessage(Utils.chat(msg));
				else
					Bukkit.getConsoleSender().sendMessage(Utils.chat(msg));
			}
		}
		else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("setOwnerColor")) {
				plotConfig.getConfig().set("ownerTextColor", args[1]);
				plotConfig.saveConfig();
				plotConfig.reloadConfig();
				msg = "Successfully change owner text " + args[1] + "color.";
				if (sender instanceof Player)
					player.sendMessage(Utils.chat(msg));
				else
					Bukkit.getConsoleSender().sendMessage(Utils.chat(msg));
			}
			else if (args[0].equalsIgnoreCase("setEnderChest") || args[0].equalsIgnoreCase("setEChest")) {
				plotConfig.getConfig().set("enderChest", args[1]);
				plotConfig.saveConfig();
				plotConfig.reloadConfig();
				msg = "Plot diamonds will now be sent to " + args[1] + "'s ender chest.";
				if (sender instanceof Player)
					player.sendMessage(Utils.chat(msg));
				else
					Bukkit.getConsoleSender().sendMessage(Utils.chat(msg));
			}
		}
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("shopsearcher") || cmd.getName().equalsIgnoreCase("ss")) {
			if (args.length == 1) {
				ArrayList<String> cmds = new ArrayList<String>();
				cmds.add("reload");
				cmds.add("setOwnerColor");
				Collections.sort(cmds);
				return cmds;
			}
		}
		return null;
	}	
}
