package com.github.lantice3720.Commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class pstorage implements CommandExecutor, TabCompleter {

    public static Plugin plugin;
    public pstorage(Plugin plugin) { pstorage.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

        if(args.length == 0) return false; // command is '/pstorage'
        switch(args[0]){
            case "get":
            case "set":
            case "remove":
                if(args.length == 1) return false; // command is '/pstorage get|set'

                switch(args[1]){
                    case "entity": //
                        Entity entity;

                        if(args[2].matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}") && Bukkit.getEntity(UUID.fromString(args[2])) != null){
                            entity = Bukkit.getEntity(UUID.fromString(args[2]));
                        }else if(Bukkit.getPlayer(args[2]) != null){
                            Bukkit.getLogger().info("asd");
                            entity = Bukkit.getPlayer(args[2]);
                        }else{
                            return false; // command's argument that selects entity is invalid
                        }

                        if(args[0].equals("get")) { // '/pstorage get'
                            for(NamespacedKey key : entity.getPersistentDataContainer().getKeys()){
                                sender.sendMessage(key.getKey()+": "+entity.getPersistentDataContainer().get(key, PersistentDataType.STRING));
                            }
                        }else if(args[0].equals("set")){ // '/pstorage set entity (selector)'
                            if(args.length < 5) return false;

                            NamespacedKey namespacedKey = new NamespacedKey(plugin, args[3]);

                            entity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, args[4]);

                            sender.sendMessage("Successfully set entity's data storage "+args[3]+" to "+args[4]+".");
                        }else { // '/pstorage remove entity (selector)'
                            if(args.length != 4) return false;

                            NamespacedKey namespacedKey = new NamespacedKey(plugin, args[3]);

                            entity.getPersistentDataContainer().remove(namespacedKey);

                            sender.sendMessage("Successfully removed entity's data storage "+args[3]);
                        }

                    case "block":
                        break;
                    default:
                        return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {
        final List<String> tabCompleteList = new ArrayList<>();

        switch(args.length) {
            case 1:
                tabCompleteList.add("set");
                tabCompleteList.add("get");
                tabCompleteList.add("remove");
                break;
            case 2:
                tabCompleteList.add("entity");
                tabCompleteList.add("block");
                break;
            case 3:
                if(args[1].equals("entity")){
                    for(Player player : Bukkit.getOnlinePlayers()){
                        tabCompleteList.add(player.getName());
                    }
                }else if(args[1].equals("block")){
                    if(sender instanceof Player player){
                        if(player.getTargetBlock(19) != null) {
                            tabCompleteList.add(String.valueOf(player.getTargetBlock(19).getX()));
                            tabCompleteList.add(String.valueOf(player.getTargetBlock(19).getY()));
                            tabCompleteList.add(String.valueOf(player.getTargetBlock(19).getZ()));
                        }else{
                            tabCompleteList.add("~");
                            tabCompleteList.add("~");
                            tabCompleteList.add("~");
                        }
                    }
                }
                break;
            default:
                break;
        }

        tabCompleteList.sort(Comparator.naturalOrder());
        return tabCompleteList;
    }
}
