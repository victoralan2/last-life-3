package net.olimpium.last_life_iii.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum LastItem {
    TREASURE_SHOVEL,
    MIDAS_SWORD,
    MIDAS_PICKAXE,
    TEMPORAL_INHIBITOR,
    TOTEM_RECALL;

    public ItemStack getItemStack(){
        List<String> lore = new ArrayList<>();
        String name  = "";
        ItemStack item = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta itemMeta = null;
        switch (this){
            case TREASURE_SHOVEL:
                name = ChatColor.GOLD+"Buscatesoros";
                item.setType(Material.IRON_SHOVEL);
                itemMeta = item.getItemMeta();

                lore.add(ChatColor.GRAY.toString()+ChatColor.ITALIC+"Se dice que esta antigua herramienta");
                lore.add(ChatColor.GRAY.toString()+ChatColor.ITALIC+"tiene el poder de descubrir tesoros");
                lore.add(ChatColor.GRAY.toString()+ChatColor.ITALIC+"ocultos en las arenas del desierto.");
                break;
            case MIDAS_SWORD:
                name = ChatColor.GOLD+"Espada de Midas";
                item.setType(Material.GOLDEN_SWORD);
                itemMeta = item.getItemMeta();

                itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
                lore.add(ChatColor.GRAY + "Avaricia X");
                lore.add(ChatColor.AQUA + "Una espada cuyo poder");
                lore.add(ChatColor.AQUA + "crece tanto como su avaricia");
                lore.add(ChatColor.DARK_GRAY + "Bloques consumidos 0");
                lore.add("");
                lore.add(ChatColor.GRAY + "En la mano principal:");
                lore.add(ChatColor.DARK_GREEN + "Daño de ataque: 0");
                lore.add(ChatColor.DARK_GREEN + "Velocidad de ataque: 1.6");
                itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(),"generic.attackSpeed", -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),"generic.attackDamage", -1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                itemMeta.setUnbreakable(true);
                break;
            case MIDAS_PICKAXE:
                name = ChatColor.GOLD+"Pico de Midas";
                item.setType(Material.GOLDEN_PICKAXE);
                itemMeta = item.getItemMeta();
                break;
            case TEMPORAL_INHIBITOR:

                break;
            case TOTEM_RECALL:
                name = ChatColor.AQUA+"Totem de Reclamo";
                item.setTyoe(Material.TOTEM_OF_UNDYING);

                break;
        }
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);


        return item;
    }
}