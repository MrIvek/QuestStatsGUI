package com.craft0.mrivek.queststatsgui;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

	public static final ItemStack NO_QUEST_COMPLETED = buildNewItem(Material.BARRIER, 1, "§cNo quest completed", null, false);
	public static final ItemStack NO_QUEST_IN_PROGRESS = buildNewItem(Material.BARRIER, 1, "§cNo quest in progress", null, false);
	public static final ItemStack NEXT_PAGE = buildNewItem(Material.ARROW, 1, "§fNext Page", null, false);
	public static final ItemStack PREVIOUS_PAGE = buildNewItem(Material.ARROW, 1, "§fPrevious Page", null, false);
	public static final ItemStack CLOSE = buildNewItem(Material.BARRIER, 1, "§cClose", null, false);
	public static final ItemStack AVAILABLE_QUESTS = buildNewItem(Material.BOOKSHELF, 1, "§fAvailable Quests", null,
			false);
	public static final ItemStack IN_PROGRESS_QUESTS = buildNewItem(Material.KNOWLEDGE_BOOK, 1, "§6In progress quests", null,
			false);
	public static final ItemStack COMPLETED_QUESTS = buildNewItem(Material.ENCHANTED_BOOK, 1, "§aCompleted quests", null, false);

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
