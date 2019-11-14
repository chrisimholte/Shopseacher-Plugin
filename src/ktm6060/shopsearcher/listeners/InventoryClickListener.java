package ktm6060.shopsearcher.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import ktm6060.shopsearcher.Main;
import ktm6060.shopsearcher.ui.ItemSearchUI;
import ktm6060.shopsearcher.ui.MyShopUI;
import ktm6060.shopsearcher.ui.PlotSearchUI;
import ktm6060.shopsearcher.ui.PlotUI;
import ktm6060.shopsearcher.ui.ShopSearchMenuUI;

public class InventoryClickListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main plugin;
	
	public InventoryClickListener(Main plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		String title = e.getView().getTitle();
		
		//actions for ShopSearchMenuUI
		if(title.equals(ShopSearchMenuUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) {
				return;
			}
			if (title.equals(ShopSearchMenuUI.inventoryName)) {
				ShopSearchMenuUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
			}
		} //actions for PlotSearchUI
		else if(title.equals(PlotSearchUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) {
				return;
			}
			if (title.equals(PlotSearchUI.inventoryName)) {
				PlotSearchUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
			}
		} //actions for ItemSearchUI
		else if(title.equals(ItemSearchUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) {
				return;
			}
			if (title.equals(ItemSearchUI.inventoryName)) {
				ItemSearchUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
			}
		} //actions for MyShopUI
		else if(title.equals(MyShopUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) {
				return;
			}
			if (title.equals(MyShopUI.inventoryName)) {
				MyShopUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
			}
		} //actions for PlotUI
		else if(title.equals(PlotUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) {
				return;
			}
			if (title.equals(PlotUI.inventoryName)) {
				PlotUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
			}
		}
		
	}

}