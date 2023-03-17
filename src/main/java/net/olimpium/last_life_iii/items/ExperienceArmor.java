package net.olimpium.last_life_iii.items;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import net.kyori.adventure.text.Component;
import net.olimpium.last_life_iii.utils.EXPUtils;
import net.olimpium.last_life_iii.utils.NBTManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ExperienceArmor implements Listener {
    @EventHandler
    public void getExperience(PlayerPickupExperienceEvent event) {
        if(event.getPlayer().getInventory().getChestplate()!=null){
            int expPerPiece = (int) (event.getExperienceOrb().getExperience()/ Arrays.stream(event.getPlayer().getInventory().getArmorContents()).filter(itemStack -> itemStack!=null).count());
            Bukkit.broadcast(Component.text("EXP Orb: "+event.getExperienceOrb().getExperience()));
            Bukkit.broadcast(Component.text("Attained: "+expPerPiece));
            for (ItemStack item : event.getPlayer().getInventory().getArmorContents()) {
                try {
                    int itemExpValue = NBTManager.readNBT(item, PersistentDataType.INTEGER, "experienceAmount", Integer.class);
                    int levelValue = NBTManager.readNBT(item, PersistentDataType.INTEGER, "level", Integer.class);
                    NBTManager.writeNBT(item, PersistentDataType.INTEGER, itemExpValue + expPerPiece, "experienceAmount");
                    int updatedExpValue = NBTManager.readNBT(item, PersistentDataType.INTEGER, "experienceAmount", Integer.class);
                    if(updatedExpValue >= EXPUtils.getExpAtLevel(levelValue+1)){
                        NBTManager.writeNBT(item, PersistentDataType.INTEGER, levelValue+1, "level");
                    }

                } catch (NullPointerException e) {
                    NBTManager.writeNBT(item, PersistentDataType.INTEGER, expPerPiece, "experienceAmount");
                    NBTManager.writeNBT(item, PersistentDataType.INTEGER, 1, "level");
                }
            }
        }
    }
    @EventHandler
    public void test(PlayerSwapHandItemsEvent e){
        Bukkit.broadcast(Component.text(NBTManager.readNBT(e.getPlayer().getInventory().getChestplate(), PersistentDataType.INTEGER, "level", Integer.class)));
    }
}
