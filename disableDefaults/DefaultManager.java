package me.Macpaper.GodSword.disableDefaults;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Macpaper.GodSword.Main;
import me.Macpaper.GodSword.ParticleLine;
import me.Macpaper.GodSword.utils.NumUtil;

public class DefaultManager implements Listener {
	
	public Main plugin;
	private DecimalFormat df = new DecimalFormat("0.0"); 
	public DefaultManager(Main plugin) {
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onConsumePotion(PlayerItemConsumeEvent event) {
			if (plugin.playerUtil.checkItemInHand(Material.POTION, "Ambrosia Flask", event)) {
				consumeAmbrosia(event);
			}
//			System.out.println(NumUtil.randInt(-10, 10));
	}
	
	public void consumeAmbrosia(PlayerItemConsumeEvent event) {
		event.setCancelled(true);
		Player p = (Player) event.getPlayer();
		double amt = 500.0D;
		if (p.getHealth() + amt >= p.getMaxHealth()) {
			double healed = p.getMaxHealth() - p.getHealth();
			p.setHealth(p.getMaxHealth());
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 5.0f, 0.5f);
			p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "" + "HEALED +"+healed);
		} else {
			p.setHealth(p.getHealth() + amt);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WANDERING_TRADER_DRINK_POTION, 5.0f, 0.5f);
			p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "" + "HEALED +"+amt);
		}
	}
	
	@EventHandler
	public void onTarget(EntityTargetLivingEntityEvent event) {
		Entity target = (Entity) event.getTarget();
		Entity targeter = (Entity) event.getEntity();
		if (target != null && targeter != null) {
//			System.out.println("A " + targeter.toString() + " IS TARGETTING: " + target.toString());	
		}
		
	}
	
	@EventHandler
	public void onMobShoot(ProjectileLaunchEvent event) {
		Entity e = (Entity) event.getEntity().getShooter();
		
			if (e != null && e instanceof Blaze) {
				World world = e.getWorld();
				Location l = e.getLocation();
				Location original = e.getLocation();
				new BukkitRunnable() {
					double delay = 0;
					int ticks = 0;
					boolean done = false;

					
					int dura = 0;
					public void run() {
						if (done)
							return;
						ticks++;
						delay = 1/20;
						if (ticks > delay * 10) {
							dura++;
							ticks = 0;
							for(int i = 0; i < 3; i++) {
								l.setY(original.getY() + 1.5);
								l.setX(original.getX() + e.getLocation().getDirection().getX() * 1.2);
								l.setZ(original.getZ() + e.getLocation().getDirection().getZ() * 1.2);

//								Entity a = (Entity)e.getWorld().spawnEntity(l, EntityType.ARROW);
								Entity f = (Entity)e.getWorld().spawnEntity(l, EntityType.SMALL_FIREBALL);
								SmallFireball fire = (SmallFireball)f;
								Vector v = e.getLocation().getDirection().multiply(2);
								v.rotateAroundY(-Math.PI / 12 + Math.PI * i / 12);
								fire.setVelocity(v);
								
//								Arrow arrow = (Arrow)a;
								world.playSound(e.getLocation(), Sound.ITEM_FIRECHARGE_USE, 2.0f, 1.0f);
//								arrow.setDamage(1);
//								arrow.setVelocity(e.getLocation().getDirection().multiply(2));
//								arrow.setColor(colors[(int)(Math.random() * colors.length)]);
							}
							
							if (dura >= 5) {
								cancel();
							}
						}
					}
				}.runTaskTimer(plugin, 0, 1);
			}
			if (e != null && e instanceof Skeleton) {
				
				World world = e.getWorld();
				Location l = e.getLocation();
				Location original = e.getLocation();
				new BukkitRunnable() {
					double delay = 0;
					int ticks = 0;
					boolean done = false;

					
					int dura = 0;
					public void run() {
						if (done)
							return;
						ticks++;
						delay = 1/20;
						if (ticks > delay * 10) {
							dura++;
							ticks = 0;
							for(int i = 0; i < 2; i++) {
								l.setY(original.getY() + 1.5);
								l.setX(original.getX() + e.getLocation().getDirection().getX() * 1.2);
								l.setZ(original.getZ() + e.getLocation().getDirection().getZ() * 1.2);
								
								Entity a = (Entity)e.getWorld().spawnEntity(l, EntityType.ARROW);
//								Entity f = (Entity)e.getWorld().spawnEntity(l, EntityType.SMALL_FIREBALL);
//								SmallFireball fire = (SmallFireball)f;
//								fire.setVelocity(e.getLocation().getDirection().multiply(2));
								
								Arrow arrow = (Arrow)a;
								world.playSound(e.getLocation(), Sound.ENTITY_ARROW_SHOOT, 2.0f, 1.0f);
								arrow.setDamage(1);
								arrow.setVelocity(e.getLocation().getDirection().multiply(2));
//								arrow.setColor(colors[(int)(Math.random() * colors.length)]);
							}
							
							if (dura >= 5) {
								cancel();
							}
						}
					}
				}.runTaskTimer(plugin, 0, 1);
			}
	}
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
//		World world = event.getEntity().getWorld();		
//		if (entity instanceof Zombie) {
//			if (NumUtil.randInt(0, 4) == 1) {
//				LivingEntity giant = (LivingEntity) world.spawnEntity(event.getLocation().add(0, 2, 0), EntityType.GIANT);
//				String fullName = ChatColor.RED + " HP: " + df.format(giant.getMaxHealth());
//
//				giant.setCustomName(fullName);
//				giant.setCustomNameVisible(true);
//			}
//		}

		LivingEntity entity = (LivingEntity) event.getEntity();
		if (entity instanceof Skeleton) {
				LivingEntity skeleton = (Skeleton) event.getEntity();
				skeleton.setMaxHealth(2500.0D);
				skeleton.setHealth(2500.0D);
				skeleton.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
				String fullName = ChatColor.RED + " HP: " + df.format(skeleton.getMaxHealth());
				skeleton.setCustomName(fullName);
				skeleton.setCustomNameVisible(true);
		}
		
		String fullName = ChatColor.RED + " HP: " + df.format(entity.getMaxHealth());

		entity.setCustomName(fullName);
		entity.setCustomNameVisible(true);
		
	}
	
	@EventHandler
	public void onMobKill(EntityDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			Player player = (Player) event.getEntity().getKiller();
			plugin.playerUtil.killed(player);
			if (plugin.lockOnTargets.get(player.getName()) != null && event.getEntity().equals(plugin.lockOnTargets.get(player.getName()))) {
				System.out.println("Lock on target died.");
				plugin.lockOnTargets.put(player.getName(), null);
			}
		}
	}
	
