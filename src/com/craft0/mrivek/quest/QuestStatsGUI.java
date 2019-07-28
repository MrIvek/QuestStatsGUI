package com.craft0.mrivek.quest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class QuestStatsGUI extends JavaPlugin implements Listener {

	public Quests quests = (Quests) getServer().getPluginManager().getPlugin("Quests");
	public static final ItemStack IN_PROGRESS_QUESTS = ItemBuilder.buildNewItem(Material.BOOK, 1, "§fQuests",
			Arrays.asList("§6In Progress"), false);
	public static final ItemStack NONE = ItemBuilder.buildNewItem(Material.BARRIER, 1, "§cNo quest completed", null,
			false);
	public static final ItemStack NEXT_PAGE = ItemBuilder.buildNewItem(Material.ARROW, 1, "§fNext Page", null, false);
	public static final ItemStack PREVIOUS_PAGE = ItemBuilder.buildNewItem(Material.ARROW, 1, "§fPrevious Page", null,
			false);
	public static final ItemStack CLOSE = ItemBuilder.buildNewItem(Material.BARRIER, 1, "§cClose", null, false);
	public static final Inventory gui = Bukkit.createInventory(null, 54, "Quest Stats");
	public static List<Inventory> inventories = new ArrayList<>();
	public static int inventorySize = 54;
	public static int emptySlots = 45;
	public static Inventory MENU = Bukkit.createInventory(null, 27, "Quest Stats Menu");

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("questgui").setExecutor(new QuestGUICommand(this));

		if (quests == null) {
			getServer().getLogger().info("You are missing §cQuests§r plugin");
			this.onDisable();
		}

		getServer().getLogger().info("Plugin Enabled.");
		super.onEnable();
	}

	@Override
	public void onDisable() {
		getServer().getLogger().info("Plugin Disabled.");
		super.onDisable();
	}

	@EventHandler
	public void questInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;

		if (event.getCurrentItem() == null || event.getCurrentItem().equals(new ItemStack(Material.AIR))) {
			return;
		}

		if (event.getClickedInventory().equals(MENU)) {
			event.setCancelled(true);

			Map<Enchantment, Integer> enchantments = new HashMap<>();
			enchantments.put(Enchantment.LUCK, 1);

			ItemStack completedQuests = ItemBuilder.build(Material.BOOK, 1, "§fQuests", Arrays.asList("§aCompleted"),
					false, ItemFlag.HIDE_ENCHANTS, enchantments);
			if (event.getCurrentItem().equals(completedQuests)) {
				Player player = (Player) event.getWhoClicked();
				Quester quester = quests.getQuester(player.getUniqueId());
				int numberOfItemsToStore = quester.getCompletedQuests().size();
				int neededInventories = Math.max(1, (int) Math.ceil((double) numberOfItemsToStore / emptySlots));

				if (quester.getCompletedQuests().isEmpty()) {
					int[] positions = { 0, 1, 2, 6, 7, 8, 9, 10, 11, 15, 16, 17, 21, 22, 23, 30, 31, 32, 36, 37, 38, 42,
							43, 44, 45, 46, 47, 51, 52, 53 };
					for (int position : positions) {
						gui.setItem(position, NONE);
					}
					player.openInventory(gui);
				} else {
					for (int i = 0; i < neededInventories; i++) {
						inventories.add(Bukkit.createInventory(null, inventorySize, "Completed Quests P" + i));
					}

					for (int i = 0; i < numberOfItemsToStore; i++) {
						int inventoryIndex = i / emptySlots;
						Inventory inventory = inventories.get(inventoryIndex);

						ItemStack questItem = new ItemStack(Material.BOOK, 1);
						ItemMeta questItemMeta = questItem.getItemMeta();
						questItemMeta.setDisplayName("§f" + quester.getCompletedQuests().get(i));
						questItemMeta.setLore(Arrays.asList("§aCompleted"));
						questItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
						questItemMeta.addEnchant(Enchantment.LUCK, 3, false);
						questItem.setItemMeta(questItemMeta);
						if (!inventory.contains(questItem)) {
							inventory.addItem(questItem);
						}

						if (inventoryIndex != neededInventories - 1) {
							inventory.setItem(53, NEXT_PAGE);
						}

						if (inventoryIndex != 0) {
							inventory.setItem(45, PREVIOUS_PAGE);
						}

						inventory.setItem(49, CLOSE);
					}

					player.openInventory(inventories.get(0));
				}
			}
			Quester quester = quests.getQuester(event.getWhoClicked().getUniqueId());
			ItemStack questPoints = ItemBuilder.buildNewItem(Material.END_CRYSTAL, 1, "§fQuests §dPoints",
					Arrays.asList("§d - " + quester.getQuestPoints() + " - "), false);
			if (event.getCurrentItem().equals(questPoints)) {
				event.setCancelled(true);
				return;
			}
		}

		Quester quester = quests.getQuester(event.getWhoClicked().getUniqueId());
		for (String quest : quester.getCompletedQuests()) {
			ItemStack questItemStack = new ItemStack(Material.BOOK, 1);
			ItemMeta questItemMeta = questItemStack.getItemMeta();
			questItemMeta.setDisplayName("§f" + quest);
			questItemMeta.setLore(Arrays.asList("§aCompleted"));
			questItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			questItemMeta.addEnchant(Enchantment.LUCK, 3, false);
			questItemStack.setItemMeta(questItemMeta);

			if (event.getCurrentItem().equals(NONE) || event.getCurrentItem().equals(questItemStack)) {
				event.setCancelled(true);
				return;
			}
		}

		if (event.getCurrentItem().equals(CLOSE)) {
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
			return;
		}

		if (event.getCurrentItem().equals(NEXT_PAGE)) {
			event.setCancelled(true);
			event.getWhoClicked().openInventory(inventories.get(inventories.indexOf(event.getClickedInventory()) + 1));
		}

		if (event.getCurrentItem().equals(PREVIOUS_PAGE)) {
			event.setCancelled(true);
			event.getWhoClicked().openInventory(inventories.get(inventories.indexOf(event.getClickedInventory()) - 1));
		}

		return;
	}
}
