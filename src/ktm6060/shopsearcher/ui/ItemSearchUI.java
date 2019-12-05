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
	private int icurrPage = 1;
	private int inumPages = 0;
	private static ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();
	
	public ItemSearchUI() {
		
	}

	public static void initialize() {
		inventoryName = Utils.chat("&8Item Search (Page " + currPage + " of " + numPages + ")");
		
		inv = Bukkit.createInventory(null, invBoxes);
	}
	
	public static Inventory staticGUI(Player player) {
		numPages = shopItems.size() / 45;
		numPages += shopItems.size() % 45 > 0 ? 1 : 0;
		inventoryName = Utils.chat("&8Item Search (Page " + currPage + " of " + numPages + ")");
		
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		/*
		 * Add items for sale from owners shop
		 */
		Tools.displayShopItemsOnly(inv, shopItems, currPage, 45);
		Tools.setPageSwitchingIcons(inv, numPages, currPage);
		
		//go back icon
		Utils.createItem(inv, "barrier", 1, 49, "&CGo Back");
		
		toReturnInventory.setContents(inv.getContents());
		return toReturnInventory;
	}
	
	public Inventory GUI(Player player, int cp) {
		icurrPage = cp;
		inumPages = shopItems.size() / 45;
		inumPages += shopItems.size() % 45 > 0 ? 1 : 0;
		inventoryName = Utils.chat("&8Item Search (Page " + icurrPage + " of " + inumPages + ")");
		
		Inventory toReturnInventory = Bukkit.createInventory(null, invBoxes, inventoryName);
		inv.clear();
		
		/*
		 * Add items for sale from owners shop
		 */
		Tools.displayShopItemsOnly(inv, shopItems, icurrPage, 45);
		Tools.setPageSwitchingIcons(inv, inumPages, icurrPage);
		
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
			int target = Integer.parseInt(clicked.getItemMeta().getDisplayName().substring(7));
			Bukkit.getConsoleSender().sendMessage("Change page to: " + target);
			
			if (target < currPage)
				currPage--;
			else if (target > currPage)
				currPage++;
			
			player.closeInventory();
			ItemSearchUI inventory = new ItemSearchUI();
			player.openInventory(inventory.GUI(player, target));
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
			/*
			String str = "";
			for (int i = 0; i < list.size(); i++)
				str += list.get(i).getAmount() + ":" + list.get(i).getPrice() + ", ";
			Bukkit.getConsoleSender().sendMessage(str);
			*/
			list.sort(ShopItem::compareToDeal);
			ArrayList<ShopItem> temp = new ArrayList<ShopItem>();
			
			for (int i = 0; i < list.size(); i++) 
				temp.add(list.get(i));
			/*
			str = "";
			for (int i = 0; i < temp.size(); i++)
				str += temp.get(i).getAmount() + ":" + temp.get(i).getPrice() + ", ";
			Bukkit.getConsoleSender().sendMessage(str);
			*/
			ShopItemsUI.setCurrPage(1);
			player.openInventory(ShopItemsUI.GUI(player, temp));
		}
	}
	
	public static void setCurrPage(int cp) {
		currPage = cp;
	}
	
	public static void setShopItems(ArrayList<ShopItem> shopItems) {
		ItemSearchUI.shopItems = shopItems;
	}
}
