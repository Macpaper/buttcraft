package me.Macpaper.GodSword;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Macpaper.GodSword.utils.NumUtil;

public class ParticleLine {
	Location start;
	Location end;
	double duration;
	Particle particle;
	JavaPlugin plugin;

	World world;
	
	public ParticleLine(World world, JavaPlugin plugin, Particle particle, Location start, Location end,
			double duration) {
		this.particle = particle;
		this.plugin = plugin;
		this.start = start;
		this.end = end;
		this.duration = duration;
		
		this.world = world;
	}
	
	public void spawnLineDelay() {
		World world = start.getWorld();

		double diffX = start.getX() - end.getX();
		double diffY = start.getY() - end.getY();
		double diffZ = start.getZ() - end.getZ();
		double dist = Math.sqrt((diffX * diffX + diffY * diffY + diffZ * diffZ));
		int numberOfParticles = (int) (dist * 10);
		double ox = start.getX();
		double oy = start.getY();
		double oz = start.getZ();
		new BukkitRunnable() {
			int ticks = 0;
			int dura = 0;
			boolean done = false;

			int i = 0;

			@Override
			public void run() {
				if (done)
					return;
				ticks++;
				dura++;


				for(int i = 0; i < dura*numberOfParticles/duration; i++) {
//					i = (int) (numberOfParticles * dura/duration);

					Location l = start;
					double x = ox - diffX / numberOfParticles * i;
					double y = oy - diffY / numberOfParticles * i;
					double z = oz - diffZ / numberOfParticles * i;
					l.setX(x);
					l.setY(y);
					l.setZ(z);
					world.spawnParticle(particle, l, 1, 0, 0, 0, 0, null);
				}

				
				if (dura >= duration) {
					cancel();
					// ???? apparently explode for judgment cut regular
					world.createExplosion(end.getX(), end.getY(), end.getZ(), 10, false, false);
				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void createTrail() {
		World world = start.getWorld();

		double diffX = start.getX() - end.getX();
		double diffY = start.getY() - end.getY();
		double diffZ = start.getZ() - end.getZ();
		double dist = Math.sqrt((diffX * diffX + diffY * diffY + diffZ * diffZ));
		int numberOfParticles = (int) (dist * 10);
		double ox = start.getX();
		double oy = start.getY();
		double oz = start.getZ();
		new BukkitRunnable() {
			int ticks = 0;
			int dura = 0;
			boolean done = false;

			int i = 0;

			@Override
			public void run() {
				if (done)
					return;
				ticks++;
				dura++;


				for(int i = (int) (dura*(numberOfParticles/duration)/5*3); i < dura*numberOfParticles/duration; i++) {
//					i = (int) (numerOfParticles * dura/duration);

					Location l = start;
					double x = ox - diffX / numberOfParticles * i;
					double y = oy - diffY / numberOfParticles * i;
					double z = oz - diffZ / numberOfParticles * i;
					l.setX(x);
					l.setY(y);
					l.setZ(z);
					world.spawnParticle(particle, l, 1, 0, 0, 0, 0, null);
				}

				
				if (dura >= duration) {
					cancel();
				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void createTrailCircle(double radius) {
//
//		double diffX = start.getX() - end.getX();
//		double diffY = start.getY() - end.getY();
//		double diffZ = start.getZ() - end.getZ();
//		double dist = Math.sqrt((diffX * diffX + diffY * diffY + diffZ * diffZ));
//		int numberOfParticles = (int) (dist * 10);
//		double ox = start.getX();
//		double oy = start.getY();
//		double oz = start.getZ();
		
		new BukkitRunnable() {
			int dura = 0;
			int ticks = 0;

			@Override
			public void run() {
				ticks++;
				dura++;

//				for(int i = 0; i < 100; i++) {

//					

//					l.add(radius * Math.cos(rads), 0, radius * Math.sin(rads));
					if (dura % 2 == 0) {
						for(int i = 0; i < 50; i++) {
							double angle = 360*(((double)i)/50);
							double rads = Math.toRadians(angle);
							
							Location l = new Location(start.getWorld(), start.getX(), start.getY(), start.getZ());
							l.setX(l.getX() + radius * Math.cos(rads));
							l.setZ(l.getZ() + radius * Math.sin(rads));
//							double what = radius * Math.cos(rads);
//							if (dura % 14 == 0) {
////								player.sendMessage("x: " + l.getX() + " y: " + l.getY() + " z: " + l.getZ());
//								player.sendMessage("adding x: " + what);
//							}

							world.spawnParticle(particle, l, 1, 0, 0, 0, 0, null);
						}
					}

//				}

//				player.getWorld().spawnEntity(start, EntityType.LIGHTNING);
				
				
//				
//				for(int i = (int) (dura*(numberOfParticles/duration)/5*3); i < dura*numberOfParticles/duration; i++) {
////					i = (int) (numerOfParticles * dura/duration);
//
//					Location l = start;
//					double x = ox - diffX / numberOfParticles * i;
//					double y = oy - diffY / numberOfParticles * i;
//					double z = oz - diffZ / numberOfParticles * i;
//					l.setX(x);
//					l.setY(y);
//					l.setZ(z);
//					world.spawnParticle(particle, l, 1, 0, 0, 0, 0, null);
//				}

				
				if (dura >= duration) {
					cancel();
				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void createTrailCurve(int r, int lifeTime) {
		World world = start.getWorld();
		
		double diffX = start.getX() - end.getX();
		double diffY = start.getY() - end.getY();
		double diffZ = start.getZ() - end.getZ();
		double dist = Math.sqrt((diffX * diffX + diffY * diffY + diffZ * diffZ));
		int numberOfParticles = (int) (dist * 10);
		double ox = start.getX();
		double oy = start.getY();
		double oz = start.getZ();
		int freq = NumUtil.randInt(-r, r);
		int mag = NumUtil.randInt(-r, r);
		int freq1 = NumUtil.randInt(-r, r);
		int mag1 = NumUtil.randInt(-r, r);
		int freq2 = NumUtil.randInt(-r, r);
		int mag2 = NumUtil.randInt(-r, r);
		new BukkitRunnable() {
			int ticks = 0;
			int dura = 0;
			boolean done = false;

			int i = 0;

			@Override
			public void run() {
				if (done)
					return;
				ticks++;
				dura++;


				for(int i = (int) (dura*(numberOfParticles/duration)/10*lifeTime); i < dura*numberOfParticles/duration; i++) {
//					i = (int) (numberOfParticles * dura/duration);

					Location l = start;
					double x = ox - diffX / numberOfParticles * i + Math.sin(freq*(Math.PI * i / numberOfParticles))*mag;
					double y = oy - diffY / numberOfParticles * i + Math.sin(freq1*(Math.PI * i / numberOfParticles))*mag1;
					double z = oz - diffZ / numberOfParticles * i + Math.cos(freq2*(Math.PI * i / numberOfParticles))*mag2;
					l.setX(x);
					l.setY(y);
					l.setZ(z);
					world.spawnParticle(particle, l, 1, 0, 0, 0, 0, null);
				}

				
				if (dura >= duration) {
					cancel();
				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void createTrailCurveColor(int r, int lifeTime, double red, double green, double blue) {
		World world = start.getWorld();
		
		double diffX = start.getX() - end.getX();
		double diffY = start.getY() - end.getY();
		double diffZ = start.getZ() - end.getZ();
		double dist = Math.sqrt((diffX * diffX + diffY * diffY + diffZ * diffZ));
		int numberOfParticles = (int) (dist * 10);
		double ox = start.getX();
		double oy = start.getY();
		double oz = start.getZ();
		int freq = NumUtil.randInt(-r, r);
		int mag = NumUtil.randInt(-r, r);
		int freq1 = NumUtil.randInt(-r, r);
		int mag1 = NumUtil.randInt(-r, r);
		int freq2 = NumUtil.randInt(-r, r);
		int mag2 = NumUtil.randInt(-r, r);
		new BukkitRunnable() {
			int ticks = 0;
			int dura = 0;
			boolean done = false;

			int i = 0;

			@Override
			public void run() {
				if (done)
					return;
				ticks++;
				dura++;


				for(int i = (int) (dura*(numberOfParticles/duration)/10*lifeTime); i < dura*numberOfParticles/duration; i++) {
//					i = (int) (numberOfParticles * dura/duration);

					Location l = start;
					double x = ox - diffX / numberOfParticles * i + Math.sin(freq*(Math.PI * i / numberOfParticles))*mag;
					double y = oy - diffY / numberOfParticles * i + Math.sin(freq1*(Math.PI * i / numberOfParticles))*mag1;
					double z = oz - diffZ / numberOfParticles * i + Math.cos(freq2*(Math.PI * i / numberOfParticles))*mag2;
					l.setX(x);
					l.setY(y);
					l.setZ(z);
					world.spawnParticle(particle, l, 0, red, green, blue, 1);
				}

				
				if (dura >= duration) {
					cancel();
				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void spawnCurve() {
		World world = start.getWorld();
//		double tx = (end.getX() - start.getX()) /  end.getX();
//		double ty = (end.getY() - start.getY()) /  end.getY();
//		double tz = (end.getZ() - start.getZ()) /  end.getZ();
//
//		player.sendMessage("the x is " + tx + " ticks");
//		player.sendMessage("the y is " + ty + " ticks");
//		player.sendMessage("the z is " + tz + " ticks");

		double diffX = start.getX() - end.getX();
		double diffY = start.getY() - end.getY();
		double diffZ = start.getZ() - end.getZ();
		double dist = Math.sqrt((diffX * diffX + diffY * diffY + diffZ * diffZ));
		int numberOfParticles = (int) (dist * 10);

//		player.sendMessage("the distance is " + dist + " ticks");
		double ox = start.getX();
		double oy = start.getY();
		double oz = start.getZ();
		int freq = NumUtil.randInt(1, 8);
		int mag = NumUtil.randInt(1, 8);
		int freq1 = NumUtil.randInt(1, 8);
		int mag1 = NumUtil.randInt(1, 8);
		int freq2 = NumUtil.randInt(1, 8);
		int mag2 = NumUtil.randInt(1, 8);
		new BukkitRunnable() {
			int ticks = 0;
			int dura = 0;
			boolean done = false;

			@Override
			public void run() {
				if (done)
					return;
				ticks++;
				dura++;

//				world.spawnParticle(particle, end, 1, 0, 0, 0, 0, null);
				if (ticks > 2) {

					for (int i = 0; i < numberOfParticles; i++) {
						Location l = start;

						
						double x = ox - diffX / numberOfParticles * i + Math.sin(freq*(Math.PI * i / numberOfParticles))*mag;
						double y = oy - diffY / numberOfParticles * i + Math.sin(freq1*(Math.PI * i / numberOfParticles))*mag1;
						double z = oz - diffZ / numberOfParticles * i + Math.cos(freq2*(Math.PI * i / numberOfParticles))*mag2;
						l.setX(x);
						l.setY(y);
						l.setZ(z);
						world.spawnParticle(particle, l, 1, 0, 0, 0, 0, null);
					}
					ticks = 0;
					if (dura >= duration) {

						cancel();
					}

				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void spawnCurveDelay() {
		World world = start.getWorld();

		double diffX = start.getX() - end.getX();
		double diffY = start.getY() - end.getY();
		double diffZ = start.getZ() - end.getZ();
		double dist = Math.sqrt((diffX * diffX + diffY * diffY + diffZ * diffZ));
		int numberOfParticles = (int) (dist * 10);

		double ox = start.getX();
		double oy = start.getY();
		double oz = start.getZ();
		int freq = NumUtil.randInt(1, 4);
		int mag = NumUtil.randInt(1, 4);
		int freq1 = NumUtil.randInt(1, 4);
		int mag1 = NumUtil.randInt(1, 4);
		int freq2 = NumUtil.randInt(1, 4);
		int mag2 = NumUtil.randInt(1, 4);
		
		new BukkitRunnable() {
			int ticks = 0;
			int dura = 0;
			boolean done = false;
			
			@Override
			public void run() {
				if (done)
					return;
				ticks++;
				dura++;

//				world.spawnParticle(particle, end, 1, 0, 0, 0, 0, null);
					for (int i = 0; i < dura*numberOfParticles/duration; i++) {
						Location l = start;

						
						double x = ox - diffX / numberOfParticles * i + Math.sin(freq*(Math.PI * i / numberOfParticles))*mag;
						double y = oy - diffY / numberOfParticles * i + Math.sin(freq1*(Math.PI * i / numberOfParticles))*mag1;
						double z = oz - diffZ / numberOfParticles * i + Math.cos(freq2*(Math.PI * i / numberOfParticles))*mag2;
						l.setX(x);
						l.setY(y);
						l.setZ(z);
						world.spawnParticle(particle, l, 1, 0, 0, 0, 0, null);
					}
					ticks = 0;
					if (dura >= duration) {
						cancel();
					}
			}

		}.runTaskTimer(plugin, 0, 1);
		
	}



	public void spawnLine(int c, double offx, double offy, double offz, double extra, DustOptions dustOptions) {
		World world = start.getWorld();
		
		double diffX = start.getX() - end.getX();
		double diffY = start.getY() - end.getY();
		double diffZ = start.getZ() - end.getZ();
		double dist = Math.sqrt((diffX * diffX + diffY * diffY + diffZ * diffZ));
		int numberOfParticles = (int) (dist * 20);

		double ox = start.getX();
		double oy = start.getY();
		double oz = start.getZ();
		
		new BukkitRunnable() {
			int ticks = 0;
			int dura = 0;
			boolean done = false;

			@Override
			public void run() {
				if (done)
					return;
				ticks++;
				dura++;

				if (ticks > 2) {
					
					for (int i = 0; i < numberOfParticles; i++) {
						Location l = start;
						double x = ox - diffX / numberOfParticles * i;
						double y = oy - diffY / numberOfParticles * i;
						double z = oz - diffZ / numberOfParticles * i;

						l.setX(x);
						l.setY(y);
						l.setZ(z);
						//
						int c = 3; // <- count
						//                  particle, l, c,ox,oy,oz, extra, data
						world.spawnParticle(particle, l, 1, offx, offy, offz, 0, dustOptions);
					}
					ticks = 0;
					if (dura >= duration) {
						cancel();
					}

				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}
	
	public void spawnLine() {
		World world = start.getWorld();

		double diffX = start.getX() - end.getX();
		double diffY = start.getY() - end.getY();
		double diffZ = start.getZ() - end.getZ();
		double dist = Math.sqrt((diffX * diffX + diffY * diffY + diffZ * diffZ));
		int numberOfParticles = (int) (dist * 20);

		double ox = start.getX();
		double oy = start.getY();
		double oz = start.getZ();
		
		new BukkitRunnable() {
			int ticks = 0;
			int dura = 0;
			boolean done = false;

			@Override
			public void run() {
				if (done)
					return;
				ticks++;
				dura++;

				if (ticks > 2) {
					
					for (int i = 0; i < numberOfParticles; i++) {
						Location l = start;
						double x = ox - diffX / numberOfParticles * i;
						double y = oy - diffY / numberOfParticles * i;
						double z = oz - diffZ / numberOfParticles * i;

						l.setX(x);
						l.setY(y);
						l.setZ(z);
						//
						int c = 3; // <- count
						//                  particle, l, c,ox,oy,oz, extra, data
						world.spawnParticle(particle, l, 1, 0, 0, 0, 0, null);
					}
					ticks = 0;
					if (dura >= duration) {
						cancel();
					}

				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}
	
	public double roundTo(double a) {
		double roundOff = (double) Math.round(a * 100) / 100;
		return roundOff;
	}

}
