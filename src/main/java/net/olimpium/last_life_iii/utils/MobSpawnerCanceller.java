package net.olimpium.last_life_iii.utils;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MobSpawnerCanceller implements Listener{
    @EventHandler
    public void onPlayerPlaceEgg(PlayerInteractEvent e){
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR))
                if (e.getItem() != null)
                    if (e.getItem().getType().toString().toLowerCase().contains("spawn_egg"))

                        e.setCancelled(true);
    }
    @EventHandler
    public void onDispenserDispend(BlockDispenseEvent e){
        if (e.getItem().getType().toString().toLowerCase().contains("spawn_egg")){
            e.setCancelled(true);
        }
    }
}
