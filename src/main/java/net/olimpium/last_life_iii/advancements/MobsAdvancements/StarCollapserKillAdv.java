package net.olimpium.last_life_iii.advancements.MobsAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.entity.EntityDeathEvent;

public class StarCollapserKillAdv extends BaseAdvancement implements HiddenVisibility {

    public static String key = "starcollapser_advancement";
    public static AdvancementDisplay advancementDisplay = new AdvancementDisplay(Material.END_CRYSTAL, "Star Annihilator", AdvancementFrameType.CHALLENGE,true,true,4, 9f,"Mata al Star Collapser");
    public static BaseAdvancement advancement;
    public StarCollapserKillAdv(Advancement parent){
        super(key, advancementDisplay, parent);
        advancement = this;
        registerEvent(EntityDeathEvent.class, e -> {
            if(e.getEntity().getKiller() != null){
                if(e.getEntity().getCustomName().equals(ChatColor.DARK_PURPLE + "Star"+ ChatColor.LIGHT_PURPLE+" Collapser")){
                }
            }
        });
    }

}
