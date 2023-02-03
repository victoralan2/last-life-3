package net.olimpium.last_life_iii.advancements.MobsAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import net.olimpium.last_life_iii.advancements.AdvancementManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class OverheatedKillAdv extends BaseAdvancement implements HiddenVisibility {

    public static String key = "overheated_advancement";
    public static AdvancementDisplay advancementDisplay= new AdvancementDisplay(Material.CREEPER_HEAD, "Overheated!", AdvancementFrameType.TASK, true, true, 4, 8f, "Mata a un Overheated Creeper");

    public OverheatedKillAdv(Advancement parent) {
        super(key, advancementDisplay, parent);
        registerEvent(EntityDeathEvent.class, e -> {
            if ((e.getEntity().getKiller() != null)) {
                if (e.getEntity().getCustomName().equals(net.md_5.bungee.api.ChatColor.of("#FF4500") + "Over" + net.md_5.bungee.api.ChatColor.of("#FF8300") + "heated" + net.md_5.bungee.api.ChatColor.of("#B7AC44") + " Creeper")) {
                    grant(e.getEntity().getKiller());
                }
            }
        });
    }
}
