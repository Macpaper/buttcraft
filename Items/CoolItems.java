package me.Macpaper.GodSword.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class CoolItems {
	
	public ItemStack getSword() {
		ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
		setItemMetaLore(sword, ChatColor.AQUA + "" + ChatColor.BOLD + "Yamato Blade", ChatColor.GOLD + "" + ChatColor.ITALIC + "A sword for those Motivated.");
		ItemMeta meta = sword.getItemMeta();

		meta.addEnchant(Enchantment.DAMAGE_ALL, 20, true);
		meta.addEnchant(Enchantment.FIRE_ASPECT, 20, true);
		meta.addEnchant(Enchantment.KNOCKBACK, 20, true);

		
		sword.setItemMeta(meta);
		return sword;
	}
	public ItemStack getHealthBoots() {
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

		setItemMetaLore(boots, ChatColor.GOLD + "Boots of Cool", "You are literally God now");
		return boots;
	}
	public ItemStack getTritip() {
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		setItemMetaLore(sword, "Tritip-blade", "Bleeds enemies");
		return sword;
	}
	public ItemStack getAmbrosia() {
		ItemStack potion = new ItemStack(Material.POTION, 1);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		// type, extend, upgraded
//		meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, true, true));
		setItemMetaLore(potion, ChatColor.GOLD + "Ambrosia Flask", "Heals the soul", meta);
		return potion;
	}
	public ItemStack getPyromancyStaff() {
		ItemStack rod = new ItemStack(Material.BLAZE_ROD);
		setItemMetaLore(rod, ChatColor.YELLOW + "" + ChatColor.BOLD + "Pyromancy Staff", "Pyro Flame shot go boom");
		return rod;
	}
	
	public ItemStack getLightningBow() {
		ItemStack bow = new ItemStack(Material.BOW);
		setItemMetaLore(bow, ChatColor.GOLD + "Lightning Bow", ChatColor.DARK_BLUE + "Charged shot brings forth lightning");
//		bow.getItemMeta().addEnchant(Enchantment.ARROW_INFINITE, 20, true);
		return bow;
	}
	
	public ItemStack getMonsoonBow() {
		ItemStack bow = new ItemStack(Material.BOW);
		setItemMetaLore(bow, ChatColor.GOLD + "Monsoon", ChatColor.DARK_RED + "And it will come like a flood of pain.");
//		bow.getItemMeta().addEnchant(Enchantment.ARROW_INFINITE, 20, true);
		return bow;
	} 
	
	public ItemStack getBlazeFireOrb() {
		ItemStack r = new ItemStack(Material.BLAZE_POWDER);
		setItemMetaLore(r, ChatColor.GOLD + "Fire Orb", ChatColor.RED + "The Orb " + ChatColor.BOLD + "VIBRATES " + "with power.");
		return r;
	}
	
	public ItemStack getHellzoneGrenade() {
		ItemStack r = new ItemStack(Material.BLAZE_POWDER);
		setItemMetaLore(r, ChatColor.GOLD + "Hellzone Grenade", ChatColor.RED + "Shit, its even got a cool name. ");
		return r;
	}
	
	public ItemStack getRed() {
		ItemStack r = new ItemStack(Material.RED_DYE);
		setItemMetaLore(r, ChatColor.RED + "Red", ChatColor.RED + "Repels enemies and objects");
		return r;
	}
	
	public ItemStack getBlue() {
		ItemStack r = new ItemStack(Material.BLUE_DYE);
		setItemMetaLore(r, ChatColor.BLUE + "Blue", ChatColor.BLUE + "Attracts enemies and objects");
		return r;
	}
	
	public void setItemMetaLore(ItemStack item, String displayName, String loreName) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		// uses this display name for thing.
		meta.setDisplayName(displayName);
		meta.setUnbreakable(true);
		
		lore.add("");
		lore.add(ChatColor.ITALIC + loreName);
		meta.setLore(lore);
		

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		
		item.setItemMeta(meta);
	}
	public void setItemMetaLore(ItemStack item, String displayName, String loreName, ItemMeta meta) {
		List<String> lore = new ArrayList<String>();
		
		// uses this display name for thing.
		meta.setDisplayName(displayName);
		meta.setUnbreakable(true);
		
		lore.add("");
		lore.add(ChatColor.ITALIC + loreName);
		meta.setLore(lore);
		

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		
		item.setItemMeta(meta);
	}
}
