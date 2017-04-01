package me.limeglass.npcaddon.util.npcs;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

import org.bukkit.Location;

public class Npc {
	
	Object entity;
	UUID uuid;
	String name, skin;
	int ID;
	GameProfile profile;
	Location location;
	
	public Npc(Object entity, UUID uuid, String name, String skin, int ID, GameProfile profile, Location location) {
		this.entity = entity;
		this.uuid = uuid;
		this.name = name;
		this.skin = skin;
		this.ID = ID;
		this.profile = profile;
		this.location = location;
	}
	
	public Object getEntity() {
		return entity;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSkinName() {
		return skin;
	}
	
	public int getID() {
		return ID;
	}
	
	public GameProfile getProfile() {
		return profile;
	}
	
	public Location getLocation() {
		return location;
	}
}