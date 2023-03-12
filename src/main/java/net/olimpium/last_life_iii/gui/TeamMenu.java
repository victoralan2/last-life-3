package net.olimpium.last_life_iii.gui;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamMenu extends Gui {
	public TeamMenu(Player player) {
		super(player, "test-gui", "Test Title", 3);
		//player, id, title, row
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {
		Icon nothing = new Icon(Material.BLACK_STAINED_GLASS_PANE);
		nothing.setLore(" ");
		nothing.setName(" ");
		nothing.hideFlags(ItemFlag.HIDE_ATTRIBUTES);
		nothing.setName(" ");
		Skull skull = new ItemStack(Material.);
		Icon enderChest = new Icon(Material.PLAYER_HEAD);
		fillGui(nothing);
		addItem();
		//slot
	}
}
