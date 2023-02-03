package net.olimpium.last_life_iii.items;

import com.google.common.collect.Multimap;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NetworkManagerServer;
import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MidasSword implements Listener{

    public static void init() { MidasRecipe(); }

    private static void MidasRecipe(){
        ItemStack item = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Midas Sword");
        item.setItemMeta(im);
        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("midasswordupgrade"), item);
        sr.shape("GGG",
                "GMG",
                "GGG");
        sr.setIngredient('G', Material.GOLD_BLOCK);
        sr.setIngredient('M', Material.GOLDEN_SWORD);
        Bukkit.getServer().addRecipe(sr);
    }
    /*//@EventHandler
    public void onPlayerDragEvent(InventoryDragEvent e) throws NullPointerException{
        Map<Integer, ItemStack> itemlist = e.getNewItems();
        if (e.getInventory().getType() == InventoryType.WORKBENCH) {
        if (itemlist.size() != 1){
            if (itemlist.get(e.getInventorySlots().stream().toArray()[0]).getType().equals(Material.GOLD_BLOCK)) {
                if (e.getInventory().getItem(5) != null){
                    if (e.getInventory().getItem(5).getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Midas Sword")){
                        int goldCount = 0;
                        for (int i = 10; i>=1; i--){
                            if (i != 5){
                                    if (e.getInventory().getItem(i) != null){
                                        if (e.getInventory().getItem(i).getType().equals(Material.GOLD_BLOCK))
                                            goldCount++;
                                    }
                                }
                            }
                            if (goldCount + itemlist.size() == 8){

                                Bukkit.broadcastMessage("YO IT WORKED");
                            }
                        }
                    }
                }
            }
        }

    }
    //@EventHandler
    public void onPlayerClickEvent(InventoryClickEvent e) throws NullPointerException {
        if (e.getClickedInventory().getType().equals(InventoryType.WORKBENCH)){
            if (e.getCursor() != null){
                if (e.getCursor().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Midas Sword") && e.getSlot() == 5){
                    int goldCount = 0;
                    for (int i = 10; i>=1; i--){
                        if (i != 5){
                            if (e.getInventory().getItem(i) != null){
                                if (e.getInventory().getItem(i).getType().equals(Material.GOLD_BLOCK))
                                    goldCount++;
                            }
                        }
                    }
                    if (goldCount == 8){
                        //Bukkit.getScheduler().runTaskLater(Last_life_III.getPlugin(), () -> Bukkit.broadcastMessage("yo it worked"), 5);
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                Bukkit.broadcastMessage(e.getInventory().getViewers().get(0).getOpenInventory().getInventory(0).getItem(0).getType().toString());
                                if (e.getWhoClicked().getOpenInventory().getInventory(0).getItem(0).getType().equals(Material.KNOWLEDGE_BOOK)){
                                    ItemStack MS = new ItemStack(Material.GOLDEN_SWORD);
                                    ItemMeta IM = e.getInventory().getItem(5).getItemMeta();
                                    Multimap<Attribute, AttributeModifier> damageModifier = IM.getAttributeModifiers();
                                    Bukkit.broadcastMessage(damageModifier.toString());
                                    IM.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
                                    //IM.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
                                    MS.setItemMeta(IM);
                                    e.getInventory().getItem(0).setType(Material.GOLDEN_SWORD);
                                }
                            }
                        }.runTaskLater(Last_life_III.getPlugin(), 1);


                    }
                    } else if (e.getCursor().getType().equals(Material.GOLD_BLOCK)){
                        if (e.getInventory().getItem(5) != null){
                            if (e.getInventory().getItem(5).getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Midas Sword")){
                                int goldCount2 = 0;
                                for (int i = 10; i>=1; i--){
                                    if (i != 5){
                                        if (e.getInventory().getItem(i) != null){
                                            if (e.getInventory().getItem(i).getType().equals(Material.GOLD_BLOCK))
                                                goldCount2++;
                                        }
                                    }
                                }
                                if (goldCount2 == 7){
                                    Bukkit.broadcastMessage("Yo it worked");
                                }
                            }
                        }
                }
            }
        }
    }
    //@EventHandler
    public void onPlayerClickEvent2(InventoryClickEvent e) throws NullPointerException {
        if (e.getClickedInventory().getType().equals(InventoryType.WORKBENCH)){
            new BukkitRunnable(){
                @Override
                public void run(){
                    if (e.getWhoClicked().getOpenInventory().getInventory(0).getItem(0) == null)
                        return;
                    if (e.getWhoClicked().getOpenInventory().getInventory(0).getItem(0).getType().equals(Material.KNOWLEDGE_BOOK)){
                        ItemStack MS = new ItemStack(Material.GOLDEN_SWORD);
                        ItemMeta IM = e.getClickedInventory().getItem(5).getItemMeta();
                        IM.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
                        IM.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                        IM.setDisplayName(ChatColor.GOLD + "Midas Sword");
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + "Avaricia X");
                        //lore.add(ChatColor.valueOf("#66ffcc") + "Una espada cuyo poder");
                        //lore.add(ChatColor.valueOf("#66ffcc") + "crece tanto como su avaricia.");
                        IM.setLore(lore);
                        MS.setItemMeta(IM);
                        e.getWhoClicked().getOpenInventory().getInventory(0).setItem(0, MS);
                        Bukkit.getPlayer(e.getWhoClicked().getUniqueId()).updateInventory();
                    }
                }
            }.runTaskLater(Last_life_III.getPlugin(), 2);
        }
    }
    //@EventHandler
    public void onPlayerDragEvent2(InventoryDragEvent e) throws NullPointerException {
        if (e.getInventory().getType().equals(InventoryType.WORKBENCH)){
            new BukkitRunnable(){
                @Override
                public void run(){
                     if (e.getWhoClicked().getOpenInventory().getInventory(0).getItem(0) == null)
                        return;
                    if (e.getWhoClicked().getOpenInventory().getInventory(0).getItem(0).getType().equals(Material.KNOWLEDGE_BOOK)){
                        ItemStack MS = new ItemStack(Material.GOLDEN_SWORD);
                        ItemMeta IM = e.getInventory().getItem(5).getItemMeta();
                        IM.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
                        IM.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                        IM.setDisplayName(ChatColor.GOLD + "Midas Sword");
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + "Avaricia X");
                        lore.add(net.md_5.bungee.api.ChatColor.of("66ffcc") + "Una espada cuyo poder");
                        lore.add(net.md_5.bungee.api.ChatColor.of("66ffcc") + "crece tanto como su avaricia.");
                        IM.setLore(lore);
                        MS.setItemMeta(IM);
                        e.getWhoClicked().getOpenInventory().getInventory(0).setItem(0, MS);
                        Bukkit.getPlayer(e.getWhoClicked().getUniqueId()).updateInventory();

                    }
                }
            }.runTaskLater(Last_life_III.getPlugin(), 2);

        }
    }*/
    @EventHandler
    public void onPlayerCraft(PrepareItemCraftEvent e) throws NullPointerException{
        if (e.getInventory().getType().equals(InventoryType.WORKBENCH)){
            new BukkitRunnable(){
                @Override
                public void run(){
                    if (e.getView().getPlayer().getOpenInventory().getInventory(0).getItem(0) == null)
                        return;
                    if (e.getView().getPlayer().getOpenInventory().getInventory(0).getItem(0).getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Midas Sword")){
                        if (!e.getView().getPlayer().getOpenInventory().getInventory(5).getItem(5).getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Midas Sword"))
                            return;

                        ItemMeta preMSMeta = e.getView().getPlayer().getOpenInventory().getInventory(5).getItem(5).getItemMeta();

                        ItemStack MS = new ItemStack(Material.GOLDEN_SWORD);

                        ItemMeta newMSMeta = e.getInventory().getItem(5).getItemMeta();
                        newMSMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
                        AttributeModifier preAttributeMod = (AttributeModifier) preMSMeta.getAttributeModifiers().get(Attribute.GENERIC_ATTACK_DAMAGE).toArray()[0];
                        newMSMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
                        newMSMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.6-4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                        if (preAttributeMod.getAmount() == -1){
                            newMSMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", -0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                        } else if (preAttributeMod.getAmount() == -.5){
                            newMSMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                        } else {
                            newMSMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", preAttributeMod.getAmount()+.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                        }
                        newMSMeta.setDisplayName(ChatColor.GOLD + "Midas Sword");
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + "Avaricia X");
                        lore.add(net.md_5.bungee.api.ChatColor.of("#66ffcc") + "Una espada cuyo poder");
                        lore.add(net.md_5.bungee.api.ChatColor.of("#66ffcc") + "crece tanto como su avaricia.");
                        if (preAttributeMod.getAmount() == -1){
                            lore.add(net.md_5.bungee.api.ChatColor.DARK_GRAY + "Bloques consumidos: "+ 8);

                        } else {
                            Double bloques = ((preAttributeMod.getAmount()+0.5)*2)*8 + 16;
                            lore.add(net.md_5.bungee.api.ChatColor.DARK_GRAY + "Bloques consumidos: " + Math.round(bloques*10)/10);
                        }
                        lore.add("");
                        lore.add(ChatColor.GRAY+"En la mano principal:");
                        if (preAttributeMod.getAmount() == -1){
                            lore.add(ChatColor.DARK_GREEN +" Daño de ataque: " + 0.5);
                        } else if (preAttributeMod.getAmount() == -.5){
                            lore.add(ChatColor.DARK_GREEN +" Daño de ataque: " + 1);
                        } else {
                            Double damage = (preAttributeMod.getAmount()+0.5 + 1)*10;
                            lore.add(ChatColor.DARK_GREEN +" Daño de ataque: " + damage/10.0f);
                        }
                        lore.add(ChatColor.DARK_GREEN +" Velocidad de ataque: 1.6");
                        newMSMeta.setLore(lore);
                        newMSMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        MS.setItemMeta(newMSMeta);
                        e.getView().getPlayer().getOpenInventory().getInventory(0).setItem(0, MS);
                        Bukkit.getPlayer(e.getView().getPlayer().getUniqueId()).updateInventory();
                    }
                }
            }.runTaskLater(Last_life_III.getPlugin(), 1);

        }
    }
}