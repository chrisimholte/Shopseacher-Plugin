package ktm6060.shopsearcher.types;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

public class ShopKeeperData {

	private FileConfiguration skSaveConfig;
	private ArrayList<ShopItem> items;
	private String name, world, owner, shopType;
	
	public int shopkeeperID, itemsForSale, chestX, chestY, chestZ, posX, posY, posZ;
	
	public ShopKeeperData(FileConfiguration skSaveConfig, int shopkeeperID) {
		items = new ArrayList<ShopItem>();
		
		this.skSaveConfig = skSaveConfig;
		this.shopkeeperID = shopkeeperID;
		
		this.name = skSaveConfig.getString(shopkeeperID + ".name");
		this.world = skSaveConfig.getString(shopkeeperID + ".world");
		this.owner = skSaveConfig.getString(shopkeeperID + ".owner");
		this.shopType = skSaveConfig.getString(shopkeeperID + ".type");
		
		updateNumItemsForSale();
		updateItemsForSale();
		updatePosCoords();
		updateChestCoords();
	}
	
	private void updateNumItemsForSale() {
		String str;
		for (int i = 0; i < 45; i++) {
			str = "" + skSaveConfig.get(shopkeeperID + ".offers." + i + ".item"); 
			if (!str.equals("null")) this.itemsForSale++;
			else {
				str = "" + skSaveConfig.get(shopkeeperID + ".offers." + i + ".item1"); 
				if (!str.equals("null")) this.itemsForSale++;
			}
		}
	}
	
	private void updateItemsForSale() {
/*		Code for blacklisting/ignore certain items if ever needed
		int itemsIgnored = 0;
		String material;
		for (int i = 0; i < itemsForSale; i++) {
			//ignore certain items
			material = "" + skSaveConfig.getItemStack(shopkeeperID + ".offers." + i + ".item").getType();
			
			//if item being sold is to be ignored, don't add item to shopkeeper's items and decrease itemsForSale
			if (material.equals("ITEM")) itemsIgnored++;
			
			this.items.add(new ShopItem(skSaveConfig, shopkeeperID, i));
		}
		itemsForSale -= itemsIgnored;
*/
		for (int i = 0; i < itemsForSale; i++)
			this.items.add(new ShopItem(skSaveConfig, shopkeeperID, i));
	}

	private void updatePosCoords() {
		this.posX = skSaveConfig.getInt(shopkeeperID + ".x");
		this.posY = skSaveConfig.getInt(shopkeeperID + ".y");
		this.posZ = skSaveConfig.getInt(shopkeeperID + ".z");
	}
	
	private void updateChestCoords() {
		this.chestX = skSaveConfig.getInt(shopkeeperID + ".chestx");
		this.chestY = skSaveConfig.getInt(shopkeeperID + ".chesty");
		this.chestZ = skSaveConfig.getInt(shopkeeperID + ".chestz");
	}

	public FileConfiguration getSkSaveConfig() {
		return skSaveConfig;
	}

	public ArrayList<ShopItem> getItems() {
		return items;
	}
	
	public int getItemsForSale() {
		return itemsForSale;
	}

	public String getName() {
		return name;
	}

	public String getWorld() {
		return world;
	}

	public String getOwner() {
		return owner;
	}

	public String getShopType() {
		return shopType;
	}

	public int getShopkeeperID() {
		return shopkeeperID;
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

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getPosZ() {
		return posZ;
	}
	
	public String toString() {
		String str = "Items for sale: " + this.itemsForSale;
		for (int i = 0; i < items.size(); i++) {
			str += "\nItem: " + items.get(i).toString() + "  Amount: " + items.get(i).getAmount() + "  Price: " + items.get(i).getPrice();
		}
		return str;
	}
}
