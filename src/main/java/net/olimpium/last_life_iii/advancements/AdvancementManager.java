package net.olimpium.last_life_iii.advancements;


import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.advancements.MobsAdvancements.OverheatedJustInTimeAdv;
import net.olimpium.last_life_iii.advancements.MobsAdvancements.OverheatedKill10TimesAdv;
import net.olimpium.last_life_iii.advancements.MobsAdvancements.OverheatedKillAdv;
import net.olimpium.last_life_iii.advancements.MobsAdvancements.StarCollapserKillAdv;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdvancementManager {
    public static AdvancementTab advancementTab;
    public static RootAdvancement root;
    public static UltimateAdvancementAPI api;

    public static BaseAdvancement itemAdvancement;
    public static BaseAdvancement mobAdvancement;
    public static BaseAdvancement adventureAdvancement;
    public static BaseAdvancement challengeAdvancement;


    public static void createBaseAdvancements() {
        api = UltimateAdvancementAPI.getInstance(Last_life_III.getPlugin());
        //root
        advancementTab = api.createAdvancementTab("last_life_3");


        ItemStack itemStack = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("lastLifeRootAdvancement");
        itemStack.setItemMeta(itemMeta);
        AdvancementDisplay rootDisplay = new AdvancementDisplay(itemStack, "Last Life III", AdvancementFrameType.TASK, true, true, 0, 6); // 0 0 es una esquina
        root = new RootAdvancement(advancementTab, "root", rootDisplay, "textures/block/gold_block.png");
        AdvancementDisplay itemDisplay = new AdvancementDisplay(Material.STICK, "Items", AdvancementFrameType.TASK, false, false, 2, 12);
        AdvancementDisplay mobDisplay = new AdvancementDisplay(Material.ZOMBIE_HEAD, "Mobs", AdvancementFrameType.TASK, false, false, 2, 8);
        AdvancementDisplay adventureDisplay = new AdvancementDisplay(Material.CHAINMAIL_BOOTS, "Aventura", AdvancementFrameType.TASK, false, false, 2, 4);
        AdvancementDisplay challengesDisplay = new AdvancementDisplay(Material.NETHER_STAR, "Retos  ", AdvancementFrameType.TASK, false, false, 2, 0, /*ChatColor.ITALIC+ChatColor.GRAY.toString()+*/"Los retos tienen recompesas.");

        itemAdvancement = new BaseAdvancement("items_advancement", itemDisplay, root);
        mobAdvancement = new BaseAdvancement("mobs_advancement", mobDisplay, root);
        adventureAdvancement = new BaseAdvancement("adventures_advancement", adventureDisplay, root);
        challengeAdvancement = new BaseAdvancement("challenges_advancement", challengesDisplay, root);
    }

    public static void registerAdvancements() {

        BaseAdvancement overHeatedKill = new OverheatedKillAdv(mobAdvancement);
        BaseAdvancement overHetead10Kills = new OverheatedKill10TimesAdv(overHeatedKill);
        BaseAdvancement overHeteadJustInTime = new OverheatedJustInTimeAdv(overHeatedKill);
        BaseAdvancement starCollapserKillAdv = new StarCollapserKillAdv(mobAdvancement);

        advancementTab.registerAdvancements(root, itemAdvancement, mobAdvancement, adventureAdvancement, challengeAdvancement, overHeatedKill, overHetead10Kills, overHeteadJustInTime, starCollapserKillAdv);
        advancementTab.getEventManager().register(advancementTab, PlayerLoadingCompletedEvent.class, event -> {
            advancementTab.showTab(event.getPlayer());
            advancementTab.grantRootAdvancement(event.getPlayer());
        });

    }

}