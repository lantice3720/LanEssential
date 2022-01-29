package com.github.lantice3720.Commands;

import com.github.lantice3720.FakePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class fakeplayer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player player){
            Bukkit.getLogger().info("create fakeplayer command executed:");
            Bukkit.getLogger().info("label: "+label);
            Bukkit.getLogger().info("args: "+Arrays.toString(args));
            FakePlayer.fakePlayer(args[0], player.getLocation(), args[1]);
        }

        return true;
    }
}
