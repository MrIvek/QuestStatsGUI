package com.craft0.mrivek.quest;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

	public static ItemStack buildNewItem(Material material, int amount, String displayName, List<String> lore,
			boolean unbreakable) {
		ItemStack itemStack = new ItemStack(material, amount);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(displayName);

		if (lore != null) {
			itemMeta.setLore(lore);
		}

		itemMeta.setUnbreakable(unbreakable);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack build(Material material, int amount, String displayName, List<String> lore,
			boolean unbreakable, ItemFlag flags, Map<Enchantment, Integer> enchantments) {
		ItemStack itemStack = new ItemStack(material, amount);
		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(displayName);

		if (lore != null) {
			itemMeta.setLore(lore);
		}

		if (flags != null) {
			itemMeta.addItemFlags(flags);
		}

		if (enchantments != null) {
			for (Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()) {
				itemMeta.addEnchant(enchant.getKey(), enchant.getValue(), false);
			}
		}
		
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

}
