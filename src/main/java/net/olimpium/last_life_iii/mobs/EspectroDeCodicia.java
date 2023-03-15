package net.olimpium.last_life_iii.mobs;

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
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			if(player.getLevel()<1) return;
			player.setLevel(player.getLevel()-1);
			Bukkit.broadcastMessage(EntityNBTManager.readNBT((LivingEntity) e.getDamager(),PersistentDataType.INTEGER,"levelStolen",Integer.class).toString());
			if(EntityNBTManager.readNBT((LivingEntity) e.getDamager(),PersistentDataType.INTEGER,"levelStolen",Integer.class) != null){
				int levelsStolen = EntityNBTManager.readNBT((LivingEntity) e.getDamager(),PersistentDataType.INTEGER,"levelStolen",Integer.class);
				EntityNBTManager.writeNBT((LivingEntity) e.getDamager(), PersistentDataType.INTEGER,levelsStolen+1,"levelStolen");
				Bukkit.broadcastMessage(levelsStolen+"");
			}else{
				EntityNBTManager.writeNBT((LivingEntity) e.getDamager(), PersistentDataType.INTEGER,1,"levelStolen");
				Bukkit.broadcastMessage("1");
			}
		}
	}
}
