package ktm6060.shopsearcher.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.Main;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class PlotSearchUI {
	
	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 6;
	private static int invBoxes = invRows * 9;
	private static int currPage = 1;
	private static int numPages = 3;
	
	public static void initialize() {
		inventoryName = Utils.chat("&8Plots (Floor " + currPage + " of " + numPages + ")");
		
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player) {
		numPages = Main.getPlotOwnersConfig().getConfig().getInt("numFloors");
		inventoryName = Utils.chat("&8Plots (Floor " + currPage + " of " + numPages + ")");
		
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		ConfigManager plotConfig = Main.getPlotOwnersConfig();
		
		//plot icons
		for (int i = 1; i <= plotConfig.getConfig().getInt("numPlots"); i++) {
			if (plotConfig.getConfig().getString("plots.floor" + currPage + ".plot" + i) != null && !plotConfig.getConfig().getString("plots.floor" + currPage + ".plot" + i).equals(""))
				Utils.createItem(inv, "paper", 1, i-1, "&FPlot " + i, plotConfig.getConfig().getString("ownerTextColor") + "Owner: " + plotConfig.getConfig().getString("plots.floor" + currPage + ".plot" + i));
			else
				Utils.createItem(inv, "paper", 1, i-1, "&FPlot " + i);
		}
		
		Tools.setPageSwitchingIcons(inv, numPages, currPage);
		
		//go back icon
		Utils.createItem(inv, "barrier", 1, 49, "&CGo Back");
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&CGo Back")))
		{
			//player.sendMessage(Utils.chat("&8[&6*&8] &6&lBack to ShopSearchMenuUI."));
			player.openInventory(ShopSearchMenuUI.GUI(player));
		}
		else if (clicked.getItemMeta().getDisplayName().contains(Utils.chat("Floor ")))
		{
			int target = Integer.parseInt(clicked.getItemMeta().getDisplayName().substring(8));
			
			if (target < currPage)
				currPage--;
			else if (target > currPage)
				currPage++;
			
			player.openInventory(PlotSearchUI.GUI(player));
		}
		else if (clicked.getItemMeta().getDisplayName().contains(Utils.chat("Plot ")))
		{
			String targetPlot = clicked.getItemMeta().getDisplayName().substring(5);
			int plot = Integer.parseInt(targetPlot);
			ConfigManager plotConfig = Main.getPlotOwnersConfig();
			
			if (plotConfig.getConfig().getString("plots.floor" + currPage + ".plot" + plot) != null) {
				if (!plotConfig.getConfig().getString("plots.floor" + currPage + ".plot" + plot).equals("")) {
					PlotUI.setCurrPage(1);
					player.openInventory(PlotUI.GUI(player, currPage, plot, plotConfig.getConfig().getString("plots.floor" + currPage + ".plot" + plot)));
					return;
				}
			}
			player.sendMessage(Utils.chat("&CThis plot is vacant."));
		}
		else if (clicked.getItemMeta().getDisplayName().contains(Utils.chat("Page ")))
		{
			/*
			 * Change page of UI
			 */
			String targetPage = clicked.getItemMeta().getDisplayName().substring(7);
			int targetFloor = Integer.parseInt(targetPage);
			
			if (targetFloor < currPage)
				currPage--;
			else if (targetFloor > currPage)
				currPage++;
			
			player.openInventory(PlotSearchUI.GUI(player));
		}
	}
	
	public static void setCurrPage(int cp) {
		currPage = cp;
	}
}