//	@EventHandler
//	publci void 
	
	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
//		World world = event.getWorld();
//		world.setStorm(false);
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
	public void onHitByProjectile(ProjectileHitEvent event) {
		Entity hit = event.getHitEntity();
		Projectile p = event.getEntity();
		if (hit != null && p != null) {
//			System.out.println(hit.toString() + " WAS HIT BY A PROJECTILE FROM " + p.getShooter());
			LivingEntity shooter = (LivingEntity) event.getEntity().getShooter();
			if (shooter != null && shooter instanceof Player) {
				Player player = (Player) shooter;
//				if (plugin.playerUtil.checkItemInHand(Material.BLAZE_POWDER, "Hellzone Grenade", player)) {
					try {
						Damageable d = (Damageable) hit;
						// DO ALL PROJECTILE DAMAGE HERE LOL
						if (p.hasMetadata("hellzone")) { d.damage(10); }
//						if (p.hasMetadata("hellzone")) { d.damage(1000); }
//						if (p.hasMetadata("hellzone")) { d.damage(1000); }
//						if (p.hasMetadata("hellzone")) { d.damage(1000); }
//						if (p.hasMetadata("hellzone")) { d.damage(1000); }
//						if (p.hasMetadata("hellzone")) { d.damage(1000); }
//						if (p.hasMetadata("hellzone")) { d.damage(1000); }
//						if (p.hasMetadata("hellzone")) { d.damage(1000); }
//						if (p.hasMetadata("hellzone")) { d.damage(1000); }
						if (hit.isDead()) {
							plugin.playerUtil.killed(player);
						}
					} catch (Exception ex) {

					}
//				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		// PLAYERS HIT
		if (e.getEntity() instanceof Player) {
			e.setCancelled(true);
			
			Player p = (Player) e.getEntity();
			
			World w = p.getWorld();
			Location l = p.getLocation();
			double huh = e.getDamage();
			double limit = huh;
			if (p.getHealth() - limit >= 0) {
				p.setHealth(p.getHealth() - limit);
				w.playSound(l, Sound.ENTITY_GENERIC_HURT, 0.5f, 0.5f);
			} else {
				p.setHealth(0);
				w.playSound(l, Sound.ENTITY_GENERIC_HURT, 1.5f, 1.5f);
				w.playSound(l, Sound.ENTITY_GHAST_HURT, 0.1f, 0.1f);
			}
			
		}
		
		
		// MOBS HIT
		if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player)) {
			e.setCancelled(true);
			
			LivingEntity e1 = (LivingEntity) e.getEntity();
			
			World w = e1.getWorld();
			Location l = e1.getLocation();
			
			// DEFAULT DAMAGE (CALLED LIMIT)
			double limit = e.getDamage();
			// OTHER WEIRD SOURCES OF DAMAGE. BEST WAY TO DO THIS IDK???
			
			
			if (e1.getHealth() - limit >= 0) {
				e1.setHealth(e1.getHealth() - limit);
				w.playSound(l, e1.getHurtSound(), 1.5f, 1.5f);
			} else {
				e1.setHealth(0);
				w.playSound(l, e1.getDeathSound(), 1.5f, 1.5f);
			}
			
			ParticleLine pl = new ParticleLine(e1.getWorld(), Main.getPlugin(Main.class), Particle.REDSTONE, e1.getLocation().add(0, 0, 0), e1.getLocation().add(0, 1.5, 0), 1);
			pl.spawnLine(1, 1, 1, 1, 1, new Particle.DustOptions(Color.fromBGR(0, 0, 255), 1));
			String fullName = ChatColor.RED + " HP: " + df.format(e1.getHealth());

			e1.setCustomName(fullName);
			e1.setCustomNameVisible(true);
			
		}
	}
	
}
