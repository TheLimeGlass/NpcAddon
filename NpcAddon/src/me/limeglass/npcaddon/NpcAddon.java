package me.limeglass.npcaddon;

import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import me.limeglass.npcaddon.util.PacketListener;

public class NpcAddon extends JavaPlugin {
	
	private static NpcAddon instance;
	
	public void onEnable(){
		try {
			instance = this;
			Skript.registerAddon(this).loadClasses("me.limeglass.npcaddon", "elements");
			new Metrics(this);
			Register.events();
			Register.types();
			//YamlSaver.loadNpcs();
			getServer().getPluginManager().registerEvents(new PacketListener(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.getConsoleSender().sendMessage("[NpcAddon] has been enabled!");
	}
	
	public void onDisable(){
		//YamlSaver.saveNPCs();
	}
	
	public static NpcAddon getInstance() {
		return instance;
	}
}