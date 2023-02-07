package net.olimpium.last_life_iii.mobs;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Silverfish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomMobSpawners {
    public static List<UUID> artificalUUIDs = new ArrayList<>();
    public static boolean isEntityArtificial(UUID uuid){
        try {
            if (!artificalUUIDs.isEmpty()){
                return artificalUUIDs.contains(uuid);
            } else {
                return false;
            }
        } catch (Exception ex){
        }
        return false;
    }
    public static Entity OverheatedCreeperSpawn(Location loc){
        Entity entity = loc.getWorld().spawnEntity(loc, EntityType.CREEPER);
        entity.setCustomName(net.md_5.bungee.api.ChatColor.of("#FF4500") + "Over"+ net.md_5.bungee.api.ChatColor.of("#FF8300") + "heated" + net.md_5.bungee.api.ChatColor.of("#B7AC44") + " Creeper");
        artificalUUIDs.add(entity.getUniqueId());
        return entity;
    }

    public static Entity MobBombSpawn(Location loc) {
        Entity entity = loc.getWorld().spawnEntity(loc, EntityType.CREEPER);
        entity.setCustomName(ChatColor.YELLOW + "Bomba de mobs");
        artificalUUIDs.add(entity.getUniqueId());
        return entity;

    }
    public static Entity FireworkCreeperSpawn(Location loc){
        Entity entity = loc.getWorld().spawnEntity(loc, EntityType.CREEPER);
        entity.setCustomName(ChatColor.DARK_RED + "Firework Creeper");
        artificalUUIDs.add(entity.getUniqueId());

        return entity;
    }

    public static Entity CatalyzerSpawn(Location loc){
        Pillager entity = (Pillager) loc.getWorld().spawnEntity(loc, EntityType.PILLAGER);
        ItemStack banner = new ItemStack(Material.WHITE_BANNER, 1);
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.PURPLE, PatternType.BRICKS));

        BannerMeta bannerMeta = (BannerMeta)banner.getItemMeta();

        bannerMeta.setPatterns(patterns);

        banner.setItemMeta(bannerMeta);
        entity.getEquipment().setHelmet(banner);
        entity.setCustomName(ChatColor.DARK_PURPLE + "Catalyzer");
        Catalyzer.list.add(entity.getUniqueId());
        artificalUUIDs.add(entity.getUniqueId());

        return entity;

    }

    public static Entity DifractorSpawner(Location loc){
        Pillager entity = (Pillager) loc.getWorld().spawnEntity(loc, EntityType.PILLAGER);
        ItemStack banner = new ItemStack(Material.WHITE_BANNER, 1);
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.PURPLE, PatternType.BRICKS));

        BannerMeta bannerMeta = (BannerMeta)banner.getItemMeta();

        bannerMeta.setPatterns(patterns);

        banner.setItemMeta(bannerMeta);
        entity.getEquipment().setHelmet(banner);
        entity.setCustomName(ChatColor.DARK_PURPLE + "Catalyzer");
        Catalyzer.list.add(entity.getUniqueId());
        artificalUUIDs.add(entity.getUniqueId());

        return entity;

    }

    public static Entity EscorpionArenaSpawn(Location loc) throws NullPointerException{
    Location location = new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5);
        Silverfish entity = (Silverfish) loc.getWorld().spawnEntity(location, EntityType.SILVERFISH);

        entity.setCustomName(ChatColor.YELLOW+"Tyrius");
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(1);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, 4, true, true));
        entity.setCustomNameVisible(true);
        artificalUUIDs.add(entity.getUniqueId());

        return entity;
    }
    public static Entity EscorpionArenaRojaSpawn(Location loc) throws NullPointerException{
        Location location = new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5);
        Silverfish entity = (Silverfish) loc.getWorld().spawnEntity(location, EntityType.SILVERFISH);

        entity.setCustomName(ChatColor.GOLD+"Alacrán Dorado");
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
        entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(2);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, 4, true, true));
        entity.setCustomNameVisible(true);
        artificalUUIDs.add(entity.getUniqueId());

        return entity;
    }
}
