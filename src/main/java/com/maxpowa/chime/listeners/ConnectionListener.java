package com.maxpowa.chime.listeners;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.util.ServerInfo;
import com.maxpowa.chime.util.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class ConnectionListener {
    
    // Used in both integrated and remote server connections
    @SubscribeEvent
    public void clientLoggedIn(PlayerLoggedInEvent event) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
        	String name = Minecraft.getMinecraft().getIntegratedServer().getWorldName();
            Chime.myUser.setCurrentServer(new ServerInfo(name, "", "", ServerInfo.Type.SP, false));
        } else {
            ServerData sd = Utils.getServerData();
            ServerInfo si = new ServerInfo(sd.serverName, sd.serverIP, sd.gameVersion, ServerInfo.Type.MP, sd.func_152585_d());
            Chime.myUser.setCurrentServer(si);
        }
        this.syncServerInfo();
    }
    
    @SubscribeEvent
    public void clientLoggedOut(PlayerLoggedOutEvent event) {
        Chime.myUser.setCurrentServer(new ServerInfo());
    }
    
    public void syncServerInfo() {
    	//Utils.log.info("Updating current servers");
    	Chime.me.child("currentServer").setValue(Chime.myUser.getCurrentServer());
    }
}
