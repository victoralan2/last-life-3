package net.olimpium.last_life_iii.mobs;

import net.olimpium.last_life_iii.utils.EXPUtils;
import net.olimpium.last_life_iii.utils.EntityNBTManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataType;

public class EspectroDeCodicia implements Listener {
	@EventHandler
	public void onSpawn(EntitySpawnEvent e){
		if(e.getEntity() instanceof Phantom){
			if (RandomMobNumber.rngNumb <= 50 && RandomMobNumber.rngNumb > 0){
				Phantom phantom = (Phantom) e.getEntity();
				phantom.setCustomName(ChatColor.GOLD+"Espectro de la Codicia");
				phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
				phantom.setHealth(40);
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
			float expAfter = EXPUtils.getPlayerExp(player);
			float levelEXP = expBefore-expAfter;
			Bukkit.broadcastMessage("EXP BEFORE: " + expBefore);
			Bukkit.broadcastMessage("EXP AFTER: " + expAfter);
			if(EntityNBTManager.readNBT((LivingEntity) e.getDamager(),PersistentDataType.FLOAT,"EXPStolen",Float.class) != null){
				float levelsStolen = EntityNBTManager.readNBT((LivingEntity) e.getDamager(), PersistentDataType.FLOAT,"EXPStolen",Float.class);
				EntityNBTManager.writeNBT((LivingEntity) e.getDamager(), PersistentDataType.FLOAT, levelEXP + levelsStolen,"EXPStolen");
				Bukkit.broadcastMessage("2.Actual EXP: "+levelsStolen+levelEXP);
			}else{
				EntityNBTManager.writeNBT((LivingEntity) e.getDamager(), PersistentDataType.FLOAT,levelEXP,"EXPStolen");
				Bukkit.broadcastMessage("1.Actual EXP: "+levelEXP);
			}
		}
	}
}
