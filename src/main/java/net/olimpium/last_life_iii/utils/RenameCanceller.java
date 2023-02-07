package net.olimpium.last_life_iii.utils;

import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class RenameCanceller implements Listener {
    @EventHandler
    public void onRenameItem(InventoryClickEvent event) throws NullPointerException{
        List<String> BannedItems = new ArrayList();
        BannedItems.add(ChatColor.GOLD+"Buscatesoros");
        BannedItems.add(ChatColor.GOLD+"Maldición de midas");
        BannedItems.add(ChatColor.GOLD+"Pico de Midas");
        BannedItems.add(ChatColor.GOLD+"Espada de Midas");
        BannedItems.add(ChatColor.GREEN + "Inhibidor Temporal");
        BannedItems.add(net.md_5.bungee.api.ChatColor.of("#956B22")+"Pico oxidado");
        BannedItems.add(net.md_5.bungee.api.ChatColor.of("#CCCCCC")+"Escudo Macizo");
        //event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Treasure Shovel") (el object(notnull) es para error handling)
        if (event.getInventory().getType().equals(InventoryType.ANVIL)){
            if (event.getSlot() == 2){
                if(!event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Buscatesoros") || event.getCurrentItem().getEnchantments().containsKey(Enchantment.DIG_SPEED)){
                    if (event.getInventory().getType().equals(InventoryType.ANVIL) && event.getSlotType().equals(InventoryType.SlotType.RESULT)){
                        if(BannedItems.contains(event.getInventory().getItem(0).getItemMeta() == null))
                            return;
                        if(BannedItems.contains(event.getInventory().getItem(0).getItemMeta().getDisplayName())){
                            event.setCancelled(true);
                        }
                    }
                }
            }

        }
    }
    @EventHandler
    public void onRenameMob(PlayerInteractEntityEvent event) throws NullPointerException {
        //Bomb extend ChatColor.RED + "[" + ChatColor.YELLOW + "BOMB"+ ChatColor.RED + "]" +
        List<String> BannedEntities = new ArrayList();
        BannedEntities.add(ChatColor.YELLOW + "Bomba de mobs");

        BannedEntities.add(net.md_5.bungee.api.ChatColor.of("#FF4500") + "Over"+ net.md_5.bungee.api.ChatColor.of("#FF8300") + "heated" + net.md_5.bungee.api.ChatColor.of("#B7AC44") + " Creeper");

        BannedEntities.add(ChatColor.DARK_RED + "Firework Creeper");

        BannedEntities.add(ChatColor.GOLD + "Alacrán Dorado");

        BannedEntities.add(ChatColor.YELLOW + "Tyrius");

        BannedEntities.add(ChatColor.DARK_PURPLE+"Catalyzer");

        BannedEntities.add(ChatColor.BLACK+ChatColor.BOLD.toString()+"esqueleto chungo");

        BannedEntities.add(ChatColor.DARK_PURPLE + "Star"+ ChatColor.LIGHT_PURPLE+" Collapser");
        int size = BannedEntities.size();
        int i = 0;
        while (i < size){
            BannedEntities.add(ChatColor.RED + "[" + ChatColor.YELLOW + "BOMB"+ ChatColor.RED + "] " + BannedEntities.get(i));
            i++;
        }
        //banea cambiar una entidad baneada a otro nombre
        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG)) {
                if (BannedEntities.contains(event.getRightClicked().getName())) {
                    event.setCancelled(true);
                }
                if (BannedEntities.contains(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()) || BannedEntities.contains(event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getDisplayName())) {
                    event.setCancelled(true);
                }
            }
        }
        if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta() != null){
            if (event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.NAME_TAG)) {
                if (BannedEntities.contains(event.getRightClicked().getName())) {
                    event.setCancelled(true);
                }
                if (BannedEntities.contains(event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getDisplayName())) {
                    event.setCancelled(true);
                }
            }
        }


    }
}
