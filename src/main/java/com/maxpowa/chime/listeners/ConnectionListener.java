package com.maxpowa.chime.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.data.ServerInfo;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class ConnectionListener {
    
    // Used in both integrated and remote server connections
    @SubscribeEvent
    public void clientLoggedIn(ClientConnectedToServerEvent event) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
        	String name = Minecraft.getMinecraft().getIntegratedServer().getWorldName();
            Chime.myUser.setCurrentServer(new ServerInfo(name, "", "", ServerInfo.Type.SP));
        } else {
            ServerData sd = Utils.getServerData();
            ServerInfo si = new ServerInfo(sd.serverName, sd.serverIP, sd.gameVersion, ServerInfo.Type.MP);
            if (Utils.isPrivateIPAddress(sd.serverIP)) 
            	si = new ServerInfo(sd.serverName, "", "", ServerInfo.Type.LAN);
            Chime.myUser.setCurrentServer(si);
        }
        this.syncServerInfo();
    }
    
    @SubscribeEvent
    public void clientLoggedOut(ClientDisconnectionFromServerEvent event) {
        Chime.myUser.setCurrentServer(new ServerInfo());
        this.syncServerInfo();
    }
    
    public void syncServerInfo() {
    	//Utils.log.info("Updating current servers");
    	Chime.me.child("currentServer").setValue(Chime.myUser.getCurrentServer());
    }
}
