package net.olimpium.last_life_iii.mobs;

import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.utils.EXPUtils;
import net.olimpium.last_life_iii.utils.NBTManager;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class EspectroDeCodicia implements Listener {
	@EventHandler
	public void onSpawn(EntitySpawnEvent e){
		if(e.getEntity() instanceof Phantom){
			if (RandomMobNumber.rngNumb <= 50 && RandomMobNumber.rngNumb > 0){
				Phantom phantom = (Phantom) e.getEntity();
				phantom.setCustomName(ChatColor.GOLD+"Espectro de la Codicia");
				phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
				phantom.setHealth(40);

				new BukkitRunnable() {
					@Override
					public void run() {
						if (!e.getEntity().isDead()) {
							e.getEntity().getWorld().spawnParticle(Particle.TOTEM, e.getEntity().getLocation(), 1, 0.5, 0.1, 0.5, 0);
							e.getEntity().getWorld().spawnParticle(Particle.COMPOSTER, e.getEntity().getLocation(), 5, 0.5, 0.1, 0.5, 0);
						}
					}
				}.runTaskTimer(Last_life_III.getPlugin(), 0, 1);
			}
		}
	}
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager().getCustomName() == null) return;
		if(!(e.getDamager().getCustomName().equals(ChatColor.GOLD+"Espectro de la Codicia"))) return;
		if(e.getEntity() instanceof Player player){
			if(player.getLevel()<1) return;
			float expBefore = EXPUtils.getPlayerExp(player);
			player.setLevel(player.getLevel()-1);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,0);
			float expAfter = EXPUtils.getPlayerExp(player);
			float levelEXP = expBefore-expAfter;

			if(NBTManager.readNBT((LivingEntity) e.getDamager(),PersistentDataType.FLOAT,"EXPStolen",Float.class) != null){
				float levelsStolen = NBTManager.readNBT((LivingEntity) e.getDamager(), PersistentDataType.FLOAT,"EXPStolen",Float.class);
				NBTManager.writeNBT((LivingEntity) e.getDamager(), PersistentDataType.FLOAT, levelEXP + levelsStolen,"EXPStolen");
			}else{
				NBTManager.writeNBT((LivingEntity) e.getDamager(), PersistentDataType.FLOAT,levelEXP,"EXPStolen");
			}
		}
	}
	@EventHandler
	public void onDeath(EntityDeathEvent e){
		if (e.getEntity().getCustomName() == null) return;
		if(!(e.getEntity().getCustomName().equals(ChatColor.GOLD+"Espectro de la Codicia"))) return;
		e.setDroppedExp(Math.round(NBTManager.readNBT(e.getEntity(), PersistentDataType.FLOAT,"EXPStolen",Float.class)));
	}
}
