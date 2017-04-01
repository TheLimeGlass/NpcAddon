package me.limeglass.npcaddon.elements.effects;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.npcaddon.util.npcs.Npc;
import me.limeglass.npcaddon.util.npcs.NpcManager;

public class EffDeleteNpc extends Effect {
	
	static {
		Skript.registerEffect(EffDeleteNpc.class, "(delete|remove|clear|kill|unregister) npc %npc%");
	}
	
	private Expression<Npc> npc;
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
		npc = (Expression<Npc>) e[0];
		return true;
	}
	
	@Override
	public String toString(@Nullable Event paramEvent, boolean paramBoolean) {
		return "(delete|remove|clear|kill|unregister) npc %npc%";
	}
	
	@Override
	protected void execute(Event e) {
		if (npc != null) {
			NpcManager.unregister(npc.getSingle(e));
		}
	}
}