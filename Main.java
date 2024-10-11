package me.Macpaper.GodSword;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import me.Macpaper.GodSword.Items.CoolItems;
import me.Macpaper.GodSword.abilities.SwordAbilities;
import me.Macpaper.GodSword.disableDefaults.DefaultManager;
import me.Macpaper.GodSword.sql.MySQL;
import me.Macpaper.GodSword.sql.SQLGetter;
import me.Macpaper.GodSword.utils.NumUtil;
import me.Macpaper.GodSword.utils.PlayerUtil;

public class Main extends JavaPlugin implements Listener {
	public List<String> list = new ArrayList<String>();
	public static final double MAX_PLAYER_HEALTH = 1000.0;
	// array list of players who cannot use another ability currently. probably don't need multiple? you don't really want to cast multiple abilities at the same exact time anyway
	public List<String> abilityCooldownList = new ArrayList<String>();
	public HashMap<String, Long> wallJumpCDR = new HashMap<String, Long>();

	public HashMap<String, LivingEntity> lockOnTargets = new HashMap<String, LivingEntity>();
	
	ToughZombie joe = new ToughZombie(EntityType.ZOMBIE, "Tough Zombie", 900000.0, true);
	EntityType[] troops = { EntityType.ZOMBIE, EntityType.PIGLIN, EntityType.SKELETON, EntityType.CREEPER, EntityType.BAT,
			EntityType.EVOKER, EntityType.AXOLOTL, EntityType.BLAZE, EntityType.WITHER_SKELETON };

	public MySQL SQL;
	public SQLGetter data;
	
	public DefaultManager defaultManager = new DefaultManager(this);
	public PlayerUtil playerUtil = new PlayerUtil(this);
	public SwordAbilities swordAbilities = new SwordAbilities(this);
	public CoolItems coolItems = new CoolItems();
	@Override
	public void onEnable() {
		this.SQL = new MySQL();
		this.data = new SQLGetter(this);
		try {
			SQL.connect();
		} catch (ClassNotFoundException | SQLException e) {
			// e.printStackTrace();
			// Login info is incorrect
			// they are not using a database
			Bukkit.getLogger().info("Database not connected");
		}
		
		if (SQL.isConnected()) {
			Bukkit.getLogger().info("Database is connected");
			data.createTable();
			this.getServer().getPluginManager().registerEvents(this, this); // only register events if database is connected (or risk errors if not)
			this.getServer().getPluginManager().registerEvents(defaultManager, this);
			this.getServer().getPluginManager().registerEvents(swordAbilities, this);
		}
//		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getPluginManager().registerEvents(joe, this);
			
		HandlerList l = new HandlerList();
		ArrayList<HandlerList> lists = l.getHandlerLists();
		joe.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
		joe.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		joe.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
		joe.setBoots(new ItemStack(Material.IRON_BOOTS));

		joe.setHandItem(new ItemStack(Material.STONE_SWORD));
	}

	@Override
	public void onDisable() {
		SQL.disconnect();
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("stfu console");
			return true;
		}
		Player player = (Player) sender;
		
		// if sender is player and label (command) == "egg", give new ItemStack with this message. if true, return out of here.
		if (playerUtil.givePlayerItem(sender, label, "egg", new ItemStack(Material.EGG, 16), ChatColor.GOLD + "Egg.")) {
			return true;
		}
		if (playerUtil.givePlayerItem(sender, label, "bleeder", coolItems.getTritip(), ChatColor.RED + "Tri-tip blade given.")) {
			return true;
		}
		
		if (playerUtil.givePlayerItem(sender, label, "godsword", coolItems.getSword(), ChatColor.GOLD + "Granted GODSWORD SLASHER INSANE BLOOD POWER STRENGTH")) {
			return true;
		}

		if (playerUtil.givePlayerItem(sender, label, "lightningbow", coolItems.getLightningBow(), ChatColor.DARK_BLUE + "Granted LIGHTNING BOW KILL FUCKER")) {
			return true;
		}
		
		if (playerUtil.givePlayerItem(sender, label, "fireorb", coolItems.getBlazeFireOrb(), ChatColor.RED + "Granted FIRE ORB OF UNSPEAKABLE HORROR")) {
			return true;
		}
		
