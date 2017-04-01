package me.limeglass.npcaddon.util.npcs;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.MinecraftServer;
import net.minecraft.server.v1_11_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_11_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_11_R1.PlayerInteractManager;
import net.minecraft.server.v1_11_R1.WorldServer;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import me.limeglass.npcaddon.util.Utils;

public class NpcManager {
	
	public static HashMap<Integer, Npc> npcs = new HashMap<>();
	public static HashMap<String, Integer> skins = new HashMap<>();
	
	protected static EntityPlayer createNpc(String name, Location location, GameProfile profile) {
		return createNpc(name, name, location, profile);
	}
	
	protected static EntityPlayer createNpc(String name, String displayname, Location location, GameProfile profile) {
		MinecraftServer server = ((CraftServer)Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld)location.getWorld()).getHandle();
		EntityPlayer npcEntity = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
		npcEntity.setInvulnerable(true);
		npcEntity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		return npcEntity;
	}

	public static Npc registerNpc(String name, String display, Location location) {
		GameProfile profile;
		UUID uuid = UUID.randomUUID();
		if (!NpcManager.skins.containsKey(name.replace(" ", ""))) {
			profile = new ProfileLoader(uuid.toString(), display, name).loadProfile();
		} else {
			Npc npcTest = NpcManager.npcs.get(NpcManager.skins.get(name.replace(" ", "")));
			profile = npcTest.getProfile();
		}
		EntityPlayer npcEntity = createNpc(name, display, location, profile);
		Npc npc = new Npc(npcEntity, uuid, Utils.cc(display), name, npcEntity.getId(), profile, location);
		NpcManager.npcs.put(npc.getID(), npc);
		NpcManager.skins.put(name.replace(" ", ""), npc.getID());
		update(npc);
		return npc;
	}
	
	public static Npc registerNpc(String name, Location location) {
		GameProfile profile;
		UUID uuid = UUID.randomUUID();
		if (!NpcManager.skins.containsKey(name.replace(" ", ""))) {
			profile = new ProfileLoader(uuid.toString(), name).loadProfile();
		} else {
			Npc npcTest = NpcManager.npcs.get(NpcManager.skins.get(name.replace(" ", "")));
			profile = npcTest.getProfile();
		}
		EntityPlayer npcEntity = createNpc(name, location, profile);
		Npc npc = new Npc(npcEntity, uuid, name, name, npcEntity.getId(), profile, location);
		NpcManager.npcs.put(npc.getID(), npc);
		NpcManager.skins.put(name.replace(" ", ""), npc.getID());
		update(npc);
		return npc;
	}
	
	protected static void update(Npc npc) {
		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				update(npc, player);
			}
		}
	}
	
	protected static void update(Npc npc, Player player) {
		try {
			ReflectionUtil.sendPacket(player, new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, (EntityPlayer) npc.getEntity()));
			ReflectionUtil.sendPacket(player, new PacketPlayOutNamedEntitySpawn((EntityPlayer) npc.getEntity()));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateNpcs() {
		if (npcs.isEmpty()) return;
		for (Npc npc : npcs.values()) {
			update(npc);
		}
	}
	
	public static void updateNpcs(Player player) {
		if (npcs.isEmpty()) return;
		for (Npc npc : npcs.values()) {
			update(npc, player);
		}
	}
	
	public static Collection<Npc> getNpcs() {
		return npcs.isEmpty() ? null : npcs.values();
	}
	
	public static void unregister(Npc npc) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			PacketPlayOutEntityDestroy packetDestroy = new PacketPlayOutEntityDestroy(npc.getID());
			try {
				ReflectionUtil.sendPacket(player, packetDestroy);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		skins.remove(npc.getSkinName().replace(" ", ""));
		npcs.remove(npc);
	}
}