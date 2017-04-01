package me.limeglass.npcaddon.elements.effects;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.npcaddon.util.npcs.NpcManager;

public class EffSpawnNpc extends Effect {
	
	static {
		Skript.registerEffect(EffSpawnNpc.class, "(create|spawn|register) [a[n] npc [with] (name[d]|skin [owner]) %string% [and] [[with] display[ ]name %-string%] at %location%");
	}
	
	private Expression<Location> location;
	private Expression<String> name, display;
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
		name = (Expression<String>) e[0];
		display = (Expression<String>) e[1];
		location = (Expression<Location>) e[2];
		return true;
	}
	
	@Override
	public String toString(@Nullable Event paramEvent, boolean paramBoolean) {
		return "(create|spawn|register) [a[n] npc [with] (name[d]|skin [owner]) %string% [and] [[with] display[ ]name %-string%] at %location%";
	}
	
	@Override
	protected void execute(Event e) {
		if (name != null && location != null) {
			if (display != null) {
				NpcManager.registerNpc(name.getSingle(e), display.getSingle(e), location.getSingle(e));
			} else {
				NpcManager.registerNpc(name.getSingle(e), location.getSingle(e));
			}
		}
	}
}