package main.Listeners;

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;


public class Click implements Listener {
    public static Plugin plugin;
    public Click(Plugin plugin) { Click.plugin = plugin; }
    public static HashMap<Player, Integer> rclickPlayers = new HashMap<>();

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack selectedItem = player.getInventory().getItemInMainHand();
        if(e.getAction() == Action.RIGHT_CLICK_AIR) {
            NamespacedKey key = new NamespacedKey(plugin, "itemId");
            Integer itemId = selectedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            if(itemId != null && itemId >= 10000 && itemId < 20000) { // check can be aimed
                e.setCancelled(true);
                if(selectedItem.getType().equals(Material.GOLDEN_SWORD)){
                    ItemStack aimed = new ItemStack(Material.CROSSBOW);
                    CrossbowMeta aimedMeta = (CrossbowMeta) aimed.getItemMeta();

                    aimedMeta.setDisplayName(selectedItem.getItemMeta().getDisplayName()); // set aimedMeta
                    aimedMeta.addChargedProjectile(new ItemStack(Material.ARROW));
                    if(selectedItem.getItemMeta().hasCustomModelData()) aimedMeta.setCustomModelData(selectedItem.getItemMeta().getCustomModelData());
                    aimedMeta.setUnbreakable(selectedItem.getItemMeta().isUnbreakable());
                    aimedMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, selectedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER));
                    if(selectedItem.getItemMeta().hasAttributeModifiers()) aimedMeta.setAttributeModifiers(selectedItem.getItemMeta().getAttributeModifiers());
                    aimedMeta.setLore(selectedItem.getItemMeta().getLore());
                    aimed.setItemMeta(aimedMeta);

                    player.getInventory().setItemInMainHand(aimed); // switch item in hand


                    rclickPlayers.put(player, player.getServer().getCurrentTick());
                    player.setCooldown(Material.CROSSBOW, 1000);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 2, true, false, false));
                    // player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 5, 255, true, false, false));
                    player.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { // disaim
                    @Override
                    public void run() {
                        if (player.getServer().getCurrentTick() - rclickPlayers.get(player) > 2) {
                            ItemStack disaimed = new ItemStack(Material.GOLDEN_SWORD);
                            ItemMeta disaimedMeta = disaimed.getItemMeta();

                            disaimedMeta.setDisplayName(selectedItem.getItemMeta().getDisplayName());
                            if(selectedItem.getItemMeta().hasCustomModelData()) disaimedMeta.setCustomModelData(selectedItem.getItemMeta().getCustomModelData());
                            disaimedMeta.setUnbreakable(selectedItem.getItemMeta().isUnbreakable());
                            disaimedMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, selectedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER));
                            if(selectedItem.getItemMeta().hasAttributeModifiers()) disaimedMeta.setAttributeModifiers(selectedItem.getItemMeta().getAttributeModifiers());
                            disaimedMeta.setLore(selectedItem.getItemMeta().getLore());
                            disaimed.setItemMeta(disaimedMeta);

                            player.getInventory().setItemInMainHand(disaimed);
                            player.setCooldown(Material.CROSSBOW, 0);
                        }
                    }}, 5);

                }else if(selectedItem.getType().equals(Material.CROSSBOW)){
                    rclickPlayers.put(player, player.getServer().getCurrentTick());
                    player.setCooldown(Material.CROSSBOW, 1000);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 2, true, false, false));
                    // player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 5, 255, true, false, false));
                    player.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if(player.getServer().getCurrentTick() - rclickPlayers.get(player) > 2) {
                                ItemStack disaimed = new ItemStack(Material.GOLDEN_SWORD);
                                ItemMeta disaimedMeta = disaimed.getItemMeta();

                                disaimedMeta.setDisplayName(selectedItem.getItemMeta().getDisplayName());
                                if(selectedItem.getItemMeta().hasCustomModelData()) disaimedMeta.setCustomModelData(selectedItem.getItemMeta().getCustomModelData());
                                disaimedMeta.setUnbreakable(selectedItem.getItemMeta().isUnbreakable());
                                disaimedMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, selectedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER));
                                if(selectedItem.getItemMeta().hasAttributeModifiers()) disaimedMeta.setAttributeModifiers(selectedItem.getItemMeta().getAttributeModifiers());
                                disaimedMeta.setLore(selectedItem.getItemMeta().getLore());
                                disaimed.setItemMeta(disaimedMeta);

                                player.getInventory().setItemInMainHand(disaimed);
                                player.setCooldown(Material.CROSSBOW, 0);
                            }
                        }}, 5);
                }else {
                    Bukkit.getLogger().warning("Error: wrong itemId");
                }
            }
        } else if(e.getAction() == Action.LEFT_CLICK_AIR){
            e.setCancelled(true);
            if(selectedItem.getType().name().contains("SWORD")){
                Bukkit.getLogger().info("a player used sword with cooldown "+player.getAttackCooldown());

            }
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e){
        Entity victim = e.getEntity();
        Entity attacker = e.getDamager();
        if(attacker.getName().equals("lanthanide")) e.setDamage(99);

        Bukkit.getLogger().info(attacker.getName()+" attacked "+victim.getName()+" with "+ e.getDamage()+" using "+e.getCause());

    }


    @EventHandler
    public void shotCrossBow(EntityLoadCrossbowEvent e){
        Entity entity = e.getEntity();
        if(entity instanceof Player) {
            ItemStack selectedItem = ((Player) entity).getInventory().getItemInMainHand();
            Bukkit.getLogger().info(selectedItem.getItemMeta().getDisplayName());
            NamespacedKey key = new NamespacedKey(plugin, "itemId");
            Integer itemId = selectedItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            if (itemId != null && itemId >= 10000 && itemId < 20000) { // check can be aimed
                e.setCancelled(true);
            }
        }
    }

    // PlayerAttackEntityCooldownResetEvent
}
