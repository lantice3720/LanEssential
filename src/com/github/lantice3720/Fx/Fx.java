package com.github.lantice3720.Fx;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Fx {

    public static HashMap<String, String> skins = new HashMap<>();
    public static HashMap<String, String> signatures = new HashMap<>();

    // get uuid from player's name
    public static UUID getUUID(String playerName){
        String url = "https://api.mojang.com/users/profiles/minecraft/"+playerName;
        UUID uuid = null;
        try {
            // debug message
            Bukkit.getLogger().info("getting UUID from name");

            // get uuid and parse string to uuid
            String UUIDJson = IOUtils.toString(new URL(url), "UTF-8");
            if(UUIDJson.isEmpty()) return null;
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            String uuidString = UUIDObject.get("id").toString();
            uuidString = uuidString.substring(0, 8)+"-"+uuidString.substring(8, 12)+"-"+uuidString.substring(12, 16)+"-"+uuidString.substring(16, 20)+"-"+uuidString.substring(20, 32);
            uuid = UUID.fromString(uuidString);
            return uuid;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        // error
        return null;
    }

    // get skin info with uuid and name
    public static ArrayList<String> getSkinInfo(UUID uuid, String skinName){
        ArrayList<String> info = new ArrayList<>();
        if(skins.containsKey(skinName)){ // check if plugin has skin info
            // add skin data to list to return
            info.add(skins.get(skinName));
            info.add(signatures.get(skinName));
        }else {
            try {
                // connect to mojang server
                String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false";
                HttpsURLConnection skinServerConnetion = (HttpsURLConnection) new URL(url).openConnection();
                if (skinServerConnetion.getResponseCode() == HttpURLConnection.HTTP_OK) { // check if http connection is fine
                    // read webpage
                    BufferedReader reader = new BufferedReader(new InputStreamReader(skinServerConnetion.getInputStream(), "UTF8"));
                    StringBuilder reply = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        reply.append(line);
                    }
                    String skin = reply.toString().split("\"value\" : \"")[1].split("\"")[0];
                    String signature = reply.toString().split("\"signature\" : \"")[1].split("\"")[0];

                    // put skin info to list
                    skins.put(skinName, skin);
                    signatures.put(skinName, signature);
                    // put skin info to list to return
                    info.add(skin);
                    info.add(signature);
                } else {
                    Bukkit.getLogger().warning("ResponseCode: " + skinServerConnetion.getResponseCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    // get skin info without uuid provided
    public static ArrayList<String> getSkinInfo(String skinName) {
        return getSkinInfo(Fx.getUUID(skinName), skinName);
    }

    // check if the string is numeric
    public static boolean isNumeric(String string){
        try {
            Double.parseDouble(string);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


}
