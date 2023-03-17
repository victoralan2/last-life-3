package net.olimpium.last_life_iii.Enchants;


import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class precisionEnchant extends Enchantment implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addUnsafeEnchantment(Last_life_III.precisionEnchant, 1);
        ItemMeta meta = bow.getItemMeta();
        List lore = new ArrayList<>();
        lore.add(0, ChatColor.GRAY + "Precisión");
        meta.setLore(lore);
        bow.setItemMeta(meta);
        e.getPlayer().getInventory().addItem(bow);
    }

    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent e){
        if (e.getEntity() instanceof Player){
            if (e.getBow().getEnchantments().containsKey(Enchantment.getByKey(Last_life_III.precisionEnchant.getKey()))){
                e.getProjectile().setGravity(false);
                e.getProjectile().setVelocity(e.getEntity().getEyeLocation().getDirection().multiply(e.getForce()*3));

            }
        }
    }

    public precisionEnchant(String namespace){
        super(new NamespacedKey(Last_life_III.getPlugin(), namespace));
    }
    @Override
    public String getName() {
        return "precision";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.BOW;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public @NotNull Component displayName(int level) {
        return null;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return null;
    }

    @Override
    public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return null;
    }
}
