package ktm6060.shopsearcher.utils;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import ktm6060.shopsearcher.types.ShopItem;
import ktm6060.shopsearcher.types.ShopKeeperData;

public class Tools {
	
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
		int limit =  5000;
		int breakCnt = 0;
		int breakCntLimit = 50;
		int breakCntExecption = 930;
		
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
			else
				if (++breakCnt >= breakCntLimit && i >= breakCntExecption) break;
		}
		
		return shopkeepers;
	}
	
	@SuppressWarnings("unused")
	public static ArrayList<ShopItem> getUniqueShopItems() {
		FileConfiguration skSaveConfig = getSKSaveConfig();
		ArrayList<ShopItem> items = new ArrayList<ShopItem>();
		ShopItem shopItem;
		SkullMeta skull1, skull2;
		//EnchantmentStorageMeta item1, item2;
		//Enchantment ench1, ench2;
		String str = "", str2 = "", itemName1 = "", itemName2 = "";
		int limit =  1000;
		int breakCnt = 0, cnt = 0, charLimit = 0;
		int breakCntLimit = 50, breakCntExecption = 15;
		
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
				breakCnt = cnt = 0;
				
				do {
					//find proper position in item list to place ShopItem
					shopItem = new ShopItem(skSaveConfig, i, cnt);
					Bukkit.getConsoleSender().sendMessage("Found " + shopItem);
					if (items.size() == 0) {
						Bukkit.getConsoleSender().sendMessage("Added " + shopItem);
						items.add(shopItem);
					}
					else {
/*
						//loop through already found ShopItems in items list
						for (int j = 0; j < items.size(); j++) {
							breakFlag = false;
							
							if (shopItem.getItemString().charAt(0) < items.get(j).getItemString().charAt(0)) {
								Bukkit.getConsoleSender().sendMessage("Added " + shopItem);
								items.add(j, shopItem);
								break;
							}
							else if (shopItem.getItemString().charAt(0) == items.get(j).getItemString().charAt(0)) {
								//TODO Handle exception for enchanted items and player heads (sort in alphabetical order based on first enchant)
								Bukkit.getConsoleSender().sendMessage(shopItem.toString() + " : " + items.get(j).toString() + "  " + shopItem.equals(items.get(j)));
								if (shopItem.equals(items.get(j))) {
									Bukkit.getConsoleSender().sendMessage(Utils.chat("&CFound duplicate item: &F" + shopItem));
									//Handle Player Heads
									if (shopItem.getItemMeta().equals(items.get(j).getItemMeta())) {
										Bukkit.getConsoleSender().sendMessage(Utils.chat("&3ItemMeta is the same"));
									}
									else if (shopItem.getItemString().equals("PLAYER_HEAD")) {
										skull1 = (SkullMeta) shopItem.getItemMeta();
										skull2 = (SkullMeta) items.get(j).getItemMeta();
										Bukkit.getConsoleSender().sendMessage("Owners: " + skull1.getOwner() + " " + skull2.getOwner());
										//check if heads have same owner
										if (!skull1.getOwner().equals(skull2.getOwner())) {
											//loop through chars in skull owners
											charLimit = (skull1.getOwner().length() < skull2.getOwner().length()) ? skull1.getOwner().length() : skull2.getOwner().length();
											for (int k = 1; k < charLimit; k++) {
												Bukkit.getConsoleSender().sendMessage("k: " + k + "   " + skull1.getOwner() + " : " + skull2.getOwner());
												if (skull1.getOwner().charAt(k) < skull2.getOwner().charAt(k)) {
													Bukkit.getConsoleSender().sendMessage("Added " + skull1.getOwner() + "'s player head");
													items.add(j, shopItem);
													break;
												} else if (skull1.getOwner().charAt(k) > skull2.getOwner().charAt(k)) {
													Bukkit.getConsoleSender().sendMessage("Added " + skull1.getOwner() + "'s player head");
													items.add(j+1, shopItem);
													break;
												}
											}
											break;
										}
									}
									//TODO support enchanted armor/tools
									/*
									else if (shopItem.getItemString().equals("PLACEHOLDER")) {
										
									}
									/
									
									
								}
								else {
									
									//exceptions for enchanted items
									if (shopItem.getItemStack().getEnchantments().size() > 0) {
										//prevent searching array in an out of bounds index
										if (items.size() != j+1) {
											//Add new enchanted book after all enchanted books
											for (int k = 0; true; k++) {
												if (!shopItem.getItemString().equals(items.get(j+k).getItemString())) {
													Bukkit.getConsoleSender().sendMessage("Added " + shopItem);
													items.add(j+k, shopItem);
													break;
												}	
											}
											/*
											if (shopItem.getItemString().equals(items.get(j+1).getItemString())) {
												Bukkit.getConsoleSender().sendMessage(Utils.chat("&BName is exactly the same as next item"));
												//TODO check next item and compare and sort
												
												//sort enchanted books by enchantment names and level
												if (shopItem.getItemString().equals("ENCHANTED_BOOK")) {
													item1 = (EnchantmentStorageMeta) shopItem.getItemMeta();
													Bukkit.getConsoleSender().sendMessage(item1.getStoredEnchants().toString());
													//Bukkit.getConsoleSender().sendMessage("" + item1.getEnchantLevel(Enchantment.DURABILITY));
													//ench1 = (Enchantment) item1.getStoredEnchants().get(new Enchantment);
													//Bukkit.getConsoleSender().sendMessage(ench1.toString());
												}
											}
											/
											break;
										}
									}
									/*
									//check item after to ensure correct positioning
									for (int k = 0; true; k++) {
										if (!shopItem.getItemString().equals(items.get(j+k).getItemString())) {
											Bukkit.getConsoleSender().sendMessage("Added " + shopItem);
											items.add(j+k, shopItem);
											break;
										}	
									}
									/
									
									//check for underscore in item name
									itemName1 = shopItem.getItemString();
									itemName2 = items.get(j).getItemString();
									if (itemName1.contains("_") && itemName1.substring(0, itemName1.indexOf("_")).equals(itemName2.indexOf("_"))) {
										Bukkit.getConsoleSender().sendMessage("TEST: " + itemName1.substring(itemName1.indexOf("_")+1));
										itemName1 = itemName1.substring(itemName1.indexOf("_")+1);
										itemName2 = itemName2.substring(itemName2.indexOf("_")+1);
									}
									
									//loop through chars in ItemMaterialStrings
									charLimit = (itemName1.length() < itemName2.length()) ? itemName1.length() : itemName2.length();
									for (int k = 0; k < charLimit; k++) {
										if (itemName1.charAt(k) < itemName2.charAt(k)) {
											Bukkit.getConsoleSender().sendMessage("Added " + shopItem);
											items.add(j, shopItem);
											break;
										} else if (itemName1.charAt(k) > itemName2.charAt(k)) {
											Bukkit.getConsoleSender().sendMessage("Added " + shopItem);
											items.add(j+1, shopItem);
											break;
										}
									}
									Bukkit.getConsoleSender().sendMessage(Utils.chat("&CEXIT"));
									//This break is required, otherwise infinite loop will occur
									break;
								}
							}
							else if (j == items.size()-1) {
								Bukkit.getConsoleSender().sendMessage("Added " + shopItem);
								items.add(shopItem);
								break;
							}
						}
*/
						
						items.add(shopItem);
						
					}
					
					str = "" + skSaveConfig.getString(i + ".offers." + ++cnt + ".item");
					str2 = "" + skSaveConfig.getString(i + ".offers." + cnt + ".item1");
				} while (!str.equals("null") || !str2.equals("null"));
				
				Bukkit.getConsoleSender().sendMessage(items.toString());
			}
			else
				if (++breakCnt >= breakCntLimit && i >= breakCntExecption) break;
		}
		
		items.sort(ShopItem::compareTo);
		return items;
	}
	
	public static void displayShopKeeperItems(Inventory inv, ArrayList<ShopKeeperData> shopkeepers, int currPage) {
		ShopItem shopItem;
		ItemStack[] chestItems = null;
		Location chestLocation;
		Block chestBlock;
		Chest chest;
		Inventory chestInv;
		//Material highCurrencyMaterial = (Material) getSKConfig().get("high-currency-item"), currencyMaterial = (Material) getSKConfig().get("currency-item");
		//ItemMeta highCurrencyItemMeta, currencyItemMeta;
		ArrayList<ShopItem> shopItems = getShopItemsFromShopKeepers(shopkeepers);
		int cnt = 0, itemsDisplayed = 0, highCurrency, currency, amountItemsInChest;
		
		//display all items, prices and stock for current page
		for (int i = (currPage-1)*9; i < shopItems.size(); i++) {
			if (itemsDisplayed >= 9) break;
			shopItem = shopItems.get(i);
			Utils.displayItem(inv, shopItem.getItemString(), shopItem.getAmount(), cnt++, shopItem.getItemMeta());
			
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
			
			//get stock
			chestLocation = new Location(shopItem.getChestW(), shopItem.getChestX(), shopItem.getChestY(), shopItem.getChestZ());
			chestBlock = chestLocation.getBlock();
			chest = (Chest) chestBlock.getState();
			chestInv = chest.getInventory();
			chestItems = chestInv.getContents();		
			
			amountItemsInChest = 0;
			for (int j = 0; j < chestItems.length; j++) {
				if (chestItems[j] != null) {
					if (chestItems[j].getType().equals(shopItem.getType()) && chestItems[j].getItemMeta().equals(shopItem.getItemMeta()))
						amountItemsInChest += chestItems[j].getAmount();
				}
			}
			
			//display stock
			if (amountItemsInChest / shopItem.getAmount() > 0)
				Utils.createItem(inv, "LIME_CONCRETE", 1, i+27 - (9*(i/9)), "&AIn Stock!");
			else
				Utils.createItem(inv, "RED_CONCRETE", 1, i+27 - (9*(i/9)), "&COut of Stock!");
			
			itemsDisplayed++;
		}
	}
	
	public static void displayShopItemsOnly(Inventory inv, ArrayList<ShopItem> shopItems, int currPage, int max) {
		int itemsDisplayed = 0;
		for (int i = (currPage-1)*max; i < shopItems.size(); i++) {
			if (itemsDisplayed >= max) break;
			Utils.displayItem(inv, shopItems.get(i).getItemString(), shopItems.get(i).getAmount(), i, shopItems.get(i).getItemMeta());
			itemsDisplayed++;
		}
	}
	
	public static void displayShopItems(Inventory inv, ArrayList<ShopItem> shopItems, int currPage) {
		displayShopItemsOnly(inv, shopItems, currPage, 9);
		//TODO display prices
	}
	
	public static ArrayList<ShopItem> getShopItemsFromShopKeepers(ArrayList<ShopKeeperData> shopkeepers) {
		ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();
		for (int i = 0; i < shopkeepers.size(); i++) {
			for (int j = 0; j < shopkeepers.get(i).getItemsForSale(); j++)
				shopItems.add(shopkeepers.get(i).getItems().get(j));
		}
		return shopItems;
	}
	
	public static int getNumPages(ArrayList<ShopKeeperData> shopkeepers) {
		int totalItemsForSale = 0, pagesNeeded = 0;
		
		for (int i = 0; i < shopkeepers.size(); i++) {
			for (int j = 0; j < shopkeepers.get(i).getItemsForSale(); j++)
				totalItemsForSale++;
		}
		
		pagesNeeded = totalItemsForSale / 9;
		if (totalItemsForSale % 9 > 0) pagesNeeded++;
		return pagesNeeded;
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
}
