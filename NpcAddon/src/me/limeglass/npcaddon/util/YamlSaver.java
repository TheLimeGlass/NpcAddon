package me.limeglass.npcaddon.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import me.limeglass.npcaddon.NpcAddon;
import me.limeglass.npcaddon.util.npcs.Npc;
import me.limeglass.npcaddon.util.npcs.NpcManager;

public class YamlSaver {
	
	private static File file = new File(NpcAddon.getInstance().getDataFolder(), "Npcs.yml");
	private static YamlConfiguration Yaml = new YamlConfiguration();
	
	public static void saveNPCs() {
		try {
			if (!NpcAddon.getInstance().getDataFolder().exists()) {
				NpcAddon.getInstance().getDataFolder().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}
			Yaml.load(file);
			for (Npc npc : NpcManager.npcs.values()) {
				Yaml.set(npc.getName() + ".Name", npc.getName());
				Yaml.set(npc.getName() + ".Skin", npc.getSkinName());
				Yaml.set(npc.getName() + ".Location.World", npc.getLocation().getWorld().getName());
				Yaml.set(npc.getName() + ".Location.X", npc.getLocation().getX());
				Yaml.set(npc.getName() + ".Location.Y", npc.getLocation().getY());
				Yaml.set(npc.getName() + ".Location.Z", npc.getLocation().getZ());
				Yaml.set(npc.getName() + ".Location.Yaw", npc.getLocation().getYaw());
				Yaml.set(npc.getName() + ".Location.Pitch", npc.getLocation().getPitch());
			}
			Yaml.save(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static void loadNpcs() {
		try {
			if (!NpcAddon.getInstance().getDataFolder().exists()) {
				NpcAddon.getInstance().getDataFolder().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			Yaml.load(file);
			for (String t : Yaml.getKeys(false)) {
				Bukkit.getServer().getLogger().log(Level.INFO, t);
				String name = Yaml.getString(t + ".Name");
				String skin = Yaml.getString(t + ".Skin");
				String world = Yaml.getString(t + ".Location.World");
				Double x = Double.valueOf(Yaml.getDouble(t + ".Location.X"));
				Double y = Double.valueOf(Yaml.getDouble(t + ".Location.Y"));
				Double z = Double.valueOf(Yaml.getDouble(t + ".Location.Z"));
				Float yaw = Float.valueOf((float)Yaml.getDouble(t + ".Location.Yaw"));
				Float pitch = Float.valueOf((float)Yaml.getDouble(t + ".Location.Pitch"));
				Location location = new Location(Bukkit.getServer().getWorld(world), x.doubleValue(), y.doubleValue(), z.doubleValue(), yaw.floatValue(), pitch.floatValue());
				NpcManager.registerNpc(skin, name, location);
				Yaml.set(t, null);
			}
			Yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}