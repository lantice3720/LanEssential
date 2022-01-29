package com.github.lantice3720;

import com.github.lantice3720.Commands.*;
import com.github.lantice3720.Listeners.*;
import com.github.lantice3720.Fx.ChunkFx;
import com.github.lantice3720.Fx.Fx;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public class LanEssential extends JavaPlugin {

    public static HashMap<String, HashMap<String, Object>> chunkDataMap = new HashMap<>();

    @Override
    public void onEnable(){
        Logger console = Bukkit.getLogger();
        PluginDescriptionFile pluginInfo = getDescription();
        String pluginVersion = pluginInfo.getVersion();
        String pluginName = pluginInfo.getFullName();
        String pluginAuthor = pluginInfo.getAuthors().toString();

        // register commands
        this.getCommand("hi").setExecutor(new hi());
        this.getCommand("fakeplayer").setExecutor(new fakeplayer());
        this.getCommand("modifyitem").setExecutor(new modifyitem(this));
        this.getCommand("chunk").setExecutor(new chunk());
        this.getCommand("chunk").setTabCompleter(new chunkTabCompleter());

        // register events
        getServer().getPluginManager().registerEvents(new Chating(this), this);
        getServer().getPluginManager().registerEvents(new Player(this), this);
        getServer().getPluginManager().registerEvents(new Click(this), this);
        getServer().getPluginManager().registerEvents(new Inventory(this), this);
        getServer().getPluginManager().registerEvents(new Chunk(this), this);

        // reload data file
        DataManager.reloadDataFile(this, "fakePlayers.yml");
        DataManager.reloadDataFile(this, "chunkMana.yml");

        FileConfiguration fakePlayers = DataManager.getDataFile(this, "fakePlayers.yml");
        FileConfiguration chunkMana = DataManager.getDataFile(this, "chunkMana.yml");

        Location fpLocation;
        for(String key : fakePlayers.getKeys(false)) {
            console.info("Spawning fakePlayer: "+key);
            fpLocation = new Location(Bukkit.getWorld(fakePlayers.getString(key+".pos.world")), Double.parseDouble(fakePlayers.getString(key+".pos.x")), Float.parseFloat(fakePlayers.getString(key+".pos.y")), Float.parseFloat(fakePlayers.getString(key+".pos.z")));
            FakePlayer.fakePlayer(fakePlayers.getString(key+".name"), fpLocation, fakePlayers.getString(key+".skin"), fakePlayers.getString(key+".signature"), UUID.fromString(fakePlayers.getString(key+".uuid")), fakePlayers.getString(key+".skinname"));
        }

//        HashMap<String, Object> chunkData = new HashMap<>();
//        String[] keys;
//        for(String key : chunkMana.getKeys(true)) {
//            keys = key.split("\\.");
//            if(Fx.isNumeric(keys[keys.length - 1])) continue;
//
//            console.info("Loading Chunk Data: " + keys[0]);
//
//            chunkData.put(keys[1], chunkMana.get(key));
//            chunkDataMap.put(Long.parseLong(keys[0]), chunkData);
//        }

        // load chunk near player
        for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            for(org.bukkit.Chunk chunk : ChunkFx.getNearbyChunks(player.getChunk(), 5)) {
                ChunkFx.loadData(chunk, chunkMana, chunkDataMap, false);
            }
        }


        // send plugin enable message
        console.info("=================================");
        console.info("LanEssential Successfully loaded.");
        console.info("Plugin Name: "+pluginName);
        console.info("Plugin Version: "+pluginVersion);
        console.info("Plugin Made By: "+pluginAuthor);
        console.info("=================================");
        console.info("Plugin Path: "+this.getDataFolder());
    }

    @Override
    public void onDisable(){
        for(EntityPlayer fplayer : FakePlayer.fakePlayerTemp){
            FakePlayer.removeFakePlayerPacket(fplayer);
        }
        
        FileConfiguration fakePlayers = DataManager.getDataFile(this, "fakePlayers.yml");
        FileConfiguration chunkMana = DataManager.getDataFile(this, "chunkMana.yml");
//        DataManager.setDataFile(fakePlayers, "hmm", String.valueOf(Math.random()));
//        Bukkit.getLogger().info(fakePlayers.getString("hmm"));

        // plugin disable message
        Bukkit.getLogger().info("Disabling Plugin..");
        Bukkit.getLogger().info("fakePlayerTemp's size: " + FakePlayer.fakePlayerTemp.size());

        // save player's info
        String uuid, name, skinname, skin, signature, x, y, z, yaw, pitch, world;
        for(EntityPlayer fplayer : FakePlayer.fakePlayerTemp){
            org.bukkit.entity.Player fplayerP = fplayer.getBukkitEntity().getPlayer();

            // save basic info
            uuid = fplayerP.getUniqueId().toString();
            name = fplayerP.getName();
            skinname = fplayerP.getMetadata("skinname").get(0).asString();
            skin = Fx.skins.get(skinname);
            signature = Fx.signatures.get(skinname);

            // save position info
            Location pLocation = fplayerP.getLocation();
            x = String.format("%f", pLocation.getX());
            y = String.format("%f", pLocation.getY());
            z = String.format("%f", pLocation.getZ());
            yaw = String.format("%f", pLocation.getYaw());
            pitch = String.format("%f", pLocation.getPitch());
            world = pLocation.getWorld().getName();

            // debug message
            Bukkit.getLogger().info("Saving fakePlayer: "+uuid);

            // add to config
            DataManager.setDataFile(fakePlayers, uuid+".uuid", uuid);
            DataManager.setDataFile(fakePlayers, uuid+".name", name);
            DataManager.setDataFile(fakePlayers, uuid+".skinname", skinname);
            DataManager.setDataFile(fakePlayers, uuid+".skin", skin);
            DataManager.setDataFile(fakePlayers, uuid+".signature", signature);
            DataManager.setDataFile(fakePlayers, uuid+".pos.x", x);
            DataManager.setDataFile(fakePlayers, uuid+".pos.y", y);
            DataManager.setDataFile(fakePlayers, uuid+".pos.z", z);
            DataManager.setDataFile(fakePlayers, uuid+".pos.yaw", yaw);
            DataManager.setDataFile(fakePlayers, uuid+".pos.pitch", pitch);
            DataManager.setDataFile(fakePlayers, uuid+".pos.world", world);
        }

        for(Map.Entry<String, HashMap<String, Object>> chunkDataTemp : chunkDataMap.entrySet()){
            for(Map.Entry<String, Object> chunkDataTemp2 : chunkDataTemp.getValue().entrySet()){
//                Bukkit.getLogger().info("Saving Mana Data of Chunk: " + chunkDataTemp.getKey());

                DataManager.setDataFile(chunkMana, chunkDataTemp.getKey().split("_")[1]+"."+chunkDataTemp.getKey().split("_")[0]+"."+chunkDataTemp2.getKey(), String.valueOf(chunkDataTemp2.getValue()));
            }
        }

        // save config
        DataManager.saveDataFile(this, "fakePlayers.yml", fakePlayers);
        DataManager.saveDataFile(this, "chunkMana.yml", chunkMana);
    }

}