		if (playerUtil.givePlayerItem(sender, label, "pyrostaff", coolItems.getPyromancyStaff(), ChatColor.DARK_RED + "Granted PYRO STAFF OF POTENT POWER")) {
			return true;
		}
		
		if (playerUtil.givePlayerItem(sender, label, "monsoon", coolItems.getMonsoonBow(), ChatColor.DARK_RED + "Granted BOW THAT RAINS A TORRENT OF A THOUSAND ARROWS")) {
			return true;
		}
		if (playerUtil.givePlayerItem(sender, label, "estus", coolItems.getAmbrosia(), ChatColor.GOLD + "Granted AMBROSIA FLASK")) {
			return true;
		}
		if (playerUtil.givePlayerItem(sender, label, "hellzone", coolItems.getHellzoneGrenade(), ChatColor.GOLD + "Granted HELLZONE GRENADE")) {
			return true;
		}
		if (playerUtil.givePlayerItem(sender, label, "red", coolItems.getRed(), ChatColor.RED + "Granted HONORED ONE RED")) {
			return true;
		}
		if (playerUtil.givePlayerItem(sender, label, "blue", coolItems.getBlue(), ChatColor.BLUE + "Granted HONORED ONE BLUE")) {
			return true;
		}
		
		if (label.equalsIgnoreCase("testroom")) {
			player.sendMessage(ChatColor.AQUA + "Here are all the items in the game lol haha");
			BlockIterator bi = new BlockIterator(player, 5);
			Block lastBlock = bi.next();
			while (bi.hasNext()) {
				lastBlock = bi.next();
			}
			lastBlock.setType(Material.CHEST);
			Chest chest = (Chest) lastBlock.getState();
			chest.getInventory().addItem(coolItems.getAmbrosia());
			chest.getInventory().addItem(coolItems.getRed());
			chest.getInventory().addItem(coolItems.getBlue());
			chest.getInventory().addItem(coolItems.getHellzoneGrenade());
			chest.getInventory().addItem(coolItems.getMonsoonBow());
			chest.getInventory().addItem(coolItems.getPyromancyStaff());
			chest.getInventory().addItem(coolItems.getBlazeFireOrb());
			chest.getInventory().addItem(coolItems.getLightningBow());
			chest.getInventory().addItem(coolItems.getSword());
			chest.getInventory().addItem(coolItems.getTritip());
			chest.getInventory().addItem(new ItemStack(Material.EGG, 16));
			chest.getInventory().addItem(new ItemStack(Material.ARROW, 64));
			return true;
		}
		
		if (label.equalsIgnoreCase("spawnarmy")) {
			spawnArmy(player);
			player.sendMessage(ChatColor.GOLD + "You have spawned da ARMY OF DARKNESS");
			return true;
		}
		
		if (label.equalsIgnoreCase("spawnjoe")) {
			spawnJoe(player, joe);
			player.sendMessage(ChatColor.GOLD + "You have spawned THE CHOSEN ONE.");
			return true;
		}
		
