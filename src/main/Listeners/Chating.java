package main.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class Chating implements Listener {

    public static Plugin plugin;

    public Chating(Plugin plugin){
        Chating.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        e.setCancelled(false);
        e.setFormat(ChatColor.GOLD+p.getName()+ChatColor.WHITE+" : "+ChatColor.WHITE+e.getMessage());
    }
}
