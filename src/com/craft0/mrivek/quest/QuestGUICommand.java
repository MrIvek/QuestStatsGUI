package com.craft0.mrivek.quest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class QuestGUICommand implements CommandExecutor {

	private Quests quests;

	public QuestGUICommand(QuestStatsGUI plugin) {
		this.quests = plugin.quests;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}

		Player player = (Player) sender;
		if (args.length != 0) {
			player.sendMessage(cmd.getUsage());
			return true;
		}

		Quester quester = quests.getQuester(player.getUniqueId());
		ItemStack questPoints = ItemBuilder.buildNewItem(Material.END_CRYSTAL, 1, "§fQuests §dPoints",
				Arrays.asList("§d - " + quester.getQuestPoints() + " - "), false);
		QuestStatsGUI.MENU.setItem(11, questPoints);
		QuestStatsGUI.MENU.setItem(13, QuestStatsGUI.IN_PROGRESS_QUESTS);

		if (quester.getCompletedQuests().isEmpty()) {
			QuestStatsGUI.MENU.setItem(15, QuestStatsGUI.NONE);
		} else {
			Map<Enchantment, Integer> enchantments = new HashMap<>();
			enchantments.put(Enchantment.LUCK, 1);

			ItemStack completedQuests = ItemBuilder.build(Material.BOOK, 1, "§fQuests", Arrays.asList("§aCompleted"),
					false, ItemFlag.HIDE_ENCHANTS, enchantments);
			QuestStatsGUI.MENU.setItem(15, completedQuests);
		}

		QuestStatsGUI.MENU.setItem(26, QuestStatsGUI.CLOSE);
		player.openInventory(QuestStatsGUI.MENU);
		return true;
	}

}
