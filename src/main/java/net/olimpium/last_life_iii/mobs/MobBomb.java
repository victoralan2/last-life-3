package net.olimpium.last_life_iii.mobs;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MobBomb implements Listener {
    @EventHandler
    public void onCreeperSpawn(CreatureSpawnEvent e){
        if (!CustomMobSpawners.isEntityArtificial(e.getEntity().getUniqueId()))
            if (e.getEntity() instanceof Creeper){
                if (RandomMobNumber.rngNumb <= 66 && RandomMobNumber.rngNumb > 33){
                    e.getEntity().setCustomName(ChatColor.YELLOW + "Bomba de mobs");
                }
            }
    }
    public static int mobsToSpawn = -1;
    ;
    @EventHandler
    public static void onCreeperExplodes(EntityExplodeEvent e) {
        HashMap<String, Map.Entry<Float, Float>> methods = new HashMap<>();
        methods.put("FireworkCreeperSpawn", new AbstractMap.SimpleEntry(10f, 20f));
        methods.put("EscorpionArenaSpawn", new AbstractMap.SimpleEntry(20f, 30f));
        methods.put("EscorpionArenaRojaSpawn", new AbstractMap.SimpleEntry(30f, 40f));
        methods.put("MobBombSpawn", new AbstractMap.SimpleEntry(40f, 50f));
        methods.put("CatalyzerSpawn", new AbstractMap.SimpleEntry(50f, 60f));

        if (e.getEntity().getCustomName().contains(ChatColor.YELLOW + "Bomba de mobs")) {
            Random random = new Random();
            mobsToSpawn = ThreadLocalRandom.current().nextInt(1, 5 + 1);

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        CustomMobSpawners obj = new CustomMobSpawners();
                        Class<?> c = obj.getClass();

                        boolean gotAMob = false;

                        Method method = null;
                        while (!gotAMob) {
                            float MethodNumber = random.nextFloat() * 100;
                            for (Map.Entry<String, Map.Entry<Float, Float>> entry : methods.entrySet()) {
                                if (MethodNumber >= (entry.getValue()).getKey() && MethodNumber <= (entry.getValue()).getValue()) {
                                    method = c.getDeclaredMethod(entry.getKey(), Location.class);
                                    gotAMob = true;
                                    break;
                                }
                            }

                            Entity entity = (Entity) method.invoke(obj, e.getLocation());

                            entity.setCustomNameVisible(true);
                            entity.setCustomName(ChatColor.RED + "[" + ChatColor.YELLOW + "BOMB" + ChatColor.RED + "] " + "" + entity.getCustomName() + "");
                            entity.setVelocity(new Vector((new Random()).nextDouble(), 1d, (new Random()).nextDouble()));
                            mobsToSpawn--;
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                        if (mobsToSpawn < 0) {
                            this.cancel();
                        }
                    }
            }.runTaskTimer(Last_life_III.getPlugin(), 0L, 1L);
        }

    }
}
