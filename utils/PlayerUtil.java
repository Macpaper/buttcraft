package me.Macpaper.GodSword.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Macpaper.GodSword.Main;

public class PlayerUtil {

	public Main plugin;
	
	public PlayerUtil(Main plugin) {
		this.plugin = plugin;
	}
	public void killed(Player player) {
		if (player.getHealth() + 20 <= 1000) {
			player.setHealth(player.getHealth() + 20.0D);
		} else {
			player.setHealth(1000);
		}
		plugin.data.addPoints(player.getUniqueId(), 1);
		player.sendMessage("Point added!");
	}
	// gets display name
	public boolean checkItemInHand(Object material, String displayName, PlayerInteractEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(material)) {
			if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
				if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(displayName)) {
					return true;
				}
			}
		}
		return false;
	}
	

	public String cancelAbilityMessage() {
		return ChatColor.RED + "" + ChatColor.BOLD + "Ability Cancelled! Re-Equip your weapon!";
	}
	
	public boolean givePlayerItem(CommandSender sender, String command, String itemName, ItemStack itemStack, String dropMessage) {
		if (command.equalsIgnoreCase(itemName)) {
			if (plugin.consoleTypesCommand(sender)) {
				return true;
			}
			
			Player player = (Player) sender;
			if (player.getInventory().firstEmpty() == -1) {
				// inventory is full
				Location location = player.getLocation();
				World world = player.getWorld();
				world.dropItemNaturally(location, itemStack);
				player.sendMessage(dropMessage);
				return true;
			}
			player.getInventory().addItem(itemStack);
			player.sendMessage(dropMessage);
			return true;
		}
		return false;
	}
}
