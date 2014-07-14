package com.maxpowa.chime;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.input.Mouse;

import com.firebase.client.Firebase;
import com.firebase.security.token.TokenGenerator;
import com.maxpowa.chime.gui.GuiChimeButton;
import com.maxpowa.chime.gui.GuiFriendsList;
import com.maxpowa.chime.gui.GuiNotification;
import com.maxpowa.chime.listeners.Initializer;
import com.maxpowa.chime.util.Authenticator;
import com.maxpowa.chime.util.User;
import com.maxpowa.chime.util.Utils;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="Chime", name="Chime", version="v@VERSION@")
public class Chime {

	public static final String rootURL = "https://sweltering-fire-4536.firebaseio.com";
	public static Firebase users = new Firebase(rootURL+"/users/");
	public static Firebase public_requests = new Firebase(rootURL+"/public_requests/");
	public static Firebase me = null;
	
	public static User myUser = null;
	public static GameProfile myProfile = null;
	public static GuiNotification notificationOverlay;
	private boolean isDebug = true;
	
	private GuiChimeButton button = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	FMLCommonHandler.instance().bus().register(this);
    	Utils.log = event.getModLog();
    	
    	myProfile = getSession().func_148256_e();
    	
    	if (isDebug) {
    		myProfile = new GameProfile(UUID.fromString("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA"),getSession().getPlayerID());
    	}
    	
		authenticateClient();
    }
    
    @SubscribeEvent
    public void RenderTickEvent(RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (button == null) {
        	button = new GuiChimeButton(5, 5);
        }
        if (event.type == Type.RENDER && event.phase == Phase.END && mc.currentScreen != null && !(mc.currentScreen instanceof GuiFriendsList) && Initializer.initialized) {
            int mouseX = Mouse.getX() * mc.currentScreen.width / mc.displayWidth;
            int mouseY = mc.currentScreen.height - Mouse.getY() * mc.currentScreen.height / mc.displayHeight - 1; 
            button.drawButton(mc, mouseX, mouseY);
        }
        if (event.type == TickEvent.Type.RENDER && Chime.notificationOverlay != null) {
            Chime.notificationOverlay.updateNotificationWindow();
        } else if (Chime.notificationOverlay == null) {
        	Chime.notificationOverlay = new GuiNotification(mc);
        }
    }
    
    private long lastTick = System.currentTimeMillis();
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
    	// Updates the server's last seen value to keep your status "online"
    	// If this somehow fails to update the server for 60 seconds, you may appear offline to other users.
    	if (event.side == Side.CLIENT && event.phase == Phase.START) {
    		if (lastTick+15000 < System.currentTimeMillis() && myUser != null) {
    			me.child("lastSeen").setValue(System.currentTimeMillis());
    			lastTick = System.currentTimeMillis();
    		}
    	}
    }

	public static Session getSession() {
    	return Minecraft.getMinecraft().getSession();
    }
	
    private void authenticateClient() {
    	
    	Utils.log.info("Authenticating client...");
    	
    	if (myProfile.getId() == null) {
    		this.deauth();
    		return;
    	}
    	
		JSONObject auth = new JSONObject();
		try {
		    auth.put("id", myProfile.getId().toString());
		    auth.put("token", getSession().getToken());
		} catch (JSONException e) {
		    
		}   
		
		Utils.log.info("Using "+auth.toString());

		TokenGenerator tokenGenerator = new TokenGenerator("UON7iIE2AJT2eJcAXXWhRKUiXlwUe1Pu5LzkGYta");
		String token = tokenGenerator.createToken(auth);
        
        me = users.child(myProfile.getId().toString());
		
    	me.auth(token, new Authenticator(this));
    	
    	Chime.me.addListenerForSingleValueEvent(new Initializer());
    }

	public void deauth() {
		Utils.log.error("Authentication failed, please check your internet connection or buy the game.");
		Utils.log.error("Disabling due to authentication failure. This event will be logged for audit. (REF#"+System.currentTimeMillis()+")");
		FMLCommonHandler.instance().bus().unregister(this);
	}
}
 