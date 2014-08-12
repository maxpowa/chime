package com.maxpowa.chime.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import org.apache.logging.log4j.Logger;

import com.maxpowa.chime.Chime;

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
	
	/**
	 * Will find the absolute path to Chime.class
	 * 
	 * @throws IllegalStateException If the specified class was loaded from a directory or in some other way (such as via HTTP, from a database, or some
	 *                               other custom classloading device).
	 */
	public static String findPathJar() throws IllegalStateException {
		return Utils.findPathJar(null);
	}
	
	/**
	 * If the provided class has been loaded from a jar file that is on the local file system, will find the absolute path to that jar file.
	 * 
	 * @param context The jar file that contained the class file that represents this class will be found. Specify {@code null} to let {@link Chime}
	 *                find its own jar.
	 * @throws IllegalStateException If the specified class was loaded from a directory or in some other way (such as via HTTP, from a database, or some
	 *                               other custom classloading device).
	 */
	public static String findPathJar(Class<?> context) throws IllegalStateException {
	    if (context == null) context = Chime.class;
	    String rawName = context.getName();
	    String classFileName;
	    /* rawName is something like package.name.ContainingClass$ClassName. We need to turn this into ContainingClass$ClassName.class. */ {
	        int idx = rawName.lastIndexOf('.');
	        classFileName = (idx == -1 ? rawName : rawName.substring(idx+1)) + ".class";
	    }

	    String uri = context.getResource(classFileName).toString();
	    if (uri.startsWith("file:")) throw new IllegalStateException("This class has been loaded from a directory and not from a jar file.");
	    if (!uri.startsWith("jar:file:")) {
	        int idx = uri.indexOf(':');
	        String protocol = idx == -1 ? "(unknown)" : uri.substring(0, idx);
	        throw new IllegalStateException("This class has been loaded remotely via the " + protocol +
	                " protocol. Only loading from a jar on the local file system is supported.");
	    }

	    int idx = uri.indexOf('!');
	    //As far as I know, the if statement below can't ever trigger, so it's more of a sanity check thing.
	    if (idx == -1) throw new IllegalStateException("You appear to have loaded this class from a local jar file, but I can't make sense of the URL!");

	    try {
	        String fileName = URLDecoder.decode(uri.substring("jar:file:".length(), idx), Charset.defaultCharset().name());
	        return new File(fileName).getAbsolutePath();
	    } catch (UnsupportedEncodingException e) {
	        throw new InternalError("Default charset doesn't exist. Your VM is borked.");
	    }
	}

}
