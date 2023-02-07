package net.olimpium.last_life_iii.mobs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Escorpiones implements Listener {
    @EventHandler
    public void onPlayerDamaged(EntityDamageByEntityEvent event){
        if (event.getEntity().getType().equals(EntityType.PLAYER)){
            Player player = (Player) event.getEntity();
            if (event.getDamager().getCustomName() == null)
                return;
            if (event.getDamager().getCustomName().contains(ChatColor.GOLD+"Alacrán Dorado")){
                player.setHealth(player.getHealth() -2);
                player.damage(0.00000001);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 250, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 250, 2));
                event.setCancelled(true);
            } else if (event.getDamager().getCustomName().contains(ChatColor.YELLOW+"Tyrius")){
                player.setHealth(player.getHealth()-1);
                player.damage(0.00000001);
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 250, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 250, 0));
                event.setCancelled(true);
            }
        }
    }
}
