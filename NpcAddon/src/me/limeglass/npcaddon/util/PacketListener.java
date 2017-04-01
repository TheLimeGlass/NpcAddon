package me.limeglass.npcaddon.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.netty.channel.*;
import me.limeglass.npcaddon.NpcAddon;
import me.limeglass.npcaddon.util.npcs.Npc;
import me.limeglass.npcaddon.util.npcs.NpcDamageEvent;
import me.limeglass.npcaddon.util.npcs.NpcInteractEvent;
import me.limeglass.npcaddon.util.npcs.NpcManager;
import net.minecraft.server.v1_11_R1.PacketPlayInUseEntity.EnumEntityUseAction;

public class PacketListener implements Listener {
	
	private ArrayList<UUID> firedTwice = new ArrayList<>();
	private HashMap<Player, String> handlers = new HashMap<>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		create(event.getPlayer());
		NpcManager.updateNpcs(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		remove(event.getPlayer());
	}
	
	private void remove(Player player) {
		try {
			Channel channel = ReflectionUtil.getChannel(player);
			channel.eventLoop().submit(() -> {
				channel.pipeline().remove(handlers.get(player));
				return null;
			});
			handlers.remove(player);
		} catch (SecurityException | NoSuchMethodException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private void create(Player player) {
		ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
			@SuppressWarnings("deprecation")
			@Override
			public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
				if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
					EnumEntityUseAction action = (EnumEntityUseAction) getValue(packet, "action");
					final UUID uuid = player.getUniqueId();
					int i = (int) getValue(packet, "a");
					Npc npc = NpcManager.npcs.get(i);
					if (NpcManager.npcs.containsKey(i) && !firedTwice.contains(uuid)) {
						firedTwice.add(uuid);
						NpcInteractEvent event = new NpcInteractEvent(player, npc, !(action == EnumEntityUseAction.ATTACK));
						Bukkit.getServer().getPluginManager().callEvent(event);
						NpcDamageEvent damageEvent = new NpcDamageEvent(player, npc);
						if (action == EnumEntityUseAction.ATTACK) {
							Bukkit.getServer().getPluginManager().callEvent(damageEvent);
						}
						Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(NpcAddon.getInstance(), new Runnable(){
							
							public void run() {
								firedTwice.remove(uuid);
							}
							
						}, 20L);
						if (event.isCancelled() || damageEvent.isCancelled()) {
							return;
						}
					}
				}
				super.channelRead(context, packet);
			}

			@Override
			public void write(ChannelHandlerContext context, Object packet, ChannelPromise channelPromise) throws Exception {
				super.write(context, packet, channelPromise);
			}
		};
		try {
			Channel channel = ReflectionUtil.getChannel(player);
			ChannelPipeline pipeline = channel.pipeline();
			int ID = 0;
			String name = player.getName();
			while (pipeline.get(name) != null) {
				name = player.getName() + ID;
				ID++;
			}
			pipeline.addBefore("packet_handler", name, channelDuplexHandler);
			if (handlers.containsKey(player)) {
				remove(player);
			}
			handlers.put(player, name);
		} catch (SecurityException | NoSuchMethodException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private Object getValue(Object packet, String field) {
		try {
			Field f = packet.getClass().getDeclaredField(field);
			f.setAccessible(true);
			return f.get(packet);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}