package com.maxpowa.chime.util;

import com.shaded.fasterxml.jackson.annotation.JsonIgnore;

import net.minecraft.client.multiplayer.ServerData;

public class ServerInfo {

	private String serverName = "";   
	private String serverIP = "";     
	private String serverVersion = "";
	private boolean hidden = false;
	private Type type = Type.NONE;
	
	public ServerInfo() {
	}
	
	public ServerInfo(String name, String ip, String version, Type type, boolean hidden) {
		this.serverName = name;
		this.serverIP = ip;
		this.serverVersion = version;
		this.type = type;
		this.hidden = hidden;
	}

	@JsonIgnore
	public ServerData getServerData() {
		return new ServerData(this.serverName, this.serverIP);
	}
	
	@Override
	@JsonIgnore
	public String toString() {
		String returnStatement = "";
		if (this.type == Type.SP) {
			returnStatement = "singleplayer";
		} else if (this.type == Type.MP) {
			returnStatement = "on "+this.serverName+" ("+this.serverIP+")";
			if (hidden) {
				returnStatement = "on a multiplayer server";
			}
		} else if (this.type == Type.NONE) {
			returnStatement = "Minecraft";
		}
		return returnStatement;
	}

	public String getName() {
		return this.serverName;
	}
	
	public void setName(String name) {
		this.serverName = name;
	}
	
	public String getIP() {
		return this.serverIP;
	}
	
	public void setIP(String ip) {
		this.serverIP = ip;
	}

	public String getVersion() {
		return serverVersion;
	}

	public void setVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public enum Type {
		MP,SP,NONE;
	}
	
}
