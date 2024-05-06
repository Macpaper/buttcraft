package me.Macpaper.GodSword.disableDefaults;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import me.Macpaper.GodSword.Main;

public class DefaultManager implements Listener {
	
	public Main plugin;
	
	public DefaultManager(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMobKill(EntityDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			Player player = (Player) event.getEntity().getKiller();
			plugin.playerUtil.killed(player);
		}
	}
	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
		World world = event.getWorld();
		world.setStorm(false);
	}
	
	@EventHandler
	public void onRegen(EntityRegainHealthEvent e) {
			if (e.getEntity() instanceof Player) {
				e.setCancelled(true);
				Player p = (Player) e.getEntity();
				
			}
	}
	
	@EventHandler
	public void onHunger(EntityExhaustionEvent e) {
		if (e.getEntity() instanceof Player) {
			e.setCancelled(true);
			Player p = (Player) e.getEntity();
			p.setExhaustion(0.0f);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			e.setCancelled(true);
			Player p = (Player) e.getEntity();
			World w = p.getWorld();
			Location l = p.getLocation();
			if (p.getHealth() - 20.0D >= 0) {
				p.setHealth(p.getHealth() - 20.0D);
				w.playSound(l, Sound.ENTITY_GENERIC_HURT, 0.5f, 0.5f);
			} else {
				p.setHealth(0);
				w.playSound(l, Sound.ENTITY_GENERIC_HURT, 1.5f, 1.5f);
			}
			
		}
	}

	@EventHandler()
	public void onSpawn(PlayerInteractEvent event) {
//		hp.setBaseValue(500.0D);
		if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.EGG)) {
			Player player = (Player) event.getPlayer();
			ItemStack boots = player.getInventory().getBoots();
			if (boots.getItemMeta().hasLore() && boots.getItemMeta().getDisplayName().contains("Boots of Cool")) {
				World world = player.getWorld();
//				world.spawnEntity(player.getLocation(), EntityType.BLAZE);
				AttributeInstance hp = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
				AttributeInstance armor = player.getAttribute(Attribute.GENERIC_ARMOR);
				hp.setBaseValue(1000.0D);
				
				player.setHealth(1000.0D);
			}

			if (event.getAction() == Action.LEFT_CLICK_AIR) {

			}
		}

	}
	
}
