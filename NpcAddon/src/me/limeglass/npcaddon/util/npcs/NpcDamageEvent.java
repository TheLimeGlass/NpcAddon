package me.limeglass.npcaddon.util.npcs;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NpcDamageEvent extends Event implements Cancellable {
	
	public Player attacker;
	public Npc npc;
	private boolean isCancelled = false;
	private static final HandlerList handlers = new HandlerList();

	public NpcDamageEvent(Player attacker, Npc npc) {
		this.attacker = attacker;
		this.npc = npc;
	}
	
	public Npc getNpc() {
		return npc;
	}
	
	public Player getAttacker() {
		return attacker;
	}
	
	public boolean isCancelled() {
		return isCancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		isCancelled = cancelled;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}