package ktm6060.shopsearcher.types;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ShopItem {

	private FileConfiguration skSaveConfig;
	private ItemStack itemStack, priceItemStack;
	private ItemMeta itemMeta;
	private Material item;
	private String owner;
	private int shopkeeperID, shopItemID, amount, price, chestX, chestY, chestZ;
	private World chestW;
	
	public ShopItem(FileConfiguration skSaveConfig, int shopkeeperID, int shopItemID) {
		this.skSaveConfig = skSaveConfig;
		this.shopkeeperID = shopkeeperID;
		this.shopItemID = shopItemID;
		updateData();
	}
	
	@SuppressWarnings("deprecation")
	public String getItemStringSort() {
		if (itemMeta instanceof SkullMeta) return item + ((SkullMeta) itemMeta).getOwner() + "'s Head";
		return "" + item;
	}
	
	public Material getType() {
		return item;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	public ItemStack getPriceItemStack() {
		return priceItemStack;
	}
	
	public ItemMeta getItemMeta() {
		return itemMeta;
	}
	
	public String getowner() {
		return owner;
	}

	public int getAmount() {
		return amount;
	}

	public int getPrice() {
		return price;
	}
	
	public World getChestW() {
		return chestW;
	}
	
	public int getChestX() {
		return chestX;
	}

	public int getChestY() {
		return chestY;
	}

	public int getChestZ() {
		return chestZ;
	}
	
	public int getShopkeeperID() {
		return shopkeeperID;
	}

	public void updateData() {
		//Bukkit.getConsoleSender().sendMessage("[ShopSearcher]: Reading data for Shopkeeper: " + shopkeeperID + "  ShopItem: " + shopItemID);
		this.itemStack = skSaveConfig.getItemStack(shopkeeperID + ".offers." + shopItemID + ".item");
		if (itemStack == null)
			this.itemStack = skSaveConfig.getItemStack(shopkeeperID + ".offers." + shopItemID + ".resultItem");
		this.priceItemStack = skSaveConfig.getItemStack(shopkeeperID + ".offers." + shopItemID + ".item1");
		this.itemMeta = itemStack.getItemMeta();
		this.item = itemStack.getType();
		this.owner = skSaveConfig.getString(shopkeeperID + ".owner");
		this.amount = itemStack.getAmount();
		this.price = skSaveConfig.getInt(shopkeeperID + ".offers." + shopItemID + ".price");
		if (price == 0)
			this.price = priceItemStack.getAmount();
		this.chestW = Bukkit.getWorld(skSaveConfig.getString(shopkeeperID + ".world"));			
		this.chestX = skSaveConfig.getInt(shopkeeperID + ".chestx");
		this.chestY = skSaveConfig.getInt(shopkeeperID + ".chesty");
		this.chestZ = skSaveConfig.getInt(shopkeeperID + ".chestz");
	}
	
	public boolean equals(ShopItem shopItem) {
		if (item.equals(shopItem.getType())) return true;
		return false;
	}
	
	public int compareTo(ShopItem shopItem) {
		if (this.getItemStringSort().equals("ENCHANTED_BOOK") && shopItem.getItemStringSort().equals("ENCHANTED_BOOK")) {
			EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta) itemMeta;
			Map<Enchantment, Integer> enchants1 = meta1.getStoredEnchants();
			EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) shopItem.getItemMeta();
			Map<Enchantment, Integer> enchants2 = meta2.getStoredEnchants();
			
			return (item.toString() + enchants1.toString()).compareToIgnoreCase(shopItem.toString() + enchants2.toString());
		}
		return item.toString().compareToIgnoreCase(shopItem.toString());
	}
	
	public int compareToDeal(ShopItem shopItem) {
		//return price per item
		//return (price/amount) - (shopItem.getPrice()/shopItem.getAmount());
		
		//int num = (int) (((((double) shopItem.getAmount())/shopItem.getPrice()) - (((double) amount)/price)) * 10000);
		//Bukkit.getConsoleSender().sendMessage("compareToDeal Ratio, " + this.toString() + "-" + shopItem.toString() + ": " + num);
		//return (int) num;
		return (int) (((((double) shopItem.getAmount())/shopItem.getPrice()) - (((double) amount)/price)) * 10000);
	}
	
	public String toString() {
		return item.toString();
	}
}
