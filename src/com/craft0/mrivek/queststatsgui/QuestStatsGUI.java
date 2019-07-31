package com.craft0.mrivek.queststatsgui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public final class QuestStatsGUI extends JavaPlugin implements Listener {

	public Quests quests = (Quests) getServer().getPluginManager().getPlugin("Quests");

	public static List<Inventory> inventories = new ArrayList<Inventory>();
	public static int inventorySize = 54;
	public static int emptySlots = 45;
	public static Inventory MENU = Bukkit.createInventory(null, 27, "Quests Menu");

	public Inventory AVAILABLE_QUESTS_INVENTORY = Bukkit.createInventory(null, 54, "Available Quests");
	public Inventory IN_PROGRESS_QUESTS_INVENTORY = Bukkit.createInventory(null, 54, "In Progress Quests");
	public Inventory COMPLETED_QUESTS_INVENTORY = Bukkit.createInventory(null, 54, "Completed Quests");
	private String pluginName = "[" + getName() + "] ";

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("questgui").setExecutor(new QuestGUICommand(this));

		getServer().getLogger().info(pluginName + "Plugin Enabled.");
		super.onEnable();
	}

	@Override
	public void onDisable() {
		getServer().getLogger().info(pluginName + "Plugin Disabled.");
		super.onDisable();
	}

	@EventHandler
	public void questInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Quester quester = quests.getQuester(event.getWhoClicked().getUniqueId());
		Inventory clickedInventory = event.getClickedInventory();
		ItemStack clickedItem = event.getCurrentItem();

		if (clickedInventory == null)
			return;

		if (clickedItem == null || clickedItem.equals(new ItemStack(Material.AIR)))
			return;

		if (clickedInventory.equals(MENU)) {
			event.setCancelled(true);

			if (clickedItem.equals(ItemBuilder.IN_PROGRESS_QUESTS)) {
				for (Quest quest : quester.getCurrentQuests().keySet()) {
					ItemStack currentQuestItemStack = new ItemStack(Material.ENCHANTED_BOOK, 1);
					ItemMeta currentQuestItemMeta = currentQuestItemStack.getItemMeta();
					currentQuestItemMeta.setDisplayName("§f" + quest.getName());
					currentQuestItemMeta.setLore(Arrays.asList("§7Click to quit the quest."));
					currentQuestItemStack.setItemMeta(currentQuestItemMeta);
					if (!IN_PROGRESS_QUESTS_INVENTORY.contains(currentQuestItemStack)) {
						IN_PROGRESS_QUESTS_INVENTORY.addItem(currentQuestItemStack);
					}
				}

				player.openInventory(IN_PROGRESS_QUESTS_INVENTORY);
				return;
			}

			if (clickedItem.equals(ItemBuilder.COMPLETED_QUESTS)) {
				inventories.clear();
				int numberOfItemsToStore = quester.getCompletedQuests().size();
				int neededInventories = Math.max(1, (int) Math.ceil((double) numberOfItemsToStore / emptySlots));

				for (int i = 0; i < neededInventories; i++) {
					inventories.add(Bukkit.createInventory(null, inventorySize, "Completed Quests P" + i));
				}

				for (int i = 0; i < numberOfItemsToStore; i++) {
					int inventoryIndex = i / emptySlots;
					Inventory inventory = inventories.get(inventoryIndex);

					ItemStack questItemStack = new ItemStack(Material.BOOK, 1);
					ItemMeta questItemMeta = questItemStack.getItemMeta();
					questItemMeta.setDisplayName("§f" + quester.getCompletedQuests().get(i));
					questItemMeta.setLore(Arrays.asList("§aCompleted"));
					questItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					questItemMeta.addEnchant(Enchantment.LUCK, 3, false);
					questItemStack.setItemMeta(questItemMeta);
					if (!inventory.contains(questItemStack)) {
						inventory.addItem(questItemStack);
					}

					if (inventoryIndex != neededInventories - 1) {
						inventory.setItem(53, ItemBuilder.NEXT_PAGE);
					}

					if (inventoryIndex != 0) {
						inventory.setItem(45, ItemBuilder.PREVIOUS_PAGE);
					}

					inventory.setItem(49, ItemBuilder.CLOSE);
				}

				if (clickedItem.equals(ItemBuilder.NEXT_PAGE)) {
					event.getWhoClicked()
							.openInventory(inventories.get(inventories.indexOf(event.getClickedInventory()) + 1));
					return;
				}

				if (clickedItem.equals(ItemBuilder.PREVIOUS_PAGE)) {
					event.getWhoClicked()
							.openInventory(inventories.get(inventories.indexOf(event.getClickedInventory()) - 1));
					return;
				}

				player.openInventory(inventories.get(0));
				return;
			}

			ItemStack questPoints = ItemBuilder.buildNewItem(Material.END_CRYSTAL, 1, "§fQuests §dPoints",
					Arrays.asList("§d - " + quester.getQuestPoints() + " - "), false);
			if (event.getCurrentItem().equals(questPoints)) {
				event.setCancelled(true);
				return;
			}

			if (clickedItem.equals(ItemBuilder.AVAILABLE_QUESTS)) {
				event.setCancelled(true);
				Inventory inventory = Bukkit.createInventory(null, 54, "Available Quests");
				for (Quest quest : quests.getQuests()) {
					ItemStack availableQuestItemStack = new ItemStack(Material.BOOK, 1);
					ItemMeta availableQuestItemMeta = availableQuestItemStack.getItemMeta();
					availableQuestItemMeta.setDisplayName("§f" + quest.getName());
					if (quester.getCompletedQuests().contains(quest.getName()) == false
							&& quester.getCurrentQuests().containsKey(quest) == false) {
						availableQuestItemMeta.setLore(
								Arrays.asList("§e" + quest.getDescription(), "", "§7Click to start the quest."));

						availableQuestItemStack.setItemMeta(availableQuestItemMeta);

						if (!inventory.contains(availableQuestItemStack)) {
							inventory.addItem(availableQuestItemStack);
						}
					}
				}

				player.openInventory(inventory);
				return;
			}
		}

		for (String quest : quester.getCompletedQuests()) {
			ItemStack questItemStack = new ItemStack(Material.BOOK, 1);
			ItemMeta questItemMeta = questItemStack.getItemMeta();
			questItemMeta.setDisplayName("§f" + quest);
			questItemMeta.setLore(Arrays.asList("§aCompleted"));
			questItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			questItemMeta.addEnchant(Enchantment.LUCK, 3, false);
			questItemStack.setItemMeta(questItemMeta);

			if (clickedItem.equals(ItemBuilder.NO_QUEST_COMPLETED) || event.getCurrentItem().equals(questItemStack)) {
				event.setCancelled(true);
				return;
			}
		}

		for (Quest quest : quests.getQuests()) {
			ItemStack availableQuestItemStack = new ItemStack(Material.BOOK, 1);
			ItemMeta availableQuestItemMeta = availableQuestItemStack.getItemMeta();
			availableQuestItemMeta.setDisplayName("§f" + quest.getName());
			availableQuestItemMeta
					.setLore(Arrays.asList("§e" + quest.getDescription(), "", "§7Click to start the quest."));

			availableQuestItemStack.setItemMeta(availableQuestItemMeta);
			if (clickedItem.equals(availableQuestItemStack)) {
				player.closeInventory();
				quester.takeQuest(quest, false);
				return;
			}
		}

		if (clickedInventory.equals(IN_PROGRESS_QUESTS_INVENTORY)) {
			event.setCancelled(true);

			for (Quest quest : quester.getCurrentQuests().keySet()) {
				ItemStack currentQuestItemStack = new ItemStack(Material.ENCHANTED_BOOK, 1);
				ItemMeta currentQuestItemMeta = currentQuestItemStack.getItemMeta();
				currentQuestItemMeta.setDisplayName("§f" + quest.getName());
				currentQuestItemMeta.setLore(Arrays.asList("§7Click to quit the quest."));
				currentQuestItemStack.setItemMeta(currentQuestItemMeta);

				if (clickedItem.equals(currentQuestItemStack)) {
					IN_PROGRESS_QUESTS_INVENTORY.remove(currentQuestItemStack);
					player.closeInventory();
					player.performCommand("quests quit " + quest.getName());
					return;
				}
			}
		}

		if (clickedItem.equals(ItemBuilder.CLOSE)) {
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
			return;
		}
	}
}
