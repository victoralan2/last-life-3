package net.olimpium.last_life_iii.items;

//import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
//import net.kyori.adventure.text.Component;
//import net.olimpium.last_life_iii.utils.NBTManager;
//import org.bukkit.Bukkit;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.persistence.PersistentDataType;
//
//public class ExperienceArmor implements Listener {
//    @EventHandler
//    public void getExperience(PlayerPickupExperienceEvent event) {
//        if(event.getPlayer().getInventory().getChestplate()!=null){
//            int expPerPiece = event.getExperienceOrb().getExperience()/event.getPlayer().getInventory().getArmorContents().length;
//            for (ItemStack item : event.getPlayer().getInventory().getArmorContents()) {
//                try {
//                    int itemExpValue = NBTManager.readNBT(item, PersistentDataType.INTEGER, "experienceAmount", Integer.class);
//                    NBTManager.writeNBT(item, PersistentDataType.INTEGER, itemExpValue + expPerPiece, "experienceAmount");
//                    Bukkit.broadcast(Component.text(itemExpValue+expPerPiece));
//
//                } catch (NullPointerException e) {
//                    NBTManager.writeNBT(item, PersistentDataType.INTEGER, expPerPiece, "experienceAmount");
//                }
//            }
//        }
//    }
//}
