package me.Macpaper.GodSword.abilities;

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
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
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
	
	
	// JUDGMENT CUT END
	public void tele(Player p) {
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
//					l.setX(x + 10*Math.cos(rads));
					l.setY(y + (Math.random() * 7) + 1);
					l.setZ(z + (Math.random() * 26) - 13);
//					l.setZ(z + 10*Math.sin(rads));
					
					l.setDirection(new Vector(Math.random()*360, Math.random()*10, Math.random()*360));

					p.teleport(l);

					p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, p.getLocation(), 40, null);
					ParticleLine pl = new ParticleLine(p, Main.getPlugin(Main.class), Particle.ELECTRIC_SPARK, start, l, 90);
					pl.spawnLine();
					if (dura >= 20) {
//						done = true;
						new BukkitRunnable() {

							@Override
							public void run() {
								for (Entity e : entities) {
									if (e != p) {
										try {
											Damageable d = (Damageable) e;
											d.damage(50);
											if (e.isDead()) {
												plugin.playerUtil.killed(p);
											}
//											d.getLocation().getDirection().multiply(2).setY(2);
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
	
	@EventHandler
	public void onBowShoot(EntityShootBowEvent event) {
		if (event.getBow().getItemMeta().hasLore() && event.getBow().getItemMeta().getDisplayName().contains("Lightning Bow")) {
			event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.LIGHTNING);
			MetadataValue mdv = new FixedMetadataValue(plugin, "bowlshit");
			event.getProjectile().setMetadata("lightningarrow", mdv);
		}
	}
	
	@EventHandler
	public void onProjectileLand(ProjectileHitEvent event) {
		if (event.getEntity().hasMetadata("lightningarrow")) {
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
							ParticleLine pl = new ParticleLine(player, Main.getPlugin(Main.class), Particle.SPELL_INSTANT, loc1, loc2, 8);
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
	
	public void healThrow(Player player) {
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
	
	@EventHandler()
	public void onClick(PlayerInteractEvent event) {
		Player player = (Player) event.getPlayer();

		// before ANY abilities can happen, first just check if player already has a cool-down. (with this in mind, make sure to remove players from the cdr list for every single ability.
		if (plugin.abilityCooldownList.contains(player.getName())) {
			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "* ABILITIES ON COOLDOWN *");
			return;
		}
		

		if (plugin.playerUtil.checkItemInHand(Material.BLAZE_ROD, "Pyromancy Staff", event)) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR) {
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
								healThrow(player);	
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
		}
		
		
		if (plugin.playerUtil.checkItemInHand(Material.NETHERITE_SWORD, "Yamato Blade", event)) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				// for cooldowns
//				if (!list.contains(player.getName())) {
//					list.add(player.getName());
//				}
				
				// JUDGMENT CUT END
				tele(player);
			}
			if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
				// JUDGMENT CUT
				Block looking = player.getTargetBlock((Set<Material>) null, 25);
				ParticleLine pl = new ParticleLine(player, Main.getPlugin(Main.class), Particle.ASH, player.getLocation(), looking.getLocation().getBlock().getLocation(), 5);
				pl.spawnLineDelay();
			}
		}


		
		if (plugin.playerUtil.checkItemInHand(Material.BLAZE_POWDER, "Fire Orb", event)) {
			World world = player.getWorld();
			
			if (event.getAction() == Action.RIGHT_CLICK_AIR) {
				boolean hitEntity = false;
				world.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 0.1f);
				BlockIterator bi = new BlockIterator(player, 20);
				while(bi.hasNext()) {
					Block next = bi.next().getLocation().getBlock();
					Collection<Entity> entities = next.getWorld().getNearbyEntities(next.getLocation(), 1, 1, 1);
					for (Entity e : entities) {
						if (e != player && e instanceof Damageable) {
							ParticleLine pl = new ParticleLine(player, Main.getPlugin(Main.class), Particle.FLAME, player.getLocation().add(0, 2, 0), e.getLocation().add(0, 1.5, 0), 1);
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
					ParticleLine pl = new ParticleLine(player, Main.getPlugin(Main.class), Particle.FLAME, player.getLocation().add(0, 2, 0), looking.getLocation(), 1);
					pl.createTrailCurve(2, 3);
				}
				//				RayTraceResult r = player.rayTraceEntities(player.getLocation(), player.getLocation().getLo;
//				if (r != null) {
//					Entity e = r.getHitEntity();
//					if (!(e instanceof Player)) {
//						e.setFireTicks(20);
//
//					}
//				}
			}
			
//			BlockIterator bi = new BlockIterator(player, 100);
//Block lastBlock = bi.next();
//while(bi.hasNext()) {
//lastBlock = bi.next();
//int s = 3;
//for(int i = -s; i < s; i++) {
//	for (int j = -s; j < s; j++) {
//		for (int k = -s; k < s; k++) {
//			Block blockNext = lastBlock.getLocation().add(i, j, k).getBlock();
//			blockNext.setType(Material.AIR);
//		}
//	}
//}
			
			
			if (event.getAction() == Action.LEFT_CLICK_AIR) {
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

								
//								ItemStack itemCrackData = new ItemStack(Material.GOLD_BLOCK);
//								player.spawnParticle(Particle.ITEM_CRACK, player.getLocation(), 40, itemCrackData);
								
								DustTransition dustTransition = new DustTransition(Color.fromRGB(255, 187, 0), Color.fromRGB(255, 0, 0), 1.0F);
								player.spawnParticle(Particle.DUST_COLOR_TRANSITION, player.getLocation(), 50, dustTransition);
								
//								DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 127, 255), 1.0F);
//								player.spawnParticle(Particle.REDSTONE, player.getLocation().add(0,2.5,0), 50, dustOptions);
								
								Location endL = player.getLocation();
								endL.add(NumUtil.randInt(-r, r), NumUtil.randInt((int)-r/2, r/2), NumUtil.randInt(-r, r));
								ParticleLine pl = new ParticleLine(player, Main.getPlugin(Main.class), Particle.FLAME, player.getLocation().add(0,1,0), endL, 15);
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
		
		// for cooldowns i think
//		if (list.contains((event.getPlayer().getName()))) {
//			list.remove(event.getPlayer().getName());
//		}
	}
	
	Color[] colors = { Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GREEN,
			Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE,
			Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW };
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
	
}

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


