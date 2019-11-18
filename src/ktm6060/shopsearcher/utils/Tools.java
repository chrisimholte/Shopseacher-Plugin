package ktm6060.shopsearcher.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ktm6060.shopsearcher.Main;
import ktm6060.shopsearcher.managers.ConfigManager;
import ktm6060.shopsearcher.types.ShopItem;
import ktm6060.shopsearcher.types.ShopKeeperData;

public class Tools {
	
	//private static ArrayList<ShopItem> allShopItems = new ArrayList<ShopItem>();
	private static HashMap<Integer, ArrayList<ShopItem>> allShopItemsMap = new HashMap<Integer, ArrayList<ShopItem>>();

	public static FileConfiguration getSKConfig() {
		FileConfiguration skConfig = null;
		if (Bukkit.getPluginCommand("Shopkeepers").getPlugin().getDataFolder().exists()) {
			File file = new File(Bukkit.getPluginCommand("Shopkeepers").getPlugin().getDataFolder(), "config.yml");
			if (file.exists())
				skConfig = YamlConfiguration.loadConfiguration(file);
		}
		return skConfig;
	}

	public static FileConfiguration getSKSaveConfig() {
		FileConfiguration skSaveConfig = null;
		if (Bukkit.getPluginCommand("Shopkeepers").getPlugin().getDataFolder().exists()) {
			File saveFile = new File(Bukkit.getPluginCommand("Shopkeepers").getPlugin().getDataFolder(), "save.yml");
			if (saveFile.exists())
				skSaveConfig = YamlConfiguration.loadConfiguration(saveFile);
		}
		return skSaveConfig;
	}
	
	public static ArrayList<ShopKeeperData> getShopKeeperDataByOwner(String owner) {
		FileConfiguration skSaveConfig = getSKSaveConfig();
		ArrayList<ShopKeeperData> shopkeepers = new ArrayList<ShopKeeperData>();
		String str = "";
		int limit =  10000;
		int breakCnt = 0;
		int breakCntLimit = 50, breakCntExecption = 1000;
		
		boolean breakFlag;
		for (int i = 0; i < limit; i++) {
			breakFlag = true;
			
			try {
				skSaveConfig.get(i + ".owner").equals(null);
			} catch (NullPointerException e) {
				breakFlag = false;
			}
			
			if (breakFlag)
			{
				breakCnt = 0;
				
				//check if owner owns a shop
				if (skSaveConfig.getString(i + ".owner").equals(owner)) {
					
					//check if shop is a sell or trade shop
					if (skSaveConfig.getString(i + ".type").equals("sell") || skSaveConfig.getString(i + ".type").equals("trade")) {
						
						//check if shop has items for sale (at least 1), if so then add Shopkeeper to shopkeepers ArrayList
						str = "" + skSaveConfig.get(i + ".offers.0");
						if (!str.equals("null"))
							shopkeepers.add(new ShopKeeperData(skSaveConfig, i));
					}
				}
			}
			else if (++breakCnt >= breakCntLimit && i >= breakCntExecption) break;
		}
		
		return shopkeepers;
	}
	
	public static ArrayList<ShopItem> getUniqueShopItems() {
		FileConfiguration skSaveConfig = getSKSaveConfig();
		ArrayList<ShopItem> items = new ArrayList<ShopItem>();
		ShopItem shopItem;
		String str = "", str2 = "";
		int limit =  10000;
		int breakCnt = 0, cnt = 0;
		int breakCntLimit = 50, breakCntExecption = 1000;
		allShopItemsMap.clear();
		
		boolean breakFlag;
		for (int i = 0; i < limit; i++) {
			breakFlag = true;
			
			try {
				skSaveConfig.get(i + ".owner").equals(null);
				skSaveConfig.getItemStack(i + ".offers.0.item").getItemMeta();
			} catch (NullPointerException e) {
				breakFlag = false;
			}
			
			if (breakFlag)
			{
				breakCnt = cnt = 0;
				
				//add all shopItems to items list
				do {
					shopItem = new ShopItem(skSaveConfig, i, cnt);
					addToMap(shopItem);
					
					if (items.size() == 0)
						items.add(shopItem);
					else {
						for (int j = 0; j < items.size(); j++) {
							if (shopItem.toString().equals("ENCHANTED_BOOK")) {
								items.add(shopItem);
								break;
							}
							else if (items.get(j).getItemStringSort().equals(shopItem.getItemStringSort()))
								break;
							else if (j == items.size()-1) {
								items.add(shopItem);
								break;
							}
						}
					}
					
					str = "" + skSaveConfig.getString(i + ".offers." + ++cnt + ".item");
					str2 = "" + skSaveConfig.getString(i + ".offers." + cnt + ".item1");
				} while (!str.equals("null") || !str2.equals("null"));
			}
			else if (++breakCnt >= breakCntLimit && i >= breakCntExecption) break;
		}
		//Bukkit.getConsoleSender().sendMessage("Map: " + getAllShopItemsMap().toString());
		items.sort(ShopItem::compareTo);
		return items;
	}
	
