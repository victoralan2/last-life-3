package net.olimpium.last_life_iii.utils;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class EntityNBTManager {
	public static <V> boolean writeNBT(LivingEntity entity, PersistentDataType persistentDataType, V value , String fieldName){
		try {
			entity.getPersistentDataContainer().set(new NamespacedKey(Last_life_III.getPlugin(), fieldName), persistentDataType, value);
			return true;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public static <V> V readNBT(LivingEntity entity,PersistentDataType persistentDataType, String fieldName, Class<V> clazz){
		try {
			return clazz.cast(entity.getPersistentDataContainer().get(new NamespacedKey(Last_life_III.getPlugin(), fieldName), persistentDataType));
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}