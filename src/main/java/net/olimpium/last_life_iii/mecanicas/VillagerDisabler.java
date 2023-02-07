package net.olimpium.last_life_iii.mecanicas;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class VillagerDisabler implements Listener {
    @EventHandler
    public void onTrade(PlayerInteractEntityEvent e){
        if(e.getRightClicked().getType().equals(EntityType.VILLAGER)){
            Villager villager =  (Villager) e.getRightClicked();
            villager.setProfession(Villager.Profession.NITWIT);
            e.setCancelled(true);
        }
    }
}
