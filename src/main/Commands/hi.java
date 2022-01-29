package main.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class hi implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player){

            ItemStack diamondAxe = new ItemStack(Material.IRON_AXE);
            diamondAxe.setAmount(200);
            player.getInventory().addItem(diamondAxe);

            player.setCustomName("hmm "+player.getName());
            Bukkit.getLogger().info(player.getCustomName());
        }

        sender.sendMessage("Well, command Executed");

        return true;
    }
}
