package me.Macpaper.GodSword.abilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import me.Macpaper.GodSword.Main;
import me.Macpaper.GodSword.ParticleLine;
import me.Macpaper.GodSword.utils.NumUtil;

public class SwordAbilities implements Listener {
	
	public Main plugin;
	public SwordAbilities(Main plugin) {
		this.plugin = plugin;
	}
	public AbilityTimerHandler timerHandler = new AbilityTimerHandler();

	Color[] colors = { Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GREEN,
			Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE,
			Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW };
	
	@EventHandler()
	public void onClick(PlayerInteractEvent event) {
		Player player = (Player) event.getPlayer();
		player.setMaxHealth(500);
		player.setHealth(500);
		// LOCK ONTO ENEMY
		boolean rightClicking = false;
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			rightClicking = true;
		}
		
		if (event.getAction() == Action.RIGHT_CLICK_AIR) {
			lockOntoEnemy(event, player);
		}
		
		// before ANY abilities can happen, first just check if player already has a cool-down. (with this in mind, make sure to remove players from the cdr list for every single ability.
		if (plugin.abilityCooldownList.contains(player.getName())) {
			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "* ABILITIES ON COOLDOWN *");
			return;
		}

		if (plugin.playerUtil.checkItemInHand(Material.BLAZE_ROD, "Pyromancy Staff", event)) {
			if (rightClicking) {
				if (timerHandler.isReady("Pyro Staff Heal", player, 3000)) {
					pyroStaffHeal(event);
				} 
			} 
		}
		
		if (plugin.playerUtil.checkItemInHand(Material.NETHERITE_SWORD, "Yamato Blade", event)) {
			if (rightClicking) {
				if (timerHandler.isReady("JUDGMENT CUT END", player, 10000)) {
					judgmentCutEnd(event);
				} 
			} else {
				if (timerHandler.isReady("JUDGMENT CUT", player, 500)) {
					judgmentCut(event);
				} 
			}
		}
		
		if (plugin.playerUtil.checkItemInHand(Material.BLAZE_POWDER, "Fire Orb", event)) {
			if (rightClicking) {
				if (timerHandler.isReady("Fire Orb Shoot", player, 250)) {
					fireOrbRightClickAbility(event);
				} 
			} else {
				if (timerHandler.isReady("FIRE ORB ULTIMATE", player, 5000)) {
					fireOrbLeftClickAbility(event);
				} 
			}
		}
		
		if (plugin.playerUtil.checkItemInHand(Material.IRON_SWORD, "Tritip-blade", event)) {
			if (rightClicking && plugin.isLockedOn(player)) {
				if (timerHandler.isReady("TRITIP-BLADE TELEPORT", player, 1000)) {
					tritipBladeTeleport(event);
				} 
			} else if (rightClicking && !plugin.isLockedOn(player)) {
				if (timerHandler.isReady("TRITIP-BLADE TELEPORT", player, 1000)) {
					tritipBladeTeleUnlocked(event);
				} 
			} else {
				if (plugin.isLockedOn(player)) {
					LivingEntity enemy = plugin.lockOnTargets.get(player.getName());
				}
			}
		}
		
		if (plugin.playerUtil.checkItemInHand(Material.BLAZE_POWDER, "Hellzone Grenade", event)) {
			if (rightClicking && plugin.isLockedOn(player)) {
				if (timerHandler.isReady("HELLZONE GRENADE", player, 10000)) {
					hellzoneGrenade(event, player);
				}
			}
			if (!rightClicking) {
				lockOntoEnemy(event, player);
			}
		}
		if (plugin.playerUtil.checkItemInHand(Material.RED_DYE, "Red", event)) {
			if (timerHandler.isReady("MAXIMUM OUTPUT RED", player, 2000)) {
				red(event, player);
			}
		}
		if (plugin.playerUtil.checkItemInHand(Material.BLUE_DYE, "Blue", event)) {
			if (rightClicking) {
				if (timerHandler.isReady("MAXIMUM OUTPUT BLUE", player, 10000)) {
					maximumOutputBlue(event);
				}
			} else {
				if (timerHandler.isReady("Blue", player, 1000)) {
					blue(event);
				}
			}

		}
		
		// for cooldowns i think
