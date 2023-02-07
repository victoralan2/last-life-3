package net.olimpium.last_life_iii.advancements.MobsAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.advancements.AdvancementManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.events.Namespace;

public class OverheatedKill10TimesAdv extends BaseAdvancement implements HiddenVisibility{

    public static String key = "many_overheated_advancement";
    public static AdvancementDisplay advancementDisplay = new AdvancementDisplay(Material.CREEPER_HEAD, "Bomb Defuser", AdvancementFrameType.CHALLENGE, true, true, 5, 8.5f, "Mata a 10 Overheated Creepers");

    public OverheatedKill10TimesAdv(Advancement parent) {
        super(key, advancementDisplay, parent, 10);
        registerEvent(EntityDeathEvent.class, e ->{
            if ((e.getEntity().getKiller() != null)) {
                if (e.getEntity().getCustomName().equals(net.md_5.bungee.api.ChatColor.of("#FF4500") + "Over" + net.md_5.bungee.api.ChatColor.of("#FF8300") + "heated" + net.md_5.bungee.api.ChatColor.of("#B7AC44") + " Creeper")) {
                    incrementProgression(e.getEntity().getKiller());
                }
            }
        });
    }
    @Override
    public void giveReward(@NotNull Player player){
        //reward here
    }
}
