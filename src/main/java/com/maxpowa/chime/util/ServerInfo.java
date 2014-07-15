package com.maxpowa.chime.util;

import com.shaded.fasterxml.jackson.annotation.JsonIgnore;

import net.minecraft.client.multiplayer.ServerData;

public class ServerInfo {

	private String serverName = "";   
	private String serverIP = "";     
	private String serverVersion = "";
	private boolean multiplayer = false;
	
	public ServerInfo() {
	}
	
	public ServerInfo(String name, String ip, String version, boolean mp) {
		this.serverName = name;
		this.serverIP = ip;
		this.serverVersion = version;
		this.multiplayer = mp;
	}

	@JsonIgnore
	public ServerData getServerData() {
		return new ServerData(this.serverName, this.serverIP);
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

	public boolean isMultiplayer() {
		return multiplayer;
	}

	public void setMultiplayer(boolean multiplayer) {
		this.multiplayer = multiplayer;
	}
	
}
