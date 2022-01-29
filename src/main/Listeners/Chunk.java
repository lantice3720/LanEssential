package main.Listeners;

import main.Fx.ChunkFx;
import main.LanEssential;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;

public class Chunk implements Listener {

    public static Plugin plugin;
    public Chunk(Plugin plugin) { Chunk.plugin = plugin; }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e){
        org.bukkit.Chunk chunk = e.getChunk();
        if(e.isNewChunk() || !LanEssential.chunkDataMap.containsKey(chunk.getChunkKey()) || !LanEssential.chunkDataMap.get(chunk.getChunkKey()).containsKey("manamax") || !LanEssential.chunkDataMap.get(chunk.getChunkKey()).containsKey("mana")) {
//            Bukkit.getOnlinePlayers().forEach((player) -> player.sendMessage("loaded chunk"));
            ChunkFx.resetMana(e.getChunk());
        }
    }
}
