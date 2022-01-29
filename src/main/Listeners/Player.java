package main.Listeners;

import main.FakePlayer;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class Player implements Listener {
    public static Plugin plugin;

    public Player(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if(FakePlayer.getFakePlayers() == null) return;
        if(FakePlayer.getFakePlayers().isEmpty()) return;
        FakePlayer.addPlayerPacket(e.getPlayer());
    }

    @EventHandler
    public void onPlayerEntityRespawn(PlayerRespawnEvent e){
        org.bukkit.entity.Player player = e.getPlayer();
        NamespacedKey manaKey = new NamespacedKey(plugin, "mana");
        NamespacedKey manamaxKey = new NamespacedKey(plugin, "manamax");

        player.getPersistentDataContainer().set(manaKey, PersistentDataType.INTEGER, 100);
        player.getPersistentDataContainer().set(manamaxKey, PersistentDataType.INTEGER, 100);
    }

    @EventHandler
    public void onPlayerEntitySpawn(PlayerSpawnLocationEvent e) {
        org.bukkit.entity.Player player = e.getPlayer();
        NamespacedKey manaKey = new NamespacedKey(plugin, "mana");
        NamespacedKey manamaxKey = new NamespacedKey(plugin, "manamax");

        player.getPersistentDataContainer().set(manaKey, PersistentDataType.INTEGER, 100);
        player.getPersistentDataContainer().set(manamaxKey, PersistentDataType.INTEGER, 100);
    }
}