package net.olimpium.last_life_iii.Items;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class InhibidorTemporal implements Listener {

    public static Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

    public static Team hoverTeam;
    public static Team green;
    public static Team darkgreen;
    public static Team noStasisTeam;

    public static Map<UUID,Double> DamageDelt = new HashMap<>();
    public static Map<UUID, Double> HaveToDamage = new HashMap<>();
    public static Map<Player, UUID> WhoDamage = new HashMap<>();
    public static Map<UUID, Double> NumbOfHits = new HashMap<>();
    public static Map<UUID, Vector> LastPlayerDirection = new HashMap<>();

    public static Map<UUID, Double> NotLivingNumbOfHits = new HashMap<>();
    public static Map<UUID, Vector> NotLivingLastPlayerDirection = new HashMap<>();
    public static List<UUID> NotLivingHasToKill = new ArrayList<>();

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e){
        stasisChecker(e.getPlayer());
    }

    @EventHandler
    public void onPlayerRightClickAtEntity(PlayerInteractAtEntityEvent e){
        stasisChecker(e.getPlayer());
    }

    public void stasisChecker(Player player){

        try {
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Inhibidor Temporal") ||
                   player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Inhibidor Temporal")){
                if (player.getCooldown(Material.STRAY_SPAWN_EGG) > 0)
                    return;
                RayTraceResult rayTracePlayer = player.getWorld().rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), player.getClientViewDistance()*16,FluidCollisionMode.NEVER,true ,0, entity -> !entity.equals(player));
                if (rayTracePlayer.getHitEntity() == null){

                    if (!rayTracePlayer.getHitBlock().getType().isOccluding()){

                        Location hitBlock = rayTracePlayer.getHitPosition().toLocation(player.getWorld());
                        for (int i = player.getClientViewDistance()*16; i>0; i--){

                            RayTraceResult rayTrace = player.getWorld().rayTrace(hitBlock, player.getEyeLocation().getDirection(), player.getClientViewDistance()*16,FluidCollisionMode.NEVER,true ,0, entity -> !entity.equals(player));
                            if (rayTrace.getHitEntity() == null){
                                if (!rayTrace.getHitBlock().getType().isOccluding()){
                                    hitBlock = rayTrace.getHitPosition().toLocation(player.getWorld());
                                } else {
                                    break;
                                }
                            } else {
                                if (rayTrace.getHitEntity() instanceof Player)
                                    return;

                                if (darkgreen.hasEntry(rayTrace.getHitEntity().getUniqueId().toString()) || green.hasEntry(rayTrace.getHitEntity().getUniqueId().toString()) || noStasisTeam.hasEntry(rayTrace.getHitEntity().getUniqueId().toString())) return;
                                player.setCooldown(Material.STRAY_SPAWN_EGG, 60);
                                EntityType type = rayTrace.getHitEntity().getType();
                                if (rayTrace.getHitEntity() instanceof LivingEntity){
                                    EntityStasis((LivingEntity) rayTrace.getHitEntity(), player);
                                } else if (type == EntityType.ENDER_PEARL || type == EntityType.ARROW || type == EntityType.SPECTRAL_ARROW){
                                    /*Slime slime = (Slime) player.getWorld().spawnEntity(rayTrace.getHitEntity().getLocation(), EntityType.SLIME);
                                    noStasisTeam.addEntry(slime.getUniqueId().toString());
                                    NotLivingHasToKill.add(slime.getUniqueId());
                                    slime.setSize(1);
                                    slime.setInvisible(true);
                                    slime.setAI(false);
                                    slime.setSilent(true);
                                    slime.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 91, 4, false, false));
                                    slime.setCustomName("UUID: " + rayTrace.getHitEntity().getUniqueId());
                                    NotLivingEntityStasis(rayTrace.getHitEntity(), player);*/
                                }
                                break;
                            }
                        }
                    }
                } else {
                    if (rayTracePlayer.getHitEntity() instanceof Player)
                        return;
                    if (darkgreen.hasEntry(rayTracePlayer.getHitEntity().getUniqueId().toString()) || green.hasEntry(rayTracePlayer.getHitEntity().getUniqueId().toString()) || noStasisTeam.hasEntry(rayTracePlayer.getHitEntity().getUniqueId().toString())) return;
                    player.setCooldown(Material.STRAY_SPAWN_EGG, 60);
                    EntityType type = rayTracePlayer.getHitEntity().getType();
                    if (rayTracePlayer.getHitEntity() instanceof LivingEntity){
                        EntityStasis((LivingEntity) rayTracePlayer.getHitEntity(), player);
                    } else if (type == EntityType.ENDER_PEARL || type == EntityType.ARROW || type == EntityType.SPECTRAL_ARROW){
                        /*Slime slime = (Slime) player.getWorld().spawnEntity(rayTracePlayer.getHitEntity().getLocation(), EntityType.SLIME);
                        noStasisTeam.addEntry(slime.getUniqueId().toString());
                        NotLivingHasToKill.add(slime.getUniqueId());
                        slime.setSize(1);
                        slime.setInvisible(true);
                        slime.setAI(false);
                        slime.setSilent(true);
                        slime.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 91, 4, false, false));
                        slime.setCustomName("UUID: " + rayTracePlayer.getHitEntity().getUniqueId());
                        NotLivingEntityStasis(rayTracePlayer.getHitEntity(), player);*/
                    }

                }
            }
        } catch (NullPointerException exception){

        }
    }


    public static void Runnable(){
        if (board.getTeam("HoverStasis") == null){
            hoverTeam = board.registerNewTeam("HoverStasis");
        } else {
            hoverTeam = board.getTeam("HoverStasis");
        }
        if (board.getTeam("GreenStasis") == null){
            green = board.registerNewTeam("GreenStasis");

        } else {
            green = board.getTeam("GreenStasis");
        }
        if (board.getTeam("DarkGStasis") == null){
            green = board.registerNewTeam("DarkGStasis");

        } else {
            green = board.getTeam("DarkGStasis");
        }
        if (board.getTeam("CannotStasis") == null){
            green = board.registerNewTeam("CannotStasis");

        } else {
            green = board.getTeam("CannotStasis");
        }

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Last_life_III.getPlugin(), () -> {

            for (Player player : Bukkit.getServer().getOnlinePlayers()){
                if (!HaveToDamage.isEmpty()){
                    UUID uuid = WhoDamage.get(player);
                    if (uuid != null){
                    Double damage = HaveToDamage.get(uuid);
                    ((LivingEntity)Bukkit.getEntity(uuid)).damage(damage, player);
                    WhoDamage.remove(player);
                    HaveToDamage.remove(uuid);
                    }
                }
                try {
                    if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Inhibidor Temporal") ||
                            player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Inhibidor Temporal")) {
                        if (player.getCooldown(Material.STRAY_SPAWN_EGG) > 0)
                            return;
                        RayTraceResult rayTracePlayer = player.getWorld().rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), player.getClientViewDistance() * 16, FluidCollisionMode.NEVER, true, 0, entity -> !entity.equals(player));
                        if (rayTracePlayer.getHitEntity() == null) {

                            if (!rayTracePlayer.getHitBlock().getType().isOccluding()) {

                                Location hitBlock = rayTracePlayer.getHitPosition().toLocation(player.getWorld());
                                for (int i = player.getClientViewDistance() * 16; i > 0; i--) {

                                    RayTraceResult rayTrace = player.getWorld().rayTrace(hitBlock, player.getEyeLocation().getDirection(), player.getClientViewDistance() * 16, FluidCollisionMode.NEVER, true, 0, entity -> !entity.equals(player));
                                    if (rayTrace.getHitEntity() == null) {
                                        if (!rayTrace.getHitBlock().getType().isOccluding()) {
                                            hitBlock = rayTrace.getHitPosition().toLocation(player.getWorld());
                                        } else {
                                            break;
                                        }
                                    } else {
                                        try {
                                            if (rayTrace.getHitEntity() instanceof Player || !(rayTrace.getHitEntity() instanceof LivingEntity))
                                                return;

                                            LivingEntity entity = (LivingEntity) rayTrace.getHitEntity();
                                            if (!green.hasEntry(entity.getUniqueId().toString()) && !darkgreen.hasEntry(entity.getUniqueId().toString()) && !noStasisTeam.hasEntry(entity.getUniqueId().toString())) {
                                                hoverTeam.addEntry(entity.getUniqueId().toString());
                                                entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2, 10, true, false));
                                            }

                                        } catch (NullPointerException exception) {

                                        }
                                        break;
                                    }
                                }
                            }
                        } else {
                            try {
                                if (rayTracePlayer.getHitEntity() instanceof Player || !(rayTracePlayer.getHitEntity() instanceof LivingEntity))
                                    return;

                                LivingEntity entity = (LivingEntity) rayTracePlayer.getHitEntity();
                                if (!green.hasEntry(entity.getUniqueId().toString()) && !darkgreen.hasEntry(entity.getUniqueId().toString()) && !noStasisTeam.hasEntry(entity.getUniqueId().toString())) {
                                    hoverTeam.addEntry(entity.getUniqueId().toString());
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2, 10, true, false));
                                }
                            } catch (NullPointerException ex) {

                            }
                        }
                    }

                } catch (NullPointerException exception) {
                }
            }
        }, 0L, 1L);
    }

    public void EntityStasis(LivingEntity entity, Player player){
        try {
            Vector oldVelocity = entity.getVelocity();
            entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 96, 20, true, false));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 96, 5, true, false));
            entity.setAI(false);
            entity.setGravity(false);
            new BukkitRunnable() {
                public void run() {
                /*for (int i = 4; i>0; i--){
                    player.playSound(entity.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 50f, 0.67f);
                    try {
                        this.wait(16*50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                    boolean dark = false;
                    for (int i = 8; i > 0; i--) {
                        if (dark) {
                            darkgreen.addEntry(entity.getUniqueId().toString());
                        } else{
                            green.addEntry(entity.getUniqueId().toString());
                        }
                        dark = !dark;
                        player.getWorld().playSound(entity.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2.1f, 0.67f);
                        try {
                            Thread.sleep(8 * 50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 8; i > 0; i--)  {
                        if (dark) {
                            darkgreen.addEntry(entity.getUniqueId().toString());
                        } else{
                            green.addEntry(entity.getUniqueId().toString());
                        }
                        dark = !dark;
                        player.getWorld().playSound(entity.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2.1f, 0.67f);
                        try {
                            Thread.sleep(4 * 50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    if (DamageDelt.containsKey(entity.getUniqueId())){
                        WhoDamage.putIfAbsent(player, entity.getUniqueId());
                        HaveToDamage.putIfAbsent(entity.getUniqueId(), DamageDelt.get(entity.getUniqueId()));
                        DamageDelt.remove(entity.getUniqueId());
                    }
                    if (NumbOfHits.containsKey(entity.getUniqueId())){
                        entity.setVelocity(oldVelocity.add(LastPlayerDirection.get(entity.getUniqueId()).multiply(NumbOfHits.get(entity.getUniqueId())).multiply(0.75f)));
                        NumbOfHits.remove(entity.getUniqueId());
                        LastPlayerDirection.remove(entity.getUniqueId());
                    } else {
                        entity.setVelocity(oldVelocity);
                    }
                    player.getWorld().playSound(entity.getLocation(), Sound.ITEM_SHIELD_BREAK, 2.1f, 0.67f);
                    entity.setAI(true);
                    entity.setGravity(true);
                    if (darkgreen.hasEntry(entity.getUniqueId().toString()))
                    {
                        darkgreen.removeEntry(entity.getUniqueId().toString());
                    } else{
                        green.removeEntry(entity.getUniqueId().toString());
                    }
                }
            }.runTaskAsynchronously(Last_life_III.getPlugin());
        } catch (NullPointerException exception){

        }
    }
    public void NotLivingEntityStasis(Entity entity, Player player){
        try {
            Vector oldVelocity = entity.getVelocity().clone();
            entity.setVelocity(new Vector(0,0,0));
            entity.setGlowing(true);
            entity.setGravity(false);
            new BukkitRunnable() {
                public void run() {
                /*for (int i = 4; i>0; i--){
                    player.playSound(entity.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 50f, 0.67f);
                    try {
                        this.wait(16*50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
                    boolean dark = false;
                    for (int i = 8; i > 0; i--) {
                        if (dark) {
                            darkgreen.addEntry(entity.getUniqueId().toString());
                        } else{
                            green.addEntry(entity.getUniqueId().toString());
                        }
                        dark = !dark;
                        player.getWorld().playSound(entity.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2.1f, 0.67f);
                        try {
                            Thread.sleep(8 * 50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 8; i > 0; i--) {
                        if (dark) {
                            darkgreen.addEntry(entity.getUniqueId().toString());
                        } else {
                            green.addEntry(entity.getUniqueId().toString());
                        }
                        dark = !dark;
                        player.getWorld().playSound(entity.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2.1f, 0.67f);
                        try {
                            Thread.sleep(4 * 50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (UUID uuid : NotLivingHasToKill){
                        Bukkit.getEntity(uuid).setVelocity(new Vector(0, 100, 0));
                        HaveToDamage.put(uuid, 1000000d);
                        WhoDamage.put((Player) Bukkit.getOnlinePlayers().toArray()[0], uuid);
                    }

                    if (NotLivingLastPlayerDirection.containsKey(entity.getUniqueId())){
                        entity.setVelocity(oldVelocity.add(NotLivingLastPlayerDirection.get(entity.getUniqueId()).multiply(NotLivingNumbOfHits.get(entity.getUniqueId()))));
                        NotLivingLastPlayerDirection.remove(entity.getUniqueId());
                        NotLivingLastPlayerDirection.remove(entity.getUniqueId());
                    } else {
                        entity.setVelocity(oldVelocity);
                    }
                    player.getWorld().playSound(entity.getLocation(), Sound.ITEM_SHIELD_BREAK, 2.1f, 0.67f);
                    entity.setGravity(true);
                    entity.setGlowing(false);
                    if (darkgreen.hasEntry(entity.getUniqueId().toString()))
                    {
                        darkgreen.removeEntry(entity.getUniqueId().toString());
                    } else{
                        green.removeEntry(entity.getUniqueId().toString());
                    }

                }
            }.runTaskAsynchronously(Last_life_III.getPlugin());
        } catch (NullPointerException exception){

        }
    }
    @EventHandler
    public void onDamageDelt(EntityDamageByEntityEvent e){


        if (((LivingEntity) e.getEntity()) instanceof Player) return;
        if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) return;
        LivingEntity damaged = (LivingEntity) e.getEntity();

        if (darkgreen.hasEntry(damaged.getUniqueId().toString()) || green.hasEntry(damaged.getUniqueId().toString())){
            if (DamageDelt.containsKey(damaged.getUniqueId())){
                DamageDelt.replace(damaged.getUniqueId(), e.getDamage() + DamageDelt.get(damaged.getUniqueId()));
            } else {
                DamageDelt.put(damaged.getUniqueId(), e.getDamage());
            }
            if (NumbOfHits.containsKey(damaged.getUniqueId())){

                NumbOfHits.replace(damaged.getUniqueId(), 1 + NumbOfHits.get(damaged.getUniqueId()));

            } else {
                NumbOfHits.put(damaged.getUniqueId(), 1d);
            }
            if (LastPlayerDirection.containsKey(damaged.getUniqueId())){
                LastPlayerDirection.replace(damaged.getUniqueId(), ((Player)e.getDamager()).getEyeLocation().getDirection());

            } else {
                LastPlayerDirection.put(damaged.getUniqueId(), ((Player)e.getDamager()).getEyeLocation().getDirection());
            }

        }
    }
    //@EventHandler deprecated
    public void onPlayerMoveCamera(PlayerMoveEvent e){
        Player player = e.getPlayer();
        try {
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Inhibidor Temporal") ||
                    player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Inhibidor Temporal")){
                if (player.getCooldown(Material.STRAY_SPAWN_EGG) > 0)
                    return;
                RayTraceResult rayTracePlayer = player.getWorld().rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), player.getClientViewDistance()*16,FluidCollisionMode.NEVER,true ,0, entity -> !entity.equals(player));
                if (rayTracePlayer.getHitEntity() == null){

                    if (!rayTracePlayer.getHitBlock().getType().isOccluding()){

                        Location hitBlock = rayTracePlayer.getHitPosition().toLocation(player.getWorld());
                        for (int i = player.getClientViewDistance()*16; i>0; i--){

                            RayTraceResult rayTrace = player.getWorld().rayTrace(hitBlock, player.getEyeLocation().getDirection(), player.getClientViewDistance()*16,FluidCollisionMode.NEVER,true ,0, entity -> !entity.equals(player));
                            if (rayTrace.getHitEntity() == null){
                                if (!rayTrace.getHitBlock().getType().isOccluding()){
                                    hitBlock = rayTrace.getHitPosition().toLocation(player.getWorld());
                                } else {
                                    break;
                                }
                            } else {
                                try {
                                    LivingEntity entity = (LivingEntity) rayTrace.getHitEntity();
                                    if (!green.hasEntry(entity.getUniqueId().toString()) || !darkgreen.hasEntry(entity.getUniqueId().toString())){
                                    }

                                } catch (NullPointerException exception){

                                }
                                break;
                            }
                        }
                    }
                } else {
                    try{
                        LivingEntity entity = (LivingEntity) rayTracePlayer.getHitEntity();
                        if (!green.hasEntry(entity.getUniqueId().toString()) || !darkgreen.hasEntry(entity.getUniqueId().toString())){

                        }
                    } catch (NullPointerException ex){

                    }
                }
            }

        } catch (NullPointerException exception){
        }
    }
}

