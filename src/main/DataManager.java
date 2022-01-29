package main;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DataManager {

    private static File file = null;
    private static FileConfiguration configuration = null;

    // reload the file
    public static void reloadDataFile(Plugin plugin, String file) {
        if(DataManager.file == null || !DataManager.file.getName().equals(file)) {
            // no file
            DataManager.file = new File(plugin.getDataFolder(), file);
        }
        configuration = YamlConfiguration.loadConfiguration(DataManager.file);

        // reload
        Reader defaultConfigStream = new InputStreamReader(plugin.getResource(file), StandardCharsets.UTF_8);
        if(defaultConfigStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
            configuration.setDefaults(defaultConfig);
        }
    }

    // get file with path
    public static FileConfiguration getDataFile(Plugin plugin, String file){
        if(configuration == null || !DataManager.file.getName().equals(file)){
            reloadDataFile(plugin, file);
        }
        return configuration;
    }

    public static void saveDataFile(Plugin plugin, String file, FileConfiguration dataFile){
        reloadDataFile(plugin, file);
        try {
            dataFile.save(DataManager.file);
            Bukkit.getLogger().info("Saving data to: "+file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setDataFile(FileConfiguration dataFile, String key, String value){
        dataFile.set(key, value);
    }

    public static void setDataFile(FileConfiguration dataFile, String key, ArrayList value){
        dataFile.set(key, value);
    }
}