	public static void displayShopKeeperItems(Inventory inv, ArrayList<ShopKeeperData> shopkeepers, int currPage) {
		displayShopItemInfo(inv, getShopItemsFromShopKeepers(shopkeepers), currPage);
	}
	
	private static int getStock(ShopItem shopItem) {
		Location chestLocation = new Location(shopItem.getChestW(), shopItem.getChestX(), shopItem.getChestY(), shopItem.getChestZ());
		Block chestBlock = chestLocation.getBlock();
		Chest chest = (Chest) chestBlock.getState();
		Inventory chestInv = chest.getInventory();
		ItemStack[] chestItems = chestInv.getContents();		
		
		int amountItemsInChest = 0;
		for (int j = 0; j < chestItems.length; j++) {
			if (chestItems[j] != null) {
				if (chestItems[j].getType().equals(shopItem.getType()) && chestItems[j].getItemMeta().equals(shopItem.getItemMeta()))
					amountItemsInChest += chestItems[j].getAmount();
			}
		}
		return amountItemsInChest;
	}
	
	public static void displayShopItemsOnly(Inventory inv, ArrayList<ShopItem> shopItems, int currPage, int max) {
		int itemsDisplayed = 0;
		for (int i = (currPage-1)*max; i < shopItems.size(); i++) {
			if (itemsDisplayed >= max) break;
			
			Utils.displayItem(inv, shopItems.get(i).toString(), 1, i - (max*(i/max)), shopItems.get(i).getItemMeta());
			itemsDisplayed++;
		}
	}
	
	public static void displayShopItems(Inventory inv, ArrayList<ShopItem> shopItems, int currPage) {
		displayShopItemInfo(inv, shopItems, currPage);
		displayShopItemLocation(inv, shopItems, currPage);
	}
	
	private static void displayShopItemInfo(Inventory inv, ArrayList<ShopItem> shopItems, int currPage) {
		ShopItem shopItem;
		int cnt = 0, itemsDisplayed = 0, highCurrency, currency;
		//display all items, prices and stock for current page
		for (int i = (currPage-1)*9; i < shopItems.size(); i++) {
			if (itemsDisplayed >= 9) break;
			shopItem = shopItems.get(i);
			Utils.displayItem(inv, shopItem.toString(), shopItem.getAmount(), cnt++, shopItem.getItemMeta());
			
			//set price
			if (shopItem.getPriceItemStack() == null) {
				if (shopItem.getPrice() > 20) {
					highCurrency = shopItem.getPrice() / 9;
					currency = shopItem.getPrice() % 9;
					Utils.createItem(inv, getSKConfig().getString("high-currency-item"), highCurrency, cnt+8, formatMaterialString(getSKConfig().getString("high-currency-item")));
					Utils.createItem(inv, getSKConfig().getString("currency-item"), currency, cnt+17, formatMaterialString(getSKConfig().getString("currency-item")));
				} else
					Utils.createItem(inv, getSKConfig().getString("currency-item"), shopItem.getPrice(), cnt+8, formatMaterialString(getSKConfig().getString("currency-item")));
			}
			else
				Utils.displayItem(inv, shopItem.getPriceItemStack().getType().toString(), shopItem.getPrice(), cnt+8, shopItem.getPriceItemStack().getItemMeta());
			
			
			//get and display stock
			if (getStock(shopItem) / shopItem.getAmount() > 0)
				Utils.createItem(inv, "LIME_CONCRETE", 1, i+27 - (9*(i/9)), "&AIn Stock!");
			else
				Utils.createItem(inv, "RED_CONCRETE", 1, i+27 - (9*(i/9)), "&COut of Stock!");
			
			itemsDisplayed++;
		}
	}
	
	private static void displayShopItemLocation(Inventory inv, ArrayList<ShopItem> shopItems, int currPage) {
		ShopItem shopItem;
		int itemsDisplayed = 0, floor = 0, plot = 0;
		ConfigManager plotConfig = Main.getPlotOwnersConfig();
		
		//display all items, prices and stock for current page
		for (int i = (currPage-1)*9; i < shopItems.size(); i++) {
			if (itemsDisplayed >= 9) break;
			shopItem = shopItems.get(i);
			
			//get floor and plot of owner
			floor = plot = 0;
			for (int f = 0; f < plotConfig.getConfig().getInt("numFloors"); f++) {
				for (int p = 0; p < plotConfig.getConfig().getInt("numPlots"); p++) {
					if ((plotConfig.getConfig().getString("plots.floor" + f + ".plot" + p) + "").equals(shopItem.getowner())) {
						floor = f;
						plot = p;
						break;
					}
				}
				if (floor != 0) break;
			}
			
			Utils.createItem(inv, "PAPER", 1, i+36 - (9*(i/9)), "&FF" + floor + " Plot " + plot, Main.getPlotOwnersConfig().getConfig().getString("ownerTextColor") + "Owner: " + shopItem.getowner());
			itemsDisplayed++;
		}
	}
	
