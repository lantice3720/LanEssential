package main.Fx;

import main.Commands.chunk;
import main.LanEssential;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ChunkFx {

    public static void refresh(int x, int z, World world) {
        world.getChunkAt(x, z).unload();
        world.getChunkAt(x, z).load();
    }

    public static void resetMana(int x, int z, World world) {
        HashMap<String, Object> chunkData = new HashMap<>();
        chunkData.put("manamax", Math.floor(Math.random()*2801 + 200));
        chunkData.put("mana", 100);
        LanEssential.chunkDataMap.put(world.getChunkAt(x, z).getChunkKey(), chunkData);
    }

    public static void resetMana(Chunk chunk) {
        resetMana(chunk.getX(), chunk.getZ(), chunk.getWorld());
    }

    public static void loadData(Chunk chunk, FileConfiguration dataFile, HashMap<String, Object> map){

    }
}
