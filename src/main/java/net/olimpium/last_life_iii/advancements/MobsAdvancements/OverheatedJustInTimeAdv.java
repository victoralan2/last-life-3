package net.olimpium.last_life_iii.advancements.MobsAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import net.olimpium.last_life_iii.mobs.OverheatedCreeper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

public class OverheatedJustInTimeAdv extends BaseAdvancement  implements HiddenVisibility {

    public static String key = "time_overheated_advancement";
    public static AdvancementDisplay advancementDisplay = new AdvancementDisplay(Material.CREEPER_HEAD, "Justo a tiempo", AdvancementFrameType.CHALLENGE, true, true, 5, 7.5f, "Te quedaba un segundo ☠");

    public OverheatedJustInTimeAdv(Advancement parent){
        super(key,advancementDisplay,parent);

        registerEvent(EntityDeathEvent.class, e ->{
            if(e.getEntity().getKiller() != null) {
                if(OverheatedCreeper.timePassed.get(e.getEntity().getUniqueId()) >= 280){
                    grant(e.getEntity().getKiller());
                }
            }
        });
    }
    @Override
    public void giveReward(@NotNull Player player){
        //reward here
    }
}