	public static ArrayList<ShopItem> getShopItemsFromShopKeepers(ArrayList<ShopKeeperData> shopkeepers) {
		ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();
		for (int i = 0; i < shopkeepers.size(); i++) {
			for (int j = 0; j < shopkeepers.get(i).getItemsForSale(); j++)
				shopItems.add(shopkeepers.get(i).getItems().get(j));
		}
		return shopItems;
	}
	
	public static void setPageSwitchingIcons(Inventory inv, int numPages, int currPage) {
		if (numPages > 1) {
			if (currPage == 1)
				Utils.createItem(inv, "writable_book", 1, 53, "&6Page " + (currPage + 1));
			else if (currPage == numPages)
				Utils.createItem(inv, "writable_book", 1, 45, "&6Page " + (currPage - 1));
			else {
				Utils.createItem(inv, "writable_book", 1, 53, "&6Page " + (currPage + 1));
				Utils.createItem(inv, "writable_book", 1, 45, "&6Page " + (currPage - 1));
			}
		}
	}
	
	public static int getNumPages(ArrayList<ShopKeeperData> shopkeepers) {
		int totalItemsForSale = 0, pagesNeeded = 0;
		
		for (int i = 0; i < shopkeepers.size(); i++) {
			for (int j = 0; j < (shopkeepers.get(i)).getItemsForSale(); j++)
				totalItemsForSale++;
		}
		
		pagesNeeded = totalItemsForSale / 9;
		if (totalItemsForSale % 9 > 0) pagesNeeded++;
		return pagesNeeded;
	}
	
	public static int getNumPagesItems(ArrayList<ShopItem> shopItems) {
		return shopItems.size() % 9 > 0 ? shopItems.size() / 9 + 1 : shopItems.size() / 9;
	}
	
	public static String formatMaterialString(String materialString) {
		materialString = assignRarityColor(materialString);
		String formatedStr = materialString.substring(0,2) + materialString.substring(2,3);
		for (int i = 3; i < materialString.length(); i++) {
			if (materialString.substring(i,i+1).equals("_"))
				formatedStr += " " + materialString.substring(++i,i+1);
			else
				formatedStr += materialString.substring(i,i+1).toLowerCase();
		}
		return formatedStr;
	}
	
	private static void addToMap(ShopItem shopItem) {
		ArrayList<ShopItem> list = allShopItemsMap.get(HashString(shopItem.toString()));
		if (list == null)
			list = new ArrayList<ShopItem>();
		
		list.add(shopItem);
		allShopItemsMap.put(HashString(shopItem.toString()), list);
	}
	
	public static Integer HashString(String str) {
		return HashString(str, str.length()-1) / 10;
	}
	
	private static Integer HashString(String str, int num) {
		if (num < 0) return 0;
		else
			return str.charAt(num) + HashString(str.substring(0, num), --num);
	}
	
	private static String assignRarityColor(String str) {
		
		switch (str.toLowerCase()) {
		case "creeper_canner_pattern":
		case "skull_banner_pattern":
		case "experience_bottle":
		case "dragon_breath":
		case "elytra":
		case "enchanted_book":
		case "skeleton_skull":
		case "wither_skeleton_skull":
		case "zombie_head":
		case "player_head":
		case "creeper_head":
		case "dragon_head":
		case "heart_of_the_sea":
		case "nether_star":
		case "totem_of_undying":
			str = "&E" + str;
			break;
		case "beacon":
		case "conduit":
		case "end_crystal":
		case "golden_apple":
		case "music_disc_13":
		case "music_disc_cat":
		case "music_disc_blocks":
		case "music_disc_chirp":
		case "music_disc_far":
		case "music_disc_mall":
		case "music_disc_mellohi":
		case "music_disc_stal":
		case "music_disc_strad":
		case "music_disc_ward":
		case "music_disc_11":
		case "music_disc_wait":
			str = "&B" + str;
			break;
		case "mojang_banner_pattern":
		case "command_block":
		case "chain_command_block":
		case "repeating_command_block":
		case "dragon_egg":
		case "enchanted_golden_apple":
		case "structure_block":
		case "jigsaw":
			str = "&O" + str;
			//str = "&5" + str;		megenta, but no color code
			break;
		case "ominous_banner":
			str = "&O" + str;
			//str = "&6" + str;		orange, but no color code
			break;
		default:
			str = "&F" + str;
		}
		
		return str;
	}
	
	public static int plotPrice(int plot) {
		switch (plot) {
		case 1:
		case 7:
		case 13:
		case 19:
			return 48;
		case 4:
		case 10:
		case 16:
		case 22:
			return 16;
		default:
			return 32;
		}
	}
	
	public static HashMap<Integer, ArrayList<ShopItem>> getAllShopItemsMap() {
		return allShopItemsMap;
	}
}
