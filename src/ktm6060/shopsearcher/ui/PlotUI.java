package ktm6060.shopsearcher.ui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.types.ShopKeeperData;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class PlotUI {

	final public static int NUM_PAGES = 3;
	
	private static Inventory inv;
	public static String inventoryName, owner;
	private static int invRows = 6;
	private static int invBoxes = invRows * 9;
	private static int plot = 0;
	private static int floor = 0;
	private static int currPage = 1;
	private static int numPages = 0;
	private static ArrayList<ShopKeeperData> shopkeepers = new ArrayList<ShopKeeperData>();
	
	public static void initialize() {
		inventoryName = Utils.chat("&8Plot " + plot + " (Page " + currPage + " of " + numPages + ")");
		
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player, int f, int p, String o) {
		plot = p;
		floor = f;
		owner = o;
		return GUI(player);
	}
	
	public static Inventory GUI(Player player) {
		shopkeepers = Tools.getShopKeeperDataByOwner(owner);
		numPages = Tools.getNumPages(shopkeepers);
		if (numPages == 0)
			inventoryName = Utils.chat("&8Floor " + floor + " | Plot " + plot + " (No items for sale)");
		else
			inventoryName = Utils.chat("&8Floor " + floor + " | Plot " + plot + " (Page " + currPage + " of " + numPages + ")");
		
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		/*
		 * Add items for sale from owners shop
		 */
		Tools.displayShopKeeperItems(inv, shopkeepers, currPage);
		shopkeepers.clear();
		
		Tools.setPageSwitchingIcons(inv, numPages, currPage);
		
		//go back icon
		Utils.createItem(inv, "barrier", 1, 49, "&CGo Back");
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&CGo Back")))
		{
			//player.sendMessage(Utils.chat("&8[&6*&8] &6&lBack to PlotSearchUI."));
			player.openInventory(PlotSearchUI.GUI(player));
		}
		else if (clicked.getItemMeta().getDisplayName().contains(Utils.chat("Page ")))
		{
			int target = Integer.parseInt(clicked.getItemMeta().getDisplayName().substring(7));
			
			if (target < currPage)
				currPage--;
			else if (target > currPage)
				currPage++;
			
			player.openInventory(PlotUI.GUI(player));
		}
	}
	
	public static void setCurrPage(int cp) {
		currPage = cp;
	}
}
