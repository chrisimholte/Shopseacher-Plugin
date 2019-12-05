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

public class FloorCommand implements CommandExecutor, TabExecutor {

	@SuppressWarnings("unused")
	private Main plugin;
	
	public FloorCommand(Main plugin) {
		this.plugin = plugin;
		
		plugin.getCommand("floor").setExecutor(this);
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
		
		if (args.length == 1) {
			msg = "Floor " + args[0] + " Shop Owners:";
			
			for (int i = 1; i <= plotConfig.getConfig().getInt("numPlots"); i++)
				msg += "\nPlot " + i + ": " + plotConfig.getConfig().getString("plots.floor" + args[0] + ".plot" + i);
			
			if (sender instanceof Player)
				player.sendMessage(Utils.chat(msg));
			else
				Bukkit.getConsoleSender().sendMessage(Utils.chat(msg));
		} else
			Bukkit.getConsoleSender().sendMessage(Utils.chat("&C Invalid command arguments. Use /floor <floor>"));
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("floor")) {
			if (args.length == 1) {
				ArrayList<String> cmds = new ArrayList<String>();
				//cmds.add("");
				Collections.sort(cmds);
				return cmds;
			}
		}
		return null;
	}
}
