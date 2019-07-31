package com.craft0.mrivek.queststatsgui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class QuestGUICommand implements CommandExecutor {

	private Quests quests;

	public QuestGUICommand(QuestStatsGUI plugin) {
		this.quests = plugin.quests;
	}

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
		ItemStack questPoints = ItemBuilder.buildNewItem(Material.END_CRYSTAL, 1, "§dQuests Points",
				Arrays.asList("§d" + quester.getQuestPoints()), false);
		QuestStatsGUI.MENU.setItem(10, questPoints);
		QuestStatsGUI.MENU.setItem(12, ItemBuilder.AVAILABLE_QUESTS);

		if (quester.getCurrentQuests().isEmpty()) {
			QuestStatsGUI.MENU.setItem(14, ItemBuilder.NO_QUEST_IN_PROGRESS);
		} else {
			QuestStatsGUI.MENU.setItem(14, ItemBuilder.IN_PROGRESS_QUESTS);
		}

		if (quester.getCompletedQuests().isEmpty()) {
			QuestStatsGUI.MENU.setItem(16, ItemBuilder.NO_QUEST_COMPLETED);
		} else {
			QuestStatsGUI.MENU.setItem(16, ItemBuilder.COMPLETED_QUESTS);
		}

		QuestStatsGUI.MENU.setItem(26, ItemBuilder.CLOSE);
		player.openInventory(QuestStatsGUI.MENU);
		return true;
	}

}
