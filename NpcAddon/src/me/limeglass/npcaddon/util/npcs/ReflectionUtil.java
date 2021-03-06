package me.limeglass.npcaddon.util.npcs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;

public class ReflectionUtil {
	
	private static String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
	}
	
	public static Class<?> getNMSClass(String classString) throws ClassNotFoundException {
		String name = "net.minecraft.server." + getVersion() + classString;
		Class<?> nmsClass = Class.forName(name);
		return nmsClass;
	}
	
	public static Channel getChannel(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object connection = getConnection(player);
		Field connectionField = connection.getClass().getField("networkManager");
		Object networkManager = connectionField.get(connection);
		Field channelField = networkManager.getClass().getField("channel");
		return (Channel) channelField.get(networkManager);
	}
	
	public static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object nmsPlayer = getHandle(player);
		Field connectionField = nmsPlayer.getClass().getField("playerConnection");
		return connectionField.get(nmsPlayer);
	}
	
	public static Object getHandle(Object obj) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (obj != null) {
			Method getHandle = obj.getClass().getMethod("getHandle");
			getHandle.setAccessible(true);
			Object nmsPlayer = getHandle.invoke(obj);
			return nmsPlayer;
		}
		return null;
	}
	
	public static void sendPacket(Player player, Object object) throws NoSuchMethodException {
		try {
			Method method = getConnection(player).getClass().getMethod("sendPacket", getNMSClass("Packet"));
			method.invoke(getConnection(player), object);
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
