package me.limeglass.npcaddon.util.npcs;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NpcInteractEvent extends Event implements Cancellable {
	
	public Player player;
	public Npc npc;
	public boolean interact;
	private boolean isCancelled = false;
	private static final HandlerList handlers = new HandlerList();

	public NpcInteractEvent(Player player, Npc npc, boolean interact) {
		this.player = player;
		this.npc = npc;
		this.interact = interact;
	}
	
	public Npc getNpc() {
		return npc;
	}
	
	public Boolean isRightClick() {
		return interact;
	}
	
	public Player getPlayer() {
		return player;
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