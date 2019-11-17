package ktm6060.shopsearcher.ui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.types.ShopItem;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class ShopItemsUI {

	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 6;
	private static int invBoxes = invRows * 9;
	private static int currPage = 1;
	private static int numPages = 0;
	private static ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();
	
	public static void initialize() {
		inventoryName = Utils.chat("&8Offers");
		
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player, ArrayList<ShopItem> list) {
		shopItems = list;
		return GUI(player);
	}
	
	public static Inventory GUI(Player player) {
		
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		/*
		 * Add items for sale from owners shop
		 */
		numPages = Tools.getNumPagesItems(shopItems);
		Tools.displayShopItems(inv, shopItems, currPage);
		shopItems.clear();
		
		Tools.setPageSwitchingIcons(inv, numPages, currPage);
		
		//go back icon
		Utils.createItem(inv, "barrier", 1, 49, "&CGo Back");
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&CGo Back"))) {
			//player.sendMessage(Utils.chat("&8[&6*&8] &6&lBack to ShopSearchMenuUI."));
			player.openInventory(ItemSearchUI.GUI(player));
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
			
			inventoryName = Utils.chat("&8My Shop (Page " + currPage + ")");
			player.openInventory(ShopItemsUI.GUI(player));
		}
	}
	
	public static void setCurrPage(int cp) {
		currPage = cp;
	}
	
	public static void setInvName(String name) {
		inventoryName = name;
	}
}
