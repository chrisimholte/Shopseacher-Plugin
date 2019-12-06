package ktm6060.shopsearcher.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import ktm6060.shopsearcher.Main;
import ktm6060.shopsearcher.ui.*;

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
			
			
			
			/*
			  Follow java conventions.
			Consider adding more comments. 
			Consider re-ordering where e.setCancelled(true) occurs in the interaction chain/else if chain.
			Where you have it, it could potentially bing ignorant of interactions later on within the process,
			thus creating the impact of not having an handler on an event.
			Potentially another fix that I would try would be adding the statement block:
			if(e.isCancelled()){
			return;
			}
			I can't spend much more time on this tonight. Good Luck Hope it works.
			/
			
			
			
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) return;
			if (title.equals(ShopSearchMenuUI.inventoryName)) {
				ShopSearchMenuUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
			}
		} //actions for PlotSearchUI
		else if(title.equals(PlotSearchUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) return;
			if (title.equals(PlotSearchUI.inventoryName)) {
				PlotSearchUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
			}
		} //actions for ItemSearchUI
		else if(title.equals(ItemSearchUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) return;
			ItemSearchUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		} //actions for MyShopUI
		else if(title.equals(MyShopUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) return;
			MyShopUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		} //actions for PlotUI
		else if(title.equals(PlotUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) return;
			PlotUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		} //actions for ShopItemsUI
		else if(title.equals(ShopItemsUI.inventoryName))
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem() == null) return;
			ShopItemsUI.clicked((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getInventory());
		}
		
	}

}
