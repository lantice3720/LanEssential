package com.github.lantice3720;

import com.github.lantice3720.Fx.Fx;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.github.lantice3720.Listeners.Player;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakePlayer {

    public static List<EntityPlayer> fakePlayerTemp = new ArrayList<EntityPlayer>();

    // add fake player without any skin data
    public static void fakePlayer(String name, Location location){
        fakePlayer(name, location, "MHF_steve");
    }

    // add fake player without skin data
    public static void fakePlayer(String name, Location location, String skinName){
        ArrayList<String> skinInfo = Fx.getSkinInfo(skinName); // skin data
        if(skinInfo.get(0) == null||skinInfo.get(1) == null) { // check data
            Bukkit.getLogger().info("Error: Couldn't read skin information.");
            return;
        }
        fakePlayer(name, location, skinInfo.get(0), skinInfo.get(1), skinName);
    }

    // add fake player
    public static void fakePlayer(String name, Location location, String skin, String signature, UUID uuid, String skinName){
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile profile = new GameProfile(uuid, name);
        if(skin == null || signature == null){ // check skin data
            skin = "ewogICJ0aW1lc3RhbXAiIDogMTY0MTAwOTkyMzQ2OSwKICAicHJvZmlsZUlkIiA6ICJjMDZmODkwNjRjOGE0OTExOWMyOWVhMWRiZDFhYWI4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfU3RldmUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWE0YWY3MTg0NTVkNGFhYjUyOGU3YTYxZjg2ZmEyNWU2YTM2OWQxNzY4ZGNiMTNmN2RmMzE5YTcxM2ViODEwYiIKICAgIH0KICB9Cn0=";
            signature = "RIQypEL6vq45N9MzQP6k8mtW+4YU8gBHSTNOQrkBcnf1QjSlC1d/6v9yZ0yUAyr65rVYkXzZ1q7cVIWKz4clTrIopddm8JxjlzSRe4DN3kRyOnOz5HPDyq8hPE368ZHDZYmv1aGL5sZm80T7VWhSquU2RtGjT9mAaXeV8Q46a1MwkRAkBSOfXdaQ2sGA9ljdCv9P3X6pWnxMMnGmx0pup78c4hjtuSIhISiCvoiDjsCzqkQGB/xGaHxvvbOhhfpP2dijjwO2CsbZLDIpEWNbh+BjthIpeA/08FQ0dftD2OmKvCTh9hf70ib2tcgiQSzBd+ZubfQs9UaGcHxcG1XU8Xrr/m1JRvVl/Om9GC/xSUSp2PH3IfMpSgsaMkhjEEwFTVrlQ4VHGENyn90W0HsGyLMlDvQ5MuPDRnmy++3VEnYn8PczBmupUSlT2zLTWxlo3u3+YbH9QYYszwNQy/Fd8c2rVaMlRvlJy2DksbT2JS3WRHuQJctXk/IMzKsyBkrpDIgdPH2Co8QzG2DQPAnuIsksiSmFLmqiaRJdC7w/AybS3nJ3EXOTLwLhmh8S3T6uF+6czPkTNKc8gtqLiq1vWvAajuqB0zDSEznxcxUMDAB6VDvpoovWbDHnPlRw8ZVfws5Tf+6wKrWRUGCnc37Qlok7yFjY8FNo5JHIKPsJ6Ps=";
        }
        Fx.skins.put(skinName, skin); // input skin data
        Fx.signatures.put(skinName, signature);

        // build fake player
        profile.getProperties().put("textures", new Property("textures", skin, signature));
        EntityPlayer fplayer = new EntityPlayer(server, worldServer, profile);
        org.bukkit.entity.Player fplayerP = fplayer.getBukkitEntity();
        fplayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        fplayer.setYawPitch(location.getYaw(), location.getPitch());
        fplayer.setHealth(20);
        fplayer.setInvisible(false);
        fplayer.spawnIn(worldServer);
        fplayerP.setMetadata("skinname", new FixedMetadataValue(Player.plugin, skinName) { // bruh point.. i don't want to use PlayerIO's plugin
        });
        fakePlayerTemp.add(fplayer);

        // send spawn packet to online players
        addFakePlayerPacket(fplayer);
    }

    // add fake player with skin data
    public static void fakePlayer(String name, Location location, String skin, String signature, String skinName){
        fakePlayer(name, location, skin, signature, UUID.randomUUID(), skinName);
    }

    // send spawn packet to online players
    public static void addFakePlayerPacket(EntityPlayer fplayer){
        DataWatcher fpData = fplayer.getDataWatcher();
        fpData.set(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte)127);
        for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            EntityPlayer ep = ((CraftPlayer) player).getHandle();
            ep.b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, fplayer));
            // ep.b.sendPacket(new PacketPlayOutEntityHeadRotation(new PacketDataSerializer()));
            ep.b.sendPacket(new PacketPlayOutNamedEntitySpawn(fplayer));
            ep.b.sendPacket(new PacketPlayOutEntityMetadata(fplayer.getId(), fpData, true));
        }
    }

    // send remove packet to online players
    public static void removeFakePlayerPacket(EntityPlayer fplayer){
        for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()){
            EntityPlayer ep = ((CraftPlayer) player).getHandle();
            try {
                Object packet = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy").getConstructor(int[].class).newInstance(new int[] {fplayer.getId()});
                ep.b.sendPacket((Packet<?>) packet);
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            // debug message
            Bukkit.getLogger().info("Removing fake player from "+player.getName());
        }
    }

    // send all fake player spawn packet to the player
    public static void addPlayerPacket(org.bukkit.entity.Player player){
        for(EntityPlayer fplayer : fakePlayerTemp){
            DataWatcher fpData = fplayer.getDataWatcher();
            fpData.set(new DataWatcherObject<>(17, DataWatcherRegistry.a), (byte)127);
            EntityPlayer ep = ((CraftPlayer) player).getHandle();
            ep.b.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, fplayer));
            ep.b.sendPacket(new PacketPlayOutNamedEntitySpawn(fplayer));
            ep.b.sendPacket(new PacketPlayOutEntityMetadata(fplayer.getId(), fpData, true));
        }
    }

    // return temp fakePlayer file
    public static List<EntityPlayer> getFakePlayers() { return fakePlayerTemp; }
}
