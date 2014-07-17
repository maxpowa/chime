package com.maxpowa.chime.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import org.apache.logging.log4j.Logger;

public class Utils {

	public static Logger log = null;

	public static ServerData getServerData() {
		return Minecraft.getMinecraft().func_147104_D();
	}

	public static String millisToTimeSpan(long millis) {
		if(millis < 60 * 1000) {
			long unit = roundUp(millis / 1000); 
			return unit + (unit == 1 ? " second" : " seconds");
		}
		else if(millis < 60 * 1000 * 60) {
			long unit = roundUp(millis / 60000); 
			return unit + (unit == 1 ? " minute" : " minutes");
		}
		else if(millis < 60 * 1000 * 60 * 24) {
			long unit = roundUp(millis / (60000 * 60)); 
			return unit + (unit == 1 ? " hour" : " hours");
		}
		else {
			long unit = roundUp(millis / (60000 * 60 * 24)); 
			return unit + (unit == 1 ? " day" : " days");
		}
	}

	private static long roundUp(double d) {
		return Math.round(d * 10.0) / 10;
	} 

	public static boolean isPrivateIPAddress(String ipAddress) {
		InetAddress ia = null;
		try {
			InetAddress ad = InetAddress.getByName(ipAddress);
			byte[] ip = ad.getAddress();
			ia = InetAddress.getByAddress(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ia.isSiteLocalAddress();

	}

}
