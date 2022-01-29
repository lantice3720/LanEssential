package main.Commands;

import main.Fx.ChunkFx;
import main.LanEssential;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class chunk implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player){
            Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
            if(args.length == 0) {
                sender.sendMessage("Chunk Key: " + chunk.getChunkKey() + "\n");
                return true;
            }
            switch (args[0]) {
                case "get" -> {
                    if (args.length == 1) {
                        sender.sendMessage("Chunk Key: " + chunk.getChunkKey() + "\n");
                        return true;
                    }
                    switch (args[1]) {
                        case "nearby_mana", "mana" -> {
                            sender.sendMessage("Mana Of Current Chunk: " + LanEssential.chunkDataMap.get(chunk.getChunkKey()).get("mana"));
                            sender.sendMessage("Max Mana Of Current Chunk: " + LanEssential.chunkDataMap.get(chunk.getChunkKey()).get("manamax"));
                        }
                        case "advanced" -> sender.sendMessage(
                                "Chunk Key: " + chunk.getChunkKey() + "\n"
                                        + "Chunk X: " + chunk.getX() + "\n"
                                        + "Chunk Z: " + chunk.getZ() + "\n"
                                        + "Chunk Inhabited Time: " + chunk.getInhabitedTime() + "\n"
                                        + "Chunk Entity Loaded: " + chunk.isEntitiesLoaded() + "\n"
                                        + "Chunk ForceLoaded: " + chunk.isForceLoaded()
                        );
                        default -> sender.sendMessage("Chunk Key: " + chunk.getChunkKey() + "\n");
                    }
                }
                case "resetmana" -> {
                    if (!LanEssential.chunkDataMap.containsKey(chunk.getChunkKey()) || !LanEssential.chunkDataMap.get(chunk.getChunkKey()).containsKey("manamax") || !LanEssential.chunkDataMap.get(chunk.getChunkKey()).containsKey("mana")) {
//            Bukkit.getOnlinePlayers().forEach((player) -> player.sendMessage("loaded chunk"));
                        ChunkFx.resetMana(chunk);
                    }
                    ;
                    sender.sendMessage("Refreshed mana of the chunk you in!");
                }
            }

        }
        return true;
    }
}