//		if (list.contains((event.getPlayer().getName()))) {
//			list.remove(event.getPlayer().getName());
//		}
	}
	
	public void red(PlayerInteractEvent event, Player player) {
		double G = 0.674;
		Location originalLoc = player.getLocation().add(player.getLocation().getDirection().multiply(5)).add(0, 2, 0);
		int r = 3;

		////////////////////////////////////
		int duration = 100;
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 5.0f, 0.5f);
//		pl.spawnLine(1, 1, 1, 1, 1, new Particle.DustOptions(Color.fromBGR(0, 0, 255), 1));
//		world.spawnParticle(particle, l, 1, offx, offy, offz, 0, dustOptions);
//		System.out.println(blocks.size());
		new BukkitRunnable() {
			int ticks = 0;
			
			@Override
			public void run() {
				Location rloc = originalLoc.add(originalLoc.getDirection().normalize());
				player.spawnParticle(Particle.REDSTONE, rloc, 10, 1, 1, 1, new Particle.DustOptions(Color.fromBGR(0, 0, 255), 10));
				ticks += 1;
				if (ticks % 5 == 0) {
					player.spawnParticle(Particle.EXPLOSION_LARGE, rloc, 3, 1, 1, 1);
				}
				if (ticks % 3 == 0) {
					Collection<Entity> entities = player.getWorld().getNearbyEntities(rloc, r, r, r);
					
					ArrayList<FallingBlock> blocks = new ArrayList<FallingBlock>();
					for (int i = -r; i <= r; i++) {
						for (int j = -r; j <= r; j++) {
							for (int k = -r; k <= r; k++) {
								Location airLoc = new Location(player.getWorld(), rloc.getX() + i, rloc.getY() + j, rloc.getZ() + k);
								Block b = airLoc.getBlock();
								double distX = rloc.getX() - airLoc.getX();
								double distY = rloc.getY() - airLoc.getY();
								double distZ = rloc.getZ() - airLoc.getZ();
								
								Vector pointAway = new Vector(distX, distY, distZ).multiply(0.5);
								double dist = airLoc.distance(rloc);
								
								double force = -1;
								if (dist > 0.3) {
									force = 1/dist;
								}
								FallingBlock fb = player.getWorld().spawnFallingBlock(airLoc, b.getType().createBlockData());
								fb.setVelocity(new Vector(force * -distX, force * -distY, force * -distZ));
								b.setType(Material.AIR);
								blocks.add(fb);
							}
						}
					}
					
					entities.forEach(e -> {
						if (e instanceof LivingEntity) {
							double distX = rloc.getX() - e.getLocation().getX();
							double distY = rloc.getY() - e.getLocation().getY();
							double distZ = rloc.getZ() - e.getLocation().getZ();
//							Vector pointAway = new Vector(distX, distY, distZ).multiply(0.5);
							double dist = e.getLocation().distance(rloc);
							double force = 10;
							if (dist != 0) {
								force = 10/dist;
							}
							Vector pointTowards = new Vector(force * -distX, force * -distY, force * -distZ);
							e.setVelocity(pointTowards);
							if (e instanceof Damageable) {
								try {
									Damageable d = (Damageable) e;
									d.damage(2);
									if (e.isDead()) {
										plugin.playerUtil.killed(player);
									}
									return;
								} catch (Exception ex) {

								}
							}
						}
					});
					
					blocks.forEach(b -> {
						double distX = rloc.getX() - b.getLocation().getX();
						double distY = rloc.getY() - b.getLocation().getY();
						double distZ = rloc.getZ() - b.getLocation().getZ();
//						Vector pointAway = new Vector(distX, distY, distZ).multiply(0.5);
						double dist = b.getLocation().distance(rloc);
						double force = -1;
						if (dist > 0.3) {
							force = 1/dist;
						}
						Vector pointTowards = new Vector(force * -distX, force * -distY, force * -distZ);
						b.setVelocity(pointTowards);
//						System.out.println("bitch im tryin");
					});
				}


				if (ticks > duration) {
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
		//////////////////////////////////////
//		Location l = player.getLocation().add(0, 3, 0);
//		FallingBlock b = player.getWorld().spawnFallingBlock(l, Material.COBBLESTONE.createBlockData());
//		b.setVelocity(new Vector(3, 0, 0));
	}
	
	public void blue(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Location rloc = player.getLocation().add(player.getLocation().getDirection().multiply(4)).add(0, 2, 0);
		player.spawnParticle(Particle.REDSTONE, rloc, 10, 1, 1, 1, new Particle.DustOptions(Color.fromBGR(245, 120, 42), 10));	
		
		int r = 3;

		////////////////////////////////////
		int duration = 20;
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 5.0f, 0.5f);
//		System.out.println(blocks.size());
		new BukkitRunnable() {
			int ticks = 0;
			
			@Override
			public void run() {
//				Location rloc = player.getLocation().add(player.getLocation().getDirection().multiply(10)).add(0, 2, 0);
				ticks += 1;
				if (ticks % 3 == 0) {
					player.spawnParticle(Particle.REDSTONE, rloc, 10, 1, 1, 1, new Particle.DustOptions(Color.fromBGR(245, 120, 42), 10));	
				}
					Collection<Entity> entities = player.getWorld().getNearbyEntities(rloc, r, r, r);

					ArrayList<FallingBlock> blocks = new ArrayList<FallingBlock>();
					for (int i = -r; i <= r; i++) {
						for (int j = -r; j <= r; j++) {
							for (int k = -r; k <= r; k++) {
								Location airLoc = new Location(player.getWorld(), rloc.getX() + i, rloc.getY() + j, rloc.getZ() + k);
								Block b = airLoc.getBlock();
								double distX = rloc.getX() - airLoc.getX();
								double distY = rloc.getY() - airLoc.getY();
								double distZ = rloc.getZ() - airLoc.getZ();
								
								Vector pointAway = new Vector(distX, distY, distZ).multiply(0.5);
								double dist = airLoc.distance(rloc);
								
								double force = -1;
								if (dist > 0.3) {
									force = -1/dist;
								}
								FallingBlock fb = player.getWorld().spawnFallingBlock(airLoc, b.getType().createBlockData());
								fb.setVelocity(new Vector(force * -distX, force * -distY, force * -distZ));
								b.setType(Material.AIR);
								blocks.add(fb);
							}
						}
					}
					
					entities.forEach(e -> {
						if (e != player) {
							double distX = rloc.getX() - e.getLocation().getX();
							double distY = rloc.getY() - e.getLocation().getY();
							double distZ = rloc.getZ() - e.getLocation().getZ();
//							Vector pointAway = new Vector(distX, distY, distZ).multiply(0.5);
							double dist = e.getLocation().distance(rloc);
							double force = -1;
							if (dist != 0) {
								force = -1/dist;
							}
							Vector pointTowards = new Vector(force * -distX, force * -distY, force * -distZ);
							e.setVelocity(pointTowards);
							if (e instanceof Damageable) {
								try {
									Damageable d = (Damageable) e;
									d.damage(2);
									if (e.isDead()) {
										plugin.playerUtil.killed(player);
									}
									return;
								} catch (Exception ex) {

								}
							}	
						}

					});
					
					blocks.forEach(b -> {
						double distX = rloc.getX() - b.getLocation().getX();
						double distY = rloc.getY() - b.getLocation().getY();
						double distZ = rloc.getZ() - b.getLocation().getZ();
//						Vector pointAway = new Vector(distX, distY, distZ).multiply(0.5);
						double dist = b.getLocation().distance(rloc);
						double force = -1;
						if (dist > 0.3) {
							force = -1/dist;
						}
						Vector pointTowards = new Vector(force * -distX, force * -distY, force * -distZ);
						b.setVelocity(pointTowards);
//						System.out.println("bitch im tryin");
					});
				


				if (ticks > duration) {
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
		
	}
	
	public void maximumOutputBlue(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		double G = 0.674;
		Location rloc = player.getLocation().add(player.getLocation().getDirection().multiply(5)).add(0, 2, 0);
		player.spawnParticle(Particle.REDSTONE, rloc, 10, 1, 1, 1, new Particle.DustOptions(Color.fromBGR(245, 120, 42), 10));	
		int r = 3;

		////////////////////////////////////
		int duration = 100;
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 5.0f, 0.5f);
//		System.out.println(blocks.size());
		new BukkitRunnable() {
			int ticks = 0;
			
			@Override
			public void run() {
				Location rloc = player.getLocation().add(player.getLocation().getDirection().multiply(10)).add(0, 2, 0);
				ticks += 1;
				if (ticks % 5 == 0) {
					player.spawnParticle(Particle.EXPLOSION_LARGE, rloc, 3, 1, 1, 1);
				}
				if (ticks % 3 == 0) {
					player.spawnParticle(Particle.REDSTONE, rloc, 10, 1, 1, 1, new Particle.DustOptions(Color.fromBGR(245, 120, 42), 10));
					Collection<Entity> entities = player.getWorld().getNearbyEntities(rloc, r, r, r);

					ArrayList<FallingBlock> blocks = new ArrayList<FallingBlock>();
					for (int i = -r; i <= r; i++) {
						for (int j = -r; j <= r; j++) {
							for (int k = -r; k <= r; k++) {
								Location airLoc = new Location(player.getWorld(), rloc.getX() + i, rloc.getY() + j, rloc.getZ() + k);
								Block b = airLoc.getBlock();
								double distX = rloc.getX() - airLoc.getX();
								double distY = rloc.getY() - airLoc.getY();
								double distZ = rloc.getZ() - airLoc.getZ();
								
								Vector pointAway = new Vector(distX, distY, distZ).multiply(0.5);
								double dist = airLoc.distance(rloc);
								
								double force = -1;
								if (dist > 0.3) {
									force = -1/dist;
								}
								FallingBlock fb = player.getWorld().spawnFallingBlock(airLoc, b.getType().createBlockData());
								fb.setVelocity(new Vector(force * -distX, force * -distY, force * -distZ));
								b.setType(Material.AIR);
								blocks.add(fb);
							}
						}
					}
					
					entities.forEach(e -> {
						
						double distX = rloc.getX() - e.getLocation().getX();
						double distY = rloc.getY() - e.getLocation().getY();
						double distZ = rloc.getZ() - e.getLocation().getZ();
//						Vector pointAway = new Vector(distX, distY, distZ).multiply(0.5);
						double dist = e.getLocation().distance(rloc);
						double force = -1;
						if (dist != 0) {
							force = -1/dist;
						}
						Vector pointTowards = new Vector(force * -distX, force * -distY, force * -distZ);
						e.setVelocity(pointTowards);
						if (e instanceof Damageable) {
							try {
								Damageable d = (Damageable) e;
								d.damage(5);
								if (e.isDead()) {
									plugin.playerUtil.killed(player);
								}
								return;
							} catch (Exception ex) {

							}
						}
					});
					
					blocks.forEach(b -> {
						double distX = rloc.getX() - b.getLocation().getX();
						double distY = rloc.getY() - b.getLocation().getY();
						double distZ = rloc.getZ() - b.getLocation().getZ();
//						Vector pointAway = new Vector(distX, distY, distZ).multiply(0.5);
						double dist = b.getLocation().distance(rloc);
						double force = -1;
						if (dist > 0.3) {
							force = -1/dist;
						}
						Vector pointTowards = new Vector(force * -distX, force * -distY, force * -distZ);
						b.setVelocity(pointTowards);
//						System.out.println("bitch im tryin");
					});
				}


				if (ticks > duration) {
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
		//////////////////////////////////////
//		Location l = player.getLocation().add(0, 3, 0);
//		FallingBlock b = player.getWorld().spawnFallingBlock(l, Material.COBBLESTONE.createBlockData());
//		b.setVelocity(new Vector(3, 0, 0));
	}
	
	public void lockOntoEnemy(PlayerInteractEvent event, Player player) {
		BlockIterator bi = new BlockIterator(player, 20);
		while(bi.hasNext()) {
			Block next = bi.next().getLocation().getBlock();
			Collection<Entity> entities = next.getWorld().getNearbyEntities(next.getLocation(), 1, 1, 1);
			for (Entity e : entities) {
				if (e != player && e instanceof Damageable) {
					try {
						LivingEntity enemy = (LivingEntity) e;
						plugin.lockOnTargets.put(player.getName(), enemy);
						enemy.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 0));
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10.0f, 0.1f);
						return;
					} catch (Exception ex) {

					}
				}

			}

		}
		// unlock if player is too far away (better algorithm would be to unlock if player looks away maybe?)
		if (plugin.isLockedOn(player)) {
			LivingEntity target = plugin.lockOnTargets.get(player.getName());
			double dist = target.getLocation().distance(player.getLocation());
			if (dist >= 20) {
				plugin.lockOnTargets.put(player.getName(), null);
			}
		}
	}
	
	@EventHandler
	public void onEntityBowShoot(EntityShootBowEvent event) {
		if (event.getBow().getItemMeta().hasLore() && event.getBow().getItemMeta().getDisplayName().contains("Lightning Bow")) {
			MetadataValue mdv = new FixedMetadataValue(plugin, "bowlshit");
			event.getProjectile().setMetadata("lightningarrow", mdv);
		}
		if (event.getBow().getItemMeta().hasLore() && event.getBow().getItemMeta().getDisplayName().contains("Monsoon")) {
//			event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.LIGHTNING);
			MetadataValue mdv = new FixedMetadataValue(plugin, "bowlshit"); // not sure what this was needed for
			event.getProjectile().setMetadata("monsoonarrow", mdv);
		}
	}
	
	@EventHandler
	public void onProjectileLand(ProjectileHitEvent event) {
		if (event.getEntity().hasMetadata("lightningarrow")) {
			event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.LIGHTNING);
			lightningBowSpecial(event);
		}
		if (event.getEntity().hasMetadata("monsoonarrow")) {
			Player p = (Player) event.getEntity().getShooter();
			p.sendMessage("OW");
			rain(p);
		}
	}

	public void tritipBladeTeleport(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		LivingEntity enemy = plugin.lockOnTargets.get(player.getName());
		Vector towardsEnemy = new Vector(enemy.getLocation().getX() - player.getLocation().getX() , enemy.getLocation().getY() - player.getLocation().getY(), enemy.getLocation().getZ() - player.getLocation().getZ()).normalize();
		player.setVelocity(new Vector(towardsEnemy.getX() * 3, towardsEnemy.getY() * 3, towardsEnemy.getZ() * 3));
		////////////////////////////////////
		int duration = 10;
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 5.0f, 0.5f);
		new BukkitRunnable() {
			int ticks = 0;
			
			@Override
			public void run() {
				ticks += 1;
				if (ticks % 1 == 0) {
					player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation().add(0, 1.5, 0), 1);
					Collection<Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), 2, 2, 2);
					for (Entity e : entities) {
						if (e instanceof Damageable && !(e instanceof Player)) {
							Damageable d = (Damageable) e;
							d.damage(5);
//							player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 5.0f, 0.5f);
//							player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation().add(0, 1.5, 0), 1);
							if (e.isDead()) {
								plugin.playerUtil.killed(player);
							}
						}
					}
				}

				if (ticks > duration) {
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
		//////////////////////////////////////
	}
	
	public void hellzoneGrenade(PlayerInteractEvent event, Player player) {
		World world = player.getWorld();
			LivingEntity enemy = plugin.lockOnTargets.get(player.getName());

			////////////////////////////////////
			int duration = 100;
			ArrayList<Fireball> balls = new ArrayList<Fireball>();
			new BukkitRunnable() {
				int ticks = 0;
				@Override
				public void run() {
					if (ticks % 3 == 0) {
						Location l = player.getLocation();
						l.setY(player.getLocation().getY() + 1.5);
						l.setX(player.getLocation().getX() + player.getLocation().getDirection().getX() * 1.2);
						l.setZ(player.getLocation().getZ() + player.getLocation().getDirection().getZ() * 1.2);
						Fireball fireball = (Fireball) world.spawnEntity(l, EntityType.SMALL_FIREBALL);
						fireball.setDirection(player.getLocation().getDirection());
						fireball.setShooter(player);
						fireball.setMetadata("hellzone", new FixedMetadataValue(plugin, "lol"));
						balls.add(fireball);

						player.getWorld().playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 5.0f, 0.5f);
						stopFireballAfterDelay(fireball, player, ticks);
					}

					ticks += 1;
//					if (ticks % 1 == 0) {
//						fireball.setVelocity(new Vector(0, 0, 0));
//					}

					if (ticks > duration) {
						launchFireballs(balls, player);
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, 1);
			//////////////////////////////////////
	}
	
	public void launchFireballs(ArrayList<Fireball> balls, Player player) {
		////////////////////////////////////
		int duration = 50;
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 5.0f, 0.5f);
		new BukkitRunnable() {
			int ticks = 0;
			
			@Override
			public void run() {
				ticks += 1;
//				if (ticks % 1 == 0) {
//					fireball.setVelocity(new Vector(0, 0, 0));
//				}

				if (ticks > duration) {
					for (Fireball f : balls) {
						if (plugin.isLockedOn(player)) {
							LivingEntity enemy = plugin.lockOnTargets.get(player.getName());
							Vector towardsEnemy = new Vector(enemy.getLocation().getX() - f.getLocation().getX() , enemy.getLocation().getY() - f.getLocation().getY(), enemy.getLocation().getZ() - f.getLocation().getZ()).normalize();
							f.setDirection(towardsEnemy);
							f.setVelocity(towardsEnemy.multiply(3));
						}
					}
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
		//////////////////////////////////////

	}
	
	public void stopFireballAfterDelay(Fireball f, Player player, int oldTicks) {
		int duration = NumUtil.randInt(20, 30);
		new BukkitRunnable() {
			int ticks = 0;
			
			@Override
			public void run() {
				ticks += 1;

				if (ticks > duration) {
					stopFireball(f, player, oldTicks);
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void stopFireball(Fireball f, Player player, int oldTicks) {
		int duration = 130 - oldTicks;
		new BukkitRunnable() {
			int ticks = 0;
			
			@Override
			public void run() {
				ticks += 1;
				f.setVelocity(new Vector(0, 0, 0));
				if (ticks > duration) {
					if (plugin.isLockedOn(player)) {
						LivingEntity enemy = plugin.lockOnTargets.get(player.getName());
						Vector towardsEnemy = new Vector(enemy.getLocation().getX() - f.getLocation().getX() , enemy.getLocation().getY() - f.getLocation().getY(), enemy.getLocation().getZ() - f.getLocation().getZ()).normalize();
						f.setDirection(towardsEnemy);
						f.setVelocity(towardsEnemy.multiply(3));
					}
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void tritipBladeTeleUnlocked(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Vector facing = new Vector(player.getLocation().getDirection().getX(), player.getLocation().getDirection().getY(), player.getLocation().getDirection().getZ()).normalize();
		player.setVelocity(new Vector(facing.getX() * 2, facing.getY() * 2, facing.getZ() * 2));
		////////////////////////////////////
		int duration = 10;
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 5.0f, 0.5f);
		new BukkitRunnable() {
			int ticks = 0;
			
			@Override
			public void run() {
				ticks += 1;
				if (ticks % 1 == 0) {
					player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation().add(0, 1.5, 0), 1);
					Collection<Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), 2, 2, 2);
					for (Entity e : entities) {
						if (e instanceof Damageable && !(e instanceof Player)) {
							Damageable d = (Damageable) e;
							d.damage(5);
//							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 5.0f, 0.5f);
//							player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation().add(0, 1.5, 0), 1);
							if (e.isDead()) {
								plugin.playerUtil.killed(player);
							}
						}
					}
				}

				if (ticks > duration) {
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
		//////////////////////////////////////
	}
	
	
	// spawns a "healing circle" (ala Dragons Dogma). heal is based off duration. 
	public void healingCircle(Player player, Location location, int duration, double healLevel) {
		DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 255, 0), 1.0F);
		new BukkitRunnable() {
			int ticks = 0;
			
			Location l = new Location(player.getWorld(), location.getX(), location.getY() + 2.5, location.getZ());
			Collection<Entity> entities = player.getWorld().getNearbyEntities(l, 2, 2, 2);
			
			@Override
			public void run() {
				ticks += 1;
				if (ticks % 5 == 0) {
					player.spawnParticle(Particle.REDSTONE, l, 25, 1, 1, 1, 0.1, dustOptions);
					player.getWorld().playSound(l, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 2.0F, (float) (Math.random() * 4 - 1));
					Collection<Entity> entities = player.getWorld().getNearbyEntities(l, 2, 2, 2);
					for (Entity e : entities) {
						if (e instanceof Player) {
							Player p = (Player) e;
							if (p.getHealth() < Main.MAX_PLAYER_HEALTH) {
							try {
									if (Main.MAX_PLAYER_HEALTH - p.getHealth() < 5) {
										p.setHealth(Main.MAX_PLAYER_HEALTH);
									} else {
										p.setHealth(p.getHealth() + 5);
									}
									p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "HP: +5");

								return;
							} catch (Exception ex) {
								player.sendMessage(ex.toString());
							}
							}
						}

					}

				}

				if (ticks > duration) {
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void healThrow(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 255, 0), 1.0F);
		player.spawnParticle(Particle.REDSTONE, player.getLocation().add(0,2.5,0), 50, dustOptions);
		Atom a = new Atom(plugin, player.getLocation(), Particle.REDSTONE);

		double vecY = player.getLocation().getDirection().getY()+0.5;
		double vecX = player.getLocation().getDirection().getX()*2 * (vecY + 0.5);
		double vecZ = player.getLocation().getDirection().getZ()*2 * (vecY + 0.5);
		a.setVelocityY(vecY);
		a.setVelocityX(vecX);
		a.setVelocityZ(vecZ);
		a.setAccelerationY(-0.08);
		player.sendMessage(ChatColor.GREEN + "Heal circle thrown!");
//		player.sendMessage("vecX: " + vecX + " vecY: " + vecY + " vecZ: " + vecZ);
//		player.sendMessage("plaX: " + a.getVelocityX() + " plaY: " + a.getVelocityY() + " plaZ: " + a.getVelocityZ());
		new BukkitRunnable() {
			int timeout = 30;
			int ticks = 0;
			@Override
			public void run() {
				double oldX = a.getLocation().getX();
				double oldY = a.getLocation().getY();
				double oldZ = a.getLocation().getZ();
				
				a.setVelocityX(a.getVelocityX() + a.getAccelerationX());
				a.setVelocityY(a.getVelocityY() + a.getAccelerationY());
				a.setVelocityZ(a.getVelocityZ() + a.getAccelerationZ());
				
				double newX = a.getLocation().getX() + a.getVelocityX();
				double newY = a.getLocation().getY() + a.getVelocityY();
				double newZ = a.getLocation().getZ() + a.getVelocityZ();
				a.setLocation(new Location(player.getWorld(), newX, newY, newZ));
				
				Block next = a.getLocation().getBlock();
				if (!next.isPassable()) {
					healingCircle(player, a.getLocation(), 120, 1);
					cancel();
				}
//				new BukkitRunnable() {
//					int ticks = 0;
//					@Override
//					public void run() {
//						
//						if (ticks >= 5) {
//							cancel();
//						}	
//					}
//				}.runTaskTimer(plugin, 0, 1);
				
//				player.sendMessage("x: " + newX + " y: " + newY + " z: " + newZ);


				
				player.getWorld().spawnParticle(a.getParticle(), a.getLocation(), 3, dustOptions);
				
				ticks += 1;
				if (ticks > timeout) {
					cancel();
				}
			}
			
		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void rain(Player p) {
		World world = p.getWorld();
		world.playSound(p.getLocation(), Sound.ENTITY_ALLAY_HURT, 2.0f, 1.0f);
		Location l = p.getLocation();
		Location original = p.getLocation();
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
					for(int i = 0; i < 10; i++) {
						l.setY(original.getY() + Math.random()*10 + 3);
						l.setX(original.getX() + Math.random()*40 - 20);
						l.setZ(original.getZ() + Math.random()*40 - 20);

						Entity a = (Entity)p.getWorld().spawnEntity(l, EntityType.ARROW);
						Arrow arrow = (Arrow)a;
						arrow.setDamage(10);
						arrow.setVelocity(p.getLocation().getDirection().multiply(2));
//						arrow.setColor(colors[(int)(Math.random() * colors.length)]);
					}
					
					if (dura >= 20) {
						cancel();
					}
				}
			}
		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void lightningBowSpecial(ProjectileHitEvent event) {
		Player player = (Player) event.getEntity().getShooter();
		
		event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.CAT);
		new BukkitRunnable() {

			Collection<Entity> entities = event.getEntity().getWorld().getNearbyEntities(event.getEntity().getLocation(), 20, 10, 20);
			int dura = 0;

			public void run() {
				
				dura += 1;
				
				if (dura % 1 == 0) {
					if (NumUtil.randInt(1, 4) == 2) {
						event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.5f, 0.1f * NumUtil.randInt(1, 20));
					}
					for(int i = 0; i < 5; i++) {
						Location loc1 = event.getEntity().getLocation();
						int r = 20;
						loc1.add(NumUtil.randInt(-r, r), NumUtil.randInt(-r, r), NumUtil.randInt(-r, r));
						Location loc2 = event.getEntity().getLocation();
						loc2.add(NumUtil.randInt(-r, r), NumUtil.randInt(-r, r), NumUtil.randInt(-r, r));
						ParticleLine pl = new ParticleLine(player.getWorld(), Main.getPlugin(Main.class), Particle.SPELL_INSTANT, loc1, loc2, 8);
						pl.createTrail();
					}
					for (Entity e : entities) {
						if (e != player && NumUtil.randInt(1, 4) == 2) {
							try {
								Damageable d = (Damageable) e;
								d.damage(3);
								event.getEntity().getWorld().spawnParticle(Particle.ELECTRIC_SPARK, ((Entity)e).getLocation(), 1, 1, 1, 1, null);
								if (e.isDead()) {
									plugin.playerUtil.killed(player);
								}
								
							} catch (Exception ex) {

							}
						}
					}
				}
				
				if (dura >= 60) {
					cancel();
				}
			}
			
		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void pyroStaffHeal(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "PREPARING HEAL...");
		// this should always be true. if its not, something went wrong LOLE the only reason for this is so no duplicates exist.
		if (!plugin.abilityCooldownList.contains(player.getName())) {
			plugin.abilityCooldownList.add(player.getName());
		}
		new BukkitRunnable() {
			int ticks = 0;
			@Override
			public void run() {
				ticks += 1;
				if (ticks >= 3) {
					if (plugin.playerUtil.checkItemInHand(Material.BLAZE_ROD, "Pyromancy Staff", event)) {
						healThrow(event);	
					} else {
						player.sendMessage(plugin.playerUtil.cancelAbilityMessage());
					}
					if (plugin.abilityCooldownList.contains(player.getName())) {
						plugin.abilityCooldownList.remove(player.getName());
					}
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 20);
	}
		
	public void judgmentCut(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block looking = player.getTargetBlock((Set<Material>) null, 25);
		ParticleLine pl = new ParticleLine(player.getWorld(), Main.getPlugin(Main.class), Particle.ASH, player.getLocation(), looking.getLocation().getBlock().getLocation(), 5);
		pl.spawnLineDelay();
	}
	
	public void fireOrbRightClickAbility(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		boolean hitEntity = false;
		world.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 0.1f);
		BlockIterator bi = new BlockIterator(player, 20);
		while(bi.hasNext()) {
			Block next = bi.next().getLocation().getBlock();
			Collection<Entity> entities = next.getWorld().getNearbyEntities(next.getLocation(), 1, 1, 1);
			for (Entity e : entities) {
				if (e != player && e instanceof Damageable) {
					ParticleLine pl = new ParticleLine(player.getWorld(), Main.getPlugin(Main.class), Particle.FLAME, player.getLocation().add(0, 2, 0), e.getLocation().add(0, 1.5, 0), 1);
					pl.createTrailCurve(2, 3);
					try {
						Damageable d = (Damageable) e;
						hitEntity = true;
						d.damage(3);
						e.setFireTicks(100);
						
						if (e.isDead()) {
							plugin.playerUtil.killed(player);
						}
						return;
					} catch (Exception ex) {

					}
				}

			}

		}
		if (!hitEntity) {
			Block looking = player.getTargetBlock((Set<Material>) null, 20);
			ParticleLine pl = new ParticleLine(player.getWorld(), Main.getPlugin(Main.class), Particle.FLAME, player.getLocation().add(0, 2, 0), looking.getLocation(), 1);
			pl.createTrailCurve(2, 3);
		}
		//				RayTraceResult r = player.rayTraceEntities(player.getLocation(), player.getLocation().getLo;
//		if (r != null) {
//			Entity e = r.getHitEntity();
//			if (!(e instanceof Player)) {
//				e.setFireTicks(20);
//
//			}
//		}
	}
	
	// JUDGMENT CUT END
	public void judgmentCutEnd(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			World world = p.getWorld();
			Collection<Entity> entities = world.getNearbyEntities(p.getLocation(), 20, 10, 20);
			Location original = p.getLocation();
			Location l = p.getLocation();
			double x = l.getX();
			double y = l.getY();
			double z = l.getZ();
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
					p.getWorld().spawnParticle(Particle.GLOW, p.getLocation(), 3, null);
					if (ticks > delay * 10) {
						if(dura % 3 == 0) {
							world.playSound(p.getLocation(), Sound.ENTITY_ALLAY_HURT, 2.0f, 0.5f);
							
						}
						dura++;
						ticks = 0;
						double ox = l.getX();
						double oy = l.getY();
						double oz = l.getZ();
						Location start = new Location(world, ox, oy, oz);
						int angle = (int)(Math.random() * 360);
						double rads = Math.toRadians(angle);
						l.setX(x + (Math.random() * 26) - 13);
//						l.setX(x + 10*Math.cos(rads));
						l.setY(y + (Math.random() * 7) + 1);
						l.setZ(z + (Math.random() * 26) - 13);
//						l.setZ(z + 10*Math.sin(rads));
						
						l.setDirection(new Vector(Math.random()*360, Math.random()*10, Math.random()*360));

						p.teleport(l);

						p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, p.getLocation(), 40, null);
						ParticleLine pl = new ParticleLine(p.getWorld(), Main.getPlugin(Main.class), Particle.ELECTRIC_SPARK, start, l, 90);
						pl.spawnLine();
						if (dura >= 20) {
//							done = true;
							new BukkitRunnable() {

								@Override
								public void run() {
									for (Entity e : entities) {
										if (e != p) {
											try {
												Damageable d = (Damageable) e;
												d.damage(2400);
												if (e.isDead()) {
													plugin.playerUtil.killed(p);
												}
//												d.getLocation().getDirection().multiply(2).setY(2);
											} catch (Exception ex) {

											}
										}
									}
									p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 1, null);
									p.getWorld().playSound(original, Sound.BLOCK_GLASS_BREAK, 2.0f, 0.20f);
									p.getWorld().playSound(original, Sound.BLOCK_GLASS_BREAK, 2.0f, 5.0f);
									p.getWorld().playSound(original, Sound.BLOCK_WOOD_BREAK, 2.0f, 1.0f);
									p.getWorld().playSound(original, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.0f, 0.25f);
									
									BlockData dustData = Material.STONE.createBlockData();
									for(int i = 0; i < 20; i++) {
										p.getWorld().spawnParticle(Particle.BLOCK_DUST, p.getLocation().add(NumUtil.randInt(-5, 5), NumUtil.randInt(-1, 2), NumUtil.randInt(-5, 5)), 40, dustData);
									}
									cancel();

								}

							}.runTaskLater(Main.getPlugin(Main.class), 30);
							p.teleport(original);
							cancel();
						}
					}
				}
			}.runTaskTimer(plugin, 0, 1);
		}
	
	public void fireOrbLeftClickAbility(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		World world = player.getWorld();
		world.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0f, 0.1f);
		
		// curved shit
		new BukkitRunnable() {
			int r = 20;
			Collection<Entity> entities = world.getNearbyEntities(player.getLocation(), r, r/4, r);
			int dura = 0;
			double red = 255/255D;
			double green = 237/255D;
			double blue = 137/255D;
			public void run() {
				
				if (NumUtil.randInt(1, 4) == 2) {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, (0.1f) * NumUtil.randInt(1, 10), 0.1f * NumUtil.randInt(1, 20));
				}
				
				dura += 1;
				
				
				if (dura % 1 == 0) {
					for(int i = 0; i < 3; i++) {

						
//						ItemStack itemCrackData = new ItemStack(Material.GOLD_BLOCK);
//						player.spawnParticle(Particle.ITEM_CRACK, player.getLocation(), 40, itemCrackData);
						
						DustTransition dustTransition = new DustTransition(Color.fromRGB(255, 187, 0), Color.fromRGB(255, 0, 0), 1.0F);
						player.spawnParticle(Particle.DUST_COLOR_TRANSITION, player.getLocation(), 50, dustTransition);
						
//						DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 127, 255), 1.0F);
//						player.spawnParticle(Particle.REDSTONE, player.getLocation().add(0,2.5,0), 50, dustOptions);
						
						Location endL = player.getLocation();
						endL.add(NumUtil.randInt(-r, r), NumUtil.randInt((int)-r/2, r/2), NumUtil.randInt(-r, r));
						ParticleLine pl = new ParticleLine(player.getWorld(), Main.getPlugin(Main.class), Particle.FLAME, player.getLocation().add(0,1,0), endL, 15);
						pl.createTrailCurve(2, 3);
					}
					for (Entity e : entities) {
						if (e != player && NumUtil.randInt(1, 4) == 2) {
							try {
								Damageable d = (Damageable) e;
								d.damage(3);
								world.spawnParticle(Particle.LAVA, ((Entity)e).getLocation(), 1, 1, 1, 1, null);
								e.setFireTicks(5);
								if (e.isDead()) {
									plugin.playerUtil.killed(player);
								}
							} catch (Exception ex) {

							}
						}
					}
				}
				
				if (dura >= 60) {
					cancel();
				}
			}
			
		}.runTaskTimer(plugin, 0, 1);
	}
}

// iterator?
//BlockIterator bi = new BlockIterator(player, 100);
//Block lastBlock = bi.next();
//while(bi.hasNext()) {
//lastBlock = bi.next();
//int s = 3;
//for(int i = -s; i < s; i++) {
//for (int j = -s; j < s; j++) {
//for (int k = -s; k < s; k++) {
//Block blockNext = lastBlock.getLocation().add(i, j, k).getBlock();
//blockNext.setType(Material.AIR);
//}
//}
//}

// CIRCLE ABILITY SO FAR (tornado?)
//new BukkitRunnable() {
//	Block looking = player.getTargetBlock((Set<Material>) null, 20);
//	
////	Collection<Entity> entities = world.getNearbyEntities(player.getLocation(), r, r/4, r);
//	int dura = 0;
//	double radius = 3;
//
//	public void run() {
//
//		Location startL = new Location(world, looking.getLocation().getX(), looking.getLocation().getY(), looking.getLocation().getZ());
//		Location endL = player.getLocation();
//		
//		
//		if (NumUtil.randInt(1, 4) == 2) {
//			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (0.1f) * NumUtil.randInt(1, 10), 0.1f * NumUtil.randInt(1, 20));
//		}
//		
//		dura += 1;
//		
//		if (dura % 1 == 0) {
////				endL.add(NumUtil.randInt(-r, r), NumUtil.randInt((int)-r/2, r/2), NumUtil.randInt(-r, r));
////			if (dura % (60/4) == 0) {
////				startL.add(0, -(60/4), 0);
////				radius = 2.5;
////			}
//			ParticleLine pl = new ParticleLine(player, Main.getPlugin(Main.class), Particle.SMOKE_LARGE, startL.add(NumUtil.randInt(-3, 3), 0, NumUtil.randInt(-3, 3)), endL, 15);
//			pl.createTrailCircle(radius);
//
//			
//			// damage
////			for (Entity e : entities) {
////				if (e != player && NumUtil.randInt(1, 4) == 2) {
////					try {
////						Damageable d = (Damageable) e;
////						d.damage(3);
////						world.spawnParticle(Particle.LAVA, ((Entity)e).getLocation(), 1, 1, 1, 1, null);
////						e.setFireTicks(5);
////						if (e.isDead()) {
////							plugin.playerUtil.killed(player);
////						}
////					} catch (Exception ex) {
////
////					}
////				}
////			}
//			
//		}
//		
//		if (dura >= 60) {
//			cancel();
//		}
//	}
//	
//}.runTaskTimer(plugin, 0, 1);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




//				Location startL = player.getLocation();
//Entity fireball = (Entity)player.getWorld().spawnEntity(startL, EntityType.FIREBALL);
//Fireball f = (Fireball) fireball;	
//MetadataValue m = new FixedMetadataValue(plugin, 3);
//f.setMetadata("fireballLevel2", m);
//f.setDirection(player.getLocation().getDirection());
//f.setVelocity(player.getLocation().getDirection().multiply(3));

//
//Entity fireball = (Entity)player.getWorld().spawnEntity(startL, EntityType.FIREBALL);
//Fireball f = (Fireball) fireball;	
//MetadataValue m = new FixedMetadataValue(plugin, 3);
//f.setMetadata("fireballLevel1", m);
//f.setDirection(player.getLocation().getDirection());
//f.setVelocity(player.getLocation().getDirection().multiply(3));


//Plugin p = plugin;
// grid of fireballs raining down cool
//new BukkitRunnable() {
//	double delay = 0;
//	int ticks = 0;
//	boolean done = false;
//
//	
//	int dura = 0;
//	public void run() {
//		if (done)
//			return;
//		ticks++;
//		delay = 1/20;
//		if (ticks > delay * 10) {
//			dura++;
//			ticks = 0;
//			for(int i = 0; i < 20; i++) {
//				Location loc = looking.getLocation().getBlock().getLocation();
//				loc.add(i - 10, 20, dura);
//				Entity fireball = (Entity)player.getWorld().spawnEntity(loc, EntityType.FIREBALL);
//				Fireball f = (Fireball) fireball;	
//				MetadataValue m = new FixedMetadataValue(p, 3);
//				f.setMetadata("fireballLevel1", m);
//				f.setDirection(new Vector(0, -1, 0));
//				f.setVelocity(new Vector(0, -1, 0));
//			}
//			
//			if (dura >= 20) {
//				cancel();
//			}
//		}
//	}
//}.runTaskTimer(this, 0, 1);




//new BukkitRunnable() {
//double delay = 0;
//int ticks = 0;
//boolean done = false;
//
//
//int dura = 0;
//public void run() {
//	if (done)
//		return;
//	ticks++;
//	delay = 1/20;
//	if (ticks > delay * 10) {
//		dura++;
//		ticks = 0;
//		for(int i = 0; i < 20; i++) {
//			Location loc = looking.getLocation().getBlock().getLocation();
//			loc.add(i - 10, 20, dura);
//			Entity fireball = (Entity)player.getWorld().spawnEntity(loc, EntityType.FIREBALL);
//			Fireball f = (Fireball) fireball;	
//			MetadataValue m = new FixedMetadataValue(p, 3);
//			f.setMetadata("fireballLevel1", m);
//			f.setDirection(new Vector(0, -1, 0));
//			f.setVelocity(new Vector(0, -1, 0));
//		}
//		
//		if (dura >= 20) {
//			cancel();
//		}
//	}
//}
//}.runTaskTimer(this, 0, 1);


// deleting a bunch of blocks in your direction (kamehameha!)
//				BlockIterator bi = new BlockIterator(player, 100);
//Block lastBlock = bi.next();
//while(bi.hasNext()) {
//	lastBlock = bi.next();
//	int s = 3;
//	for(int i = -s; i < s; i++) {
//		for (int j = -s; j < s; j++) {
//			for (int k = -s; k < s; k++) {
//				Block blockNext = lastBlock.getLocation().add(i, j, k).getBlock();
//				blockNext.setType(Material.AIR);
//			}
//		}
//	}
//	
//}


// cool kamehameha 
//				World world = player.getWorld();
//world.playSound(player.getLocation(), Sound.BLOCK_CAMPFIRE_CRACKLE, 2.0f, 1.0f);
//
//Block looking = player.getTargetBlock((Set<Material>) null, 100);
//
//Location startL = player.getLocation();
//Location endL = looking.getLocation().getBlock().getLocation();
//ParticleLine pl = new ParticleLine(player, Main.getPlugin(Main.class), Particle.CLOUD, startL, endL, 120);
//looking.setType(Material.COBBLESTONE);
////BlockIterator bi = new BlockIterator(world, player.getLocation().getDirection(), player.getLocation().getDirection(), 0, 100);
//BlockIterator bi = new BlockIterator(player, 100);
//Block lastBlock = bi.next();
//while(bi.hasNext()) {
//	lastBlock = bi.next();
//	int s = 3;
//	for(int i = -s; i < s; i++) {
//		for (int j = -s; j < s; j++) {
//			for (int k = -s; k < s; k++) {
//				Block blockNext = lastBlock.getLocation().add(i, j, k).getBlock();
//				blockNext.setType(Material.AIR);
//			}
//		}
//	}
//	
//}


