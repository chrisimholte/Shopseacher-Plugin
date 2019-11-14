package ktm6060.shopsearcher.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.Main;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.utils.Utils;

public class ShopSearchMenuUI {
	
	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 1;
	private static int invBoxes = invRows * 9;
	
	public static void initialize() {
		inventoryName = Utils.chat("&8Shop Searcher Menu");
		
		//create 1x9 inventory
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player) {
		
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		
		//TO-DO: change items
		Utils.createItem(inv, "paper", 1, 0, "&7Search By Shop", "&3See what items a plot sells!", "&4PlotMenuUI");
		Utils.createItem(inv, "paper", 1, 1, "&7Search By Item", "&5See all items being sold!", "&4ItemSearchUI");
		Utils.createPlayerSkull(inv, 1, 2, "&7My Shop", player.getName(), "&7Check your shops items and stock!", "&4MyShopUI");
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&7Search By Shop"))) {
			//player.sendMessage(Utils.chat("&8[&6*&8] &6&lPlotSearchUI opened."));
			PlotSearchUI.setCurrPage(1);
			player.openInventory(PlotSearchUI.GUI(player));
		} else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&7Search By Item"))) {
			//player.sendMessage(Utils.chat("&8[&6*&8] &6&lItemSearchUI opened."));
			ItemSearchUI.setCurrPage(1);
			player.openInventory(ItemSearchUI.GUI(player));
		} else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&7My Shop"))) {
			//player.sendMessage(Utils.chat("&8[&6*&8] &6&lMyShopUI opened."));
			ConfigManager plotConfig = Main.getPlotOwnersConfig();
			int floors = plotConfig.getConfig().getInt("numFloors");
			int plots = plotConfig.getConfig().getInt("numPlots");
			
			for (int i = 0; i < floors; i++) {
				for (int j = 0; j < plots; j++) {
					if (plotConfig.getConfig().getString("plots.floor" + i + ".plot" + j) != null) {
						if (plotConfig.getConfig().getString("plots.floor" + i + ".plot" + j).equals(player.getDisplayName())) {
							MyShopUI.setCurrPage(1);
							player.openInventory(MyShopUI.GUI(player));
							return;
						}
					}
				}
			}
			player.sendMessage(Utils.chat("&CYou do not own a shop plot."));
		}
	}

}