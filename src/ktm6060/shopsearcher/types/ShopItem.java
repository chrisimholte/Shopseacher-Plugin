package ktm6060.shopsearcher.types;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ShopItem {

	private FileConfiguration skSaveConfig;
	private ItemStack itemStack, priceItemStack;
	private ItemMeta itemMeta;
	private Material item;
	private int shopkeeperID, shopItemID, amount, price, chestX, chestY, chestZ;
	private World chestW;
	
	public ShopItem(FileConfiguration skSaveConfig, int shopkeeperID, int shopItemID) {
		this.skSaveConfig = skSaveConfig;
		this.shopkeeperID = shopkeeperID;
		this.shopItemID = shopItemID;
		updateData();
	}
	
	public String getItemString() {
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
		this.itemStack = skSaveConfig.getItemStack(shopkeeperID + ".offers." + shopItemID + ".item");
		if (itemStack == null)
			this.itemStack = skSaveConfig.getItemStack(shopkeeperID + ".offers." + shopItemID + ".resultItem");
		this.priceItemStack = skSaveConfig.getItemStack(shopkeeperID + ".offers." + shopItemID + ".item1");
		this.itemMeta = itemStack.getItemMeta();
		this.item = itemStack.getType();
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
	
	public int compareTo(ShopItem o) {
		return item.toString().compareToIgnoreCase(o.toString());
	}
	
	@SuppressWarnings("deprecation")
	public String toString() {
		if (itemMeta instanceof SkullMeta) return item + ((SkullMeta) itemMeta).getOwner() + "'s Head";
		return "" + item;
	}
}
