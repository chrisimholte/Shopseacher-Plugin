package ktm6060.shopsearcher.ui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import ktm6060.shopsearcher.types.ShopItem;
import ktm6060.shopsearcher.utils.Tools;
import ktm6060.shopsearcher.utils.Utils;

public class ItemSearchUI {
	
	private static Inventory inv;
	public static String inventoryName;
	private static int invRows = 6;
	private static int invBoxes = invRows * 9;
	private static int currPage = 1;
	private static int numPages = 0;
	private static ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();

	public static void initialize() {
		inventoryName = Utils.chat("&8Item Search (Page " + currPage + ")");
		
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory GUI(Player player) {
		
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		/*
		 * Add items for sale from owners shop
		 */
		numPages = shopItems.size() / 45;
		numPages += shopItems.size() % 45 > 0 ? 1 : 0;
		Tools.displayShopItemsOnly(inv, shopItems, currPage, 45);
		
		Tools.setPageSwitchingIcons(inv, numPages, currPage);
		
		//go back icon
		Utils.createItem(inv, "barrier", 1, 49, "&CGo Back");
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	@SuppressWarnings("deprecation")
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&CGo Back"))) {
			//player.sendMessage(Utils.chat("&8[&6*&8] &6&lBack to ShopSearchMenuUI."));
			player.openInventory(ShopSearchMenuUI.GUI(player));
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
			
			Bukkit.getConsoleSender().sendMessage("Change to page " + currPage);
			player.openInventory(ItemSearchUI.GUI(player));
		}
		else
		{
			//handle special cases for player heads and enchanted books
			@SuppressWarnings("unchecked")
			ArrayList<ShopItem> list = (ArrayList<ShopItem>) Tools.getAllShopItemsMap().get(Tools.HashString(clicked.getType().toString())).clone();
			for (int i = 0; i < list.size(); i++) {
				if (clicked.getType().toString().equals("ENCHANTED_BOOK")) {
					if (!list.get(i).getItemMeta().equals(clicked.getItemMeta()))
						list.remove(i--);
				} else if (clicked.getType().toString().equals("PLAYER_HEAD")) {
					if (!((SkullMeta) list.get(i).getItemMeta()).getOwner().equals(((SkullMeta) clicked.getItemMeta()).getOwner()))
						list.remove(i--);
				} else if (!list.get(i).toString().equals(clicked.getType().toString())) {
					list.remove(i--);
				}
			}
			list.sort(ShopItem::compareToDeal);
			player.openInventory(ShopItemsUI.GUI(player, list));
		}
	}
	
	public static void setCurrPage(int cp) {
		currPage = cp;
	}
	
	public static void setShopItems(ArrayList<ShopItem> shopItems) {
		ItemSearchUI.shopItems = shopItems;
	}
}
