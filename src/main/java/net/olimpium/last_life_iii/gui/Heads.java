package net.olimpium.last_life_iii.gui;

import org.bukkit.inventory.ItemStack;

public enum Heads {

	// EXAMPLE HEADS
	ENDER_CHES("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmU2MzllNDFiZTM2ODgzMGEyYzdiYTNmYmQ2OWUyZWJhZjhjMzkxZDNmNzI1YzI4NDRlOGE1MmI0NTZhZjQyMSJ9fX0=","enderchest"),
	SACK("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNiM2FjZGMxMWNhNzQ3YmY3MTBlNTlmNGM4ZTliM2Q5NDlmZGQzNjRjNjg2OTgzMWNhODc4ZjA3NjNkMTc4NyJ9fX0","sack");

	private ItemStack item;
	private String idTag;
	Heads(String texture, String id) {
		item = SkullManager.createSkull(texture, id);
		idTag = id;
	}

	public ItemStack getItemStack() {
		return item;
	}

	public String getName() {
		return idTag;
	}


}