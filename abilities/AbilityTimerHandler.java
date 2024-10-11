package me.Macpaper.GodSword.abilities;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AbilityTimerHandler {

	
	public HashMap<String, HashMap<String, Long>> abilityMap = new HashMap<String, HashMap<String, Long>>();
	
	public boolean isReady(String timerName, Player player, int delay) {
		String playerName = player.getName();
		if (abilityMap.containsKey(playerName)) {
			if (abilityMap.get(playerName).containsKey(timerName)) {
				if (System.currentTimeMillis() - abilityMap.get(playerName).get(timerName) >= delay) {
					setTimer(timerName, playerName);
					return true;
				} 
			} else { 
				setTimer(timerName, playerName);
				return true;
			}
		} else {
			setTimer(timerName, playerName);
			return true;
		}
		double seconds = getCooldown(timerName, playerName, delay) / 1000;
		if (seconds > 1) {
			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "" + timerName + " ON COOLDOWN FOR " + seconds + " SECONDS");
		}
		return false;
	}
	
	public void setTimer(String timerName, String playerName) {
		if (abilityMap.containsKey(playerName)) {
			abilityMap.get(playerName).put(timerName, System.currentTimeMillis());
		} else {
			HashMap<String, Long> createdTimer = new HashMap<String, Long>();
			abilityMap.put(playerName, createdTimer);
			abilityMap.get(playerName).put(timerName, System.currentTimeMillis());
		}
	}
	
	public long getCooldown(String timerName, String playerName, int cdr) {
		if (abilityMap.containsKey(playerName)) {
			if (abilityMap.get(playerName).containsKey(timerName)) {
				return cdr - (System.currentTimeMillis() - abilityMap.get(playerName).get(timerName));
			}
		}
		return 0;
	}
}
