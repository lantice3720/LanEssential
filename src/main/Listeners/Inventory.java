package main.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Inventory implements Listener {

    public static Plugin plugin;
    public Inventory(Plugin plugin) { Inventory.plugin = plugin; }

    @EventHandler
    public void onSelectedSlotChange(PlayerItemHeldEvent e){
        Player player = e.getPlayer();
        ItemStack selectedItem = player.getInventory().getItemInMainHand();
        ItemStack newSelectedItem = player.getInventory().getItem(e.getNewSlot());
        Bukkit.getLogger().info("Hmm "+e.getEventName()+", "+player.getAttackCooldown()+", "+e.getNewSlot());
        if(newSelectedItem != null && newSelectedItem.getType().name().contains("SWORD")) {
            player.setCooldown(newSelectedItem.getType(), 10);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){
        HumanEntity eplayer = e.getPlayer();
        ItemStack selectedItem = eplayer.getInventory().getItemInMainHand();
        org.bukkit.inventory.Inventory openedInventory = e.getInventory();
        Bukkit.getLogger().info(eplayer.getName()+", "+e.getView().getTitle()+", "+openedInventory.getSize()+", ");
    }

}