		return false;
	}
	
	
	public void spawnJoe(Player p, ToughZombie j) {
		Location l = p.getLocation();
		World w = p.getWorld();
		j.spawnEntityAt(w, l);
		LivingEntity mob = (LivingEntity)p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
		mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(2);
		mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500);
		mob.setHealth(mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		mob.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(200);
		mob.setCustomName(ChatColor.RED + "" + ChatColor.BOLD + "" + "THE CHOSEN ONE");
		mob.setCustomNameVisible(true);
		
	}
	
	public void spawnArmy(Player p) {
		World w = p.getWorld();
		Location original = p.getLocation();
		Location l = p.getLocation();
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
						l.setX(original.getX() + (Math.random() * 40) - 20);
						l.setZ(original.getZ() + (Math.random() * 40) - 20);
						l.setY(original.getY() + (Math.random() * 2));
						w.spawnEntity(l, troops[(int)(Math.random()*troops.length)]);
					}
					
					if (dura >= 20) {
						cancel();
					}
				}
			}
		}.runTaskTimer(this, 0, 1);
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		System.out.println(e.getPlayer().getLocation().getDirection());
	}
	
	@EventHandler
	public void wallJump(PlayerToggleFlightEvent e) {
		e.setCancelled(true);
		Player p = e.getPlayer();

		if (p.getGameMode() == GameMode.SURVIVAL) {
			if (wallJumpCDR.containsKey(p.getName()) && System.currentTimeMillis() - wallJumpCDR.get(p.getName()) < 500) {
				p.sendMessage("Walljump on Cooldown: " + (500 - (System.currentTimeMillis() - wallJumpCDR.get(p.getName())))/1000);
				return;
			} else {
				p.setAllowFlight(false);
				p.setFlying(false);
				wallJumpCDR.put(p.getName(), System.currentTimeMillis());
				BlockIterator bi = new BlockIterator(p, 2);
				Block lastBlock = bi.next();
				while (bi.hasNext()) {
					if (!lastBlock.isPassable()) {
						p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, lastBlock.getLocation(), 1);

						Vector playerVec = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
						Vector blockVec = new Vector(lastBlock.getLocation().getX(), lastBlock.getLocation().getY(), lastBlock.getLocation().getZ());
						System.out.println(p.getLocation().getDirection().angle(blockVec));
						double xl = Math.abs(p.getLocation().getDirection().getX());
						double zl = Math.abs(p.getLocation().getDirection().getZ());
						double mag = 0.75;
						if (xl >= zl) {
							e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(new Vector(-mag, mag, mag)).setY(0.5));
						} else {
							e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(new Vector(mag, mag, -mag)).setY(0.5));
						}
						break;
					}
					lastBlock = bi.next();
				}
				new BukkitRunnable() {
					int ticks = 0;
					int duration = 10;
					@Override
					public void run() {
						ticks += 1;
						if (ticks > duration) {
//							p.setAllowFlight(true);
//							p.setFlying(false);
							cancel();
						}
					}
				}.runTaskTimer(this, 0, 1);
			}

		} 

//		if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR || p.isFlying()) {
//			return;
//		} else {
//			p.setAllowFlight(false);
//			p.setFlying(false);
//			p.setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.5).setY(1));
//			p.setAllowFlight(true);
//		}
	}
	


	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.setAllowFlight(true);
		player.setFlying(false);
		data.createPlayer(player);
		lockOnTargets.put(player.getName(), null);
	}

	// checks if player has a locked on enemy. return false if enemy is dead or enemy doesnt exist
	public boolean isLockedOn(Player player) {
		return (this.lockOnTargets.get(player.getName()) != null && this.lockOnTargets.get(player.getName()).getHealth() != 0);
	}
	
	@EventHandler
	public void onExplodingFireball(EntityExplodeEvent e) {
		if (e.getEntity() instanceof Fireball) {
			Fireball f = (Fireball) e.getEntity();
			if (f.hasMetadata("fireballLevel1")) {
//				Location l = e.getLocation();

				Location locX = e.getLocation();
				int[] vL = {-1, 1};
//				Location l = e.getLocation();
//				e.getEntity().getWorld().spawnEntity(l, EntityType.LIGHTNING);
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
						for(int i = 0; i < 20; i++) {
							Location l = locX.getBlock().getLocation();
							l.add(NumUtil.randInt(-3, 3), 10, NumUtil.randInt(-3, 3));
							Entity fireball = e.getEntity().getWorld().spawnEntity(l, EntityType.FIREBALL);
							Fireball f = (Fireball) fireball;
							
							
							f.setDirection(new Vector(1, NumUtil.randInt(-1, 1), NumUtil.randInt(-1, 1)));
							f.setVelocity(new Vector(1, NumUtil.randInt(-1, 1), NumUtil.randInt(-1, 1)));
						}
						
						if (dura >= 20) {
							cancel();
						}
					}
				}
			}.runTaskTimer(this, 0, 1);
			}
			if (f.hasMetadata("fireballLevel2")) {
				Location l = e.getLocation();
				e.getEntity().getWorld().playSound(l, Sound.AMBIENT_CAVE, 1.0f, 1.0f);
				
			}
		}

	}
	
	
	public boolean consoleTypesCommand(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("stfu console");
			return true;
		}
		return false;
		
	}
}
