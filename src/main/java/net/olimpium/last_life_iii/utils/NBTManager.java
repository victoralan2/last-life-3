package net.olimpium.last_life_iii.utils;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class NBTManager {
	public static <V> boolean writeNBT(LivingEntity entity, PersistentDataType persistentDataType, V value , String fieldName){
		try {
			entity.getPersistentDataContainer().set(new NamespacedKey(Last_life_III.getPlugin(), fieldName), persistentDataType, value);
			return true;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static <V> boolean writeNBT(ItemStack itemStack, PersistentDataType persistentDataType, V value , String fieldName){
		try {
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.getPersistentDataContainer().set(new NamespacedKey(Last_life_III.getPlugin(), fieldName), persistentDataType, value);
			itemStack.setItemMeta(itemMeta);

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

	public static <V> V readNBT(ItemStack itemStack,PersistentDataType persistentDataType, String fieldName, Class<V> clazz){
		try {
			return clazz.cast(itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Last_life_III.getPlugin(), fieldName), persistentDataType));
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}