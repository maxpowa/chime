package com.maxpowa.chime;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import org.json.JSONException;
import org.json.JSONObject;

import com.firebase.client.Firebase;
import com.firebase.security.token.TokenGenerator;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="Chime", name="Chime", version="v@VERSION@")
public class Chime {

	protected static Firebase users = new Firebase("https://sweltering-fire-4536.firebaseio.com/users/");
	protected static Firebase public_requests = new Firebase("https://sweltering-fire-4536.firebaseio.com/public_requests/");
	protected static Firebase me = null;
	
	protected static User myUser = null;
	private static GameProfile myProfile = null;
	private boolean isDebug = true;

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
    
    private long lastTick = System.currentTimeMillis();
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
    	if (event.side == Side.CLIENT && event.phase == Phase.START) {
    		if (lastTick+15000 < System.currentTimeMillis() && myUser != null) {
    			me.setValue(myUser);
    			Utils.log.info("Updating server-side cached user");
    			lastTick = System.currentTimeMillis();
    		}
    	}
    }
    
    protected static Session getSession() {
    	return Minecraft.getMinecraft().getSession();
    }
	
    private void authenticateClient() {
    	
    	Utils.log.info("Authenticating client...");
    	
    	if (myProfile.getId() == null) {
    		Utils.log.info("Authentication failed!");
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
		
    	me.auth(token, new Authenticator());
    	
    	Chime.me.addListenerForSingleValueEvent(new Initializer());
    }
}
 