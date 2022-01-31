package com.github.lantice3720.Listeners;

import com.github.lantice3720.DataManager;
import com.github.lantice3720.FakePlayer;
import com.github.lantice3720.Fx.ChunkFx;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
    public void onPlayerJoin(PlayerJoinEvent e){ // player join server event
        if(FakePlayer.getFakePlayers() == null) return;
        if(FakePlayer.getFakePlayers().isEmpty()) return;
        FakePlayer.addPlayerPacket(e.getPlayer());

        Bukkit.getLogger().info("PlayerJoinEvent");
    }

    @EventHandler
    public void onPlayerEntityRespawn(PlayerRespawnEvent e){ // player respawn event
        org.bukkit.entity.Player player = e.getPlayer();
        NamespacedKey manaKey = new NamespacedKey(plugin, "mana");
        NamespacedKey manamaxKey = new NamespacedKey(plugin, "manamax");

        player.getPersistentDataContainer().set(manaKey, PersistentDataType.STRING, "100");
        player.getPersistentDataContainer().set(manamaxKey, PersistentDataType.STRING, "100");

        Bukkit.getLogger().info("PlayerRespawnEvent");
    }

    @EventHandler
    public void onPlayerEntitySpawn(PlayerSpawnLocationEvent e) { // player spawn in world event before player join event. only call when player joins
        org.bukkit.entity.Player player = e.getPlayer();
        NamespacedKey manaKey = new NamespacedKey(plugin, "mana");
        NamespacedKey manamaxKey = new NamespacedKey(plugin, "manamax");

        player.getPersistentDataContainer().set(manaKey, PersistentDataType.STRING, "100");
        player.getPersistentDataContainer().set(manamaxKey, PersistentDataType.STRING, "100");

        Bukkit.getLogger().info("PlayerSpawnLocationEvent");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) { // player moves
        org.bukkit.entity.Player player = e.getPlayer();

        ChunkFx.loadMana(player.getChunk(), DataManager.getDataFile(plugin, "chunkMana.yml"));
    }
}