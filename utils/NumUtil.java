package me.Macpaper.GodSword.utils;

import java.util.Random;

import me.Macpaper.GodSword.Main;

public class NumUtil {
	public Main plugin;
	public NumUtil(Main plugin) {
		this.plugin = plugin;
	}

	public static int randChoice(int[] list) {
		int rand = new Random().nextInt(list.length);
		return rand;
	}
	
	public static int randInt(int start, int end) {
		return (int) (Math.ceil(Math.random() * (end - start)) + start);
	}
}
