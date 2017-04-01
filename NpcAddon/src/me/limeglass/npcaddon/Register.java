package me.limeglass.npcaddon;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import me.limeglass.npcaddon.util.npcs.Npc;
import me.limeglass.npcaddon.util.npcs.NpcDamageEvent;
import me.limeglass.npcaddon.util.npcs.NpcInteractEvent;

public class Register extends JavaPlugin {
	
	public static void events(){
		registerEvent(NpcInteractEvent.class, "npc interact");
		EventValues.registerEventValue(NpcInteractEvent.class, Player.class, new Getter<Player, NpcInteractEvent>() {
			@Override
			public Player get(NpcInteractEvent e) {
				return e.getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(NpcInteractEvent.class, Npc.class, new Getter<Npc, NpcInteractEvent>() {
			@Override
			public Npc get(NpcInteractEvent e) {
				return e.getNpc();
			}
		}, 0);
		registerEvent(NpcInteractEvent.class, "npc damage");
		EventValues.registerEventValue(NpcDamageEvent.class, Player.class, new Getter<Player, NpcDamageEvent>() {
			@Override
			public Player get(NpcDamageEvent e) {
				return e.getAttacker();
			}
		}, 0);
		EventValues.registerEventValue(NpcDamageEvent.class, Npc.class, new Getter<Npc, NpcDamageEvent>() {
			@Override
			public Npc get(NpcDamageEvent e) {
				return e.getNpc();
			}
		}, 0);
	}
	
	public static void types(){
		String codeName = "npc";
		if (Classes.getClassInfoNoError(codeName) != null) {
			codeName = "npctype";
		}
		Classes.registerClass(new ClassInfo<Npc>(Npc.class, codeName)
			.user("npc(s)?")
			.name("Npc")
			.description("An type that represents the Npc class within Skript.")
			.defaultExpression(new EventValueExpression<Npc>(Npc.class))
			.parser(new Parser<Npc>() {
				@Nullable
				public Npc parse(final String s, final ParseContext context) {
					return null;
				}
				
				@Override
				public String toVariableNameString(final Npc npc) {
					return npc.toString();
				}
				
				@Override
				public String getVariableNamePattern() {
					return ".+";
				}
				
				@Override
				public String toString(final Npc npc, final int flags) {
					return npc.toString();
				}
			}));
	}
	
	@SuppressWarnings("unchecked")
	public static void registerEvent(@SuppressWarnings("rawtypes") Class clazz, String syntax) {
		Skript.registerEvent(syntax, SimpleEvent.class, clazz, "[skellett] " + syntax);
	}
}