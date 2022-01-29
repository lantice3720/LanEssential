package com.github.lantice3720.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class chunkTabCompleter implements TabCompleter {

    public static List<String> subCommands = new ArrayList<>();

    public chunkTabCompleter() {
        subCommands.add("mana");
        subCommands.add("get");
    }



    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] args) {

        List<String> completes = new ArrayList<>();

        StringUtil.copyPartialMatches(args[0], subCommands, completes);

        Collections.sort(completes);

        return null;
    }
}
