package net.olimpium.last_life_iii.items;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import net.olimpium.last_life_iii.utils.NBTManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class ExperienceArmor implements Listener {
    @EventHandler
    public void getExperience(PlayerPickupExperienceEvent event) {
        if(event.getPlayer().getInventory().getChestplate()!=null){
            NBTManager.writeNBT(event.getPlayer().getInventory().getChestplate(), PersistentDataType.INTEGER,1,"test");
            System.out.println(NBTManager.readNBT(event.getPlayer().getInventory().getChestplate(),PersistentDataType.INTEGER,"test",Integer.class));
        }
    }
}
