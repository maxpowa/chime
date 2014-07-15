package com.maxpowa.chime.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import org.apache.logging.log4j.Logger;

public class Utils {
	
	public static Logger log = null;
	
	public static ServerData getServerData() {
		return Minecraft.getMinecraft().func_147104_D();
	}
	
}
