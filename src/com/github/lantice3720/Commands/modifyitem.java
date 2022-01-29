package com.github.lantice3720.Commands;

import com.github.lantice3720.Fx.Fx;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class modifyitem implements CommandExecutor {
    public static Plugin plugin;
    public modifyitem(Plugin plugin) { modifyitem.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args[0] == null || args[1] == null) {
            sender.sendMessage("Usage: /modifyitem [id|model|name] [value]");
            return false;
        }
        if(sender instanceof Player player){
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            StringBuilder arg = new StringBuilder();
            for(int i = 1; i < args.length; i++) arg.append(args[i]).append(" ");
            switch (args[0]) {
                case "id" -> {
                    if (!Fx.isNumeric(args[1])) {
                        sender.sendMessage("Usage: /modifyitem id [number]");
                        return false;
                    }
                    meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "itemId"), PersistentDataType.INTEGER, Integer.parseInt(args[1]));
                }
                case "model" -> {
                    if (!Fx.isNumeric(args[1])) {
                        sender.sendMessage("Usage: /modifyitem model [number]");
                        return false;
                    }
                    meta.setCustomModelData(Integer.parseInt(args[1]));
                }
                case "name" -> meta.setDisplayName(arg.toString());
                case "attribute" -> {
                    if (!Fx.isNumeric(args[1])) {
                        sender.sendMessage("Usage: /modifyitem attribute [number]");
                        return false;
                    }
                    AttributeModifier.Operation op = AttributeModifier.Operation.ADD_NUMBER;
                    Attribute attr = Attribute.GENERIC_ATTACK_SPEED;
                    AttributeModifier attribute = new AttributeModifier(UUID.randomUUID(), "attackspeed", Integer.parseInt(args[1]), op);
                    meta.addAttributeModifier(attr, attribute);
                }
                default -> {
                    sender.sendMessage("Usage: /modifyitem [id|model|name] [value]");
                    return false;
                }
            }
            player.getInventory().getItemInMainHand().setItemMeta(meta);
            sender.sendMessage("Your item's "+args[0]+" changed to: "+arg);
        }

        return true;
    }
}
