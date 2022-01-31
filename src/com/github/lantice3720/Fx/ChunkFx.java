package com.github.lantice3720.Fx;

import com.github.lantice3720.LanEssential;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChunkFx {

    // refresh given chunk
    public static void refresh(int x, int z, World world) {
        world.getChunkAt(x, z).unload();
        world.getChunkAt(x, z).load();
    }

    // reset mana of the chunk
    public static void resetMana(int x, int z, World world) {
        HashMap<String, Object> chunkData = new HashMap<>();
        chunkData.put("manamax", Math.floor(Math.random()*2801 + 200));
        chunkData.put("mana", 100);
        LanEssential.chunkDataMap.put(world.getUID()+"_"+world.getChunkAt(x, z).getChunkKey(), chunkData);
    }

    public static void resetMana(Chunk chunk) {
        resetMana(chunk.getX(), chunk.getZ(), chunk.getWorld());
    }

    // load chunk mana at file and put to map
    public static void loadMana(Chunk chunk, FileConfiguration dataFile, HashMap<String, HashMap<String, Object>> map, boolean force){
        if(force && map.containsKey(chunk.getChunkKey()+"_"+chunk.getWorld().getUID())) {
            return;
        }

        if(dataFile.get(chunk.getWorld().getUID()+"."+chunk.getChunkKey()) == null){
            resetMana(chunk);
            return;
        }

        HashMap<String, Object> value = new HashMap<>();

        value.put("mana", dataFile.get(chunk.getWorld().getUID()+"."+chunk.getChunkKey()+".mana"));
        value.put("manamax", dataFile.get(chunk.getWorld().getUID()+"."+chunk.getChunkKey()+".manamax"));

        map.put(chunk.getWorld().getUID()+"_"+chunk.getChunkKey(), value);
    }

    public static void loadMana(Chunk chunk, FileConfiguration dataFile) {
        loadMana(chunk, dataFile, LanEssential.chunkDataMap, false);
    }

//    // save chunk to file
//    public static void saveData(Chunk chunk, FileConfiguration dataFile) {
//
//    }

    // return chunks near given chunk
    public static List<Chunk> getNearbyChunks(Chunk chunk, int r) {
        List<Chunk> chunks = new ArrayList<>();

        for(int i = chunk.getX() -r; i != r; i++) {
            for(int j = chunk.getZ() -r; j != r; j++){
                chunks.add(chunk.getWorld().getChunkAt(i, j));
            }
        }

        return chunks;
    }
}
