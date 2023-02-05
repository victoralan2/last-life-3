package net.olimpium.last_life_iii.Items.Magic;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MagicWandT1Inventory implements Listener {
    private static HashMap<UUID, Inventory> inventoryHashMap = new HashMap<>();
    @EventHandler
    public void onClickWithWand(PlayerInteractEvent e){
        if (e.getItem() == null) return;
        if (e.getItem().getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Varita de " + ChatColor.DARK_PURPLE + "Físioxs")){
            if (inventoryHashMap.containsKey(e.getPlayer().getUniqueId())){
                Bukkit.broadcastMessage(inventoryHashMap.get(e.getPlayer().getUniqueId()).getItem(1).getType().toString());
                e.getPlayer().openInventory(inventoryHashMap.get(e.getPlayer().getUniqueId()));
            } else {
                Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE + "Varita de " + ChatColor.DARK_PURPLE + "Físiox");
                initializeItems(inv);
                e.getPlayer().openInventory(inv);
            }
        }
    }

    public void initializeItems(Inventory inv) {
        ItemStack background = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMetaBackground = background.getItemMeta();
        itemMetaBackground.setDisplayName(" ");
        background.setItemMeta(itemMetaBackground);
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, background);
        }
    }
    public static void saveHashMap() throws IOException {
        for (UUID uuid : inventoryHashMap.keySet()){
            File f = new File(Last_life_III.getPlugin().getDataFolder().getAbsolutePath() + "/Players",  uuid + ".yml");
            FileConfiguration c = YamlConfiguration.loadConfiguration(f);
            c.set("magic.t1.inventory.content",inventoryHashMap.get(uuid).getContents());
            c.set("magic.t1.inventory.size", inventoryHashMap.get(uuid).getSize());
            c.save(f);
        }
    }

    public static void loadHashMap() {
        File directory = new File(Last_life_III.getPlugin().getDataFolder().getAbsolutePath() + "/Players");
        File[] files = directory.listFiles();
        //inventoryHashMap.clear();
        for (int i = 0; i < files.length; i++){
            if (files[i].isFile()){
                UUID fileUUID = UUID.fromString(files[i].getName().split(".yml")[0]);
                File f = files[i];
                FileConfiguration c = YamlConfiguration.loadConfiguration(f);
                ItemStack[] content = ((List<ItemStack>) c.get("magic.t1.inventory.content")).toArray(new ItemStack[0]);
                int size = ((int) c.get("magic.t1.inventory.size"));
                Inventory inventory = Bukkit.createInventory(Bukkit.getPlayer(fileUUID), size, ChatColor.LIGHT_PURPLE + "Varita de " + ChatColor.DARK_PURPLE + "Físiox");
                for (int o = 0; o < content.length; o++) {
                    inventory.setItem(o, content[i]);
                }
                inventoryHashMap.put(fileUUID, inventory);
            }
        }
    }

    public static void autoSave(){
        Last_life_III.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Last_life_III.getPlugin(),() ->{
            try {
                saveHashMap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 1200, 1200);
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(Material material, String name, String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        //item.setItemMeta(meta);

        return item;
    }
    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "Varita de " + ChatColor.DARK_PURPLE + "Físiox")) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase( " ")){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "Varita de " + ChatColor.DARK_PURPLE + "Físiox")) {
            if (inventoryHashMap.containsKey(e.getPlayer().getUniqueId())){
                inventoryHashMap.replace(e.getPlayer().getUniqueId(), e.getInventory());
            } else {
                inventoryHashMap.put(e.getPlayer().getUniqueId(), e.getInventory());
            }
        }

    }
}
