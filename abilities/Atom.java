package me.Macpaper.GodSword.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;

import me.Macpaper.GodSword.Main;

public class Atom {
	private Main plugin;
	private Location location;
	private Particle particle;
	
	private double velocityX = 0;
	private double velocityY = 0;
	private double velocityZ = 0;
	private double accelerationX = 0;
	private double accelerationY = 0;
	private double accelerationZ = 0;
	
	public Atom(Main plugin, Location location, Particle particle) {
		this.plugin = plugin;
		this.location = location;
		this.particle = particle;
	}
	
	public void setVelocityX(double vX) {
		this.velocityX = vX;
	}
	
	public void setVelocityY(double vY) {
		this.velocityY = vY;
	}
	
	public void setVelocityZ(double vZ) {
		this.velocityZ = vZ;
	}

	public double getVelocityX() {
		return velocityX;
	}
	public double getVelocityY() {
		return velocityY;
	}
	public double getVelocityZ() {
		return velocityZ;
	}


	public double getAccelerationX() {
		return accelerationX;
	}


	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}


	public double getAccelerationY() {
		return accelerationY;
	}


	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}


	public double getAccelerationZ() {
		return accelerationZ;
	}


	public void setAccelerationZ(double accelerationZ) {
		this.accelerationZ = accelerationZ;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Particle getParticle() {
		return particle;
	}

	public void setParticle(Particle particle) {
		this.particle = particle;
	}
}
