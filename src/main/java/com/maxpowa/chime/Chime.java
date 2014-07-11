package com.maxpowa.chime;

import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.security.token.TokenGenerator;
import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;
import com.shaded.fasterxml.jackson.core.JsonProcessingException;
import com.shaded.fasterxml.jackson.databind.ObjectMapper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="Chime-In", name="Chime", version="v@VERSION@")
public class Chime {

	private static Firebase users = new Firebase("https://sweltering-fire-4536.firebaseio.com/users/");
	private static Firebase public_requests = new Firebase("https://sweltering-fire-4536.firebaseio.com/public_requests/");
	private static Firebase me = null;
	
	private static Profile myProfile = null;
	
	private static Logger log = null;
	
	private boolean authSent = false;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	FMLCommonHandler.instance().bus().register(this);
    	log = event.getModLog();
    }
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
    	if (event.side == Side.CLIENT && event.phase == Phase.START) {
    		if (!authSent && (getSession().getToken().length() > 5 || getSession().getToken().equalsIgnoreCase("FML")))
    			authenticateClient();
    	}
    }
    
    private static Session getSession() {
    	return Minecraft.getMinecraft().getSession();
    }
	
    private void authenticateClient() {
    	
    	log.info("Authenticating client...");
    	
    	HttpProfileRepository profileRepo = new HttpProfileRepository();
        Profile[] matchingProfiles = profileRepo.findProfilesByCriteria(new ProfileCriteria(getSession().getUsername(), "minecraft"));
        myProfile = new Profile();
        if (matchingProfiles.length > 0) {
            myProfile.setId(matchingProfiles[0].getId());
            myProfile.setName(getSession().getUsername());
        } else {
        	log.warn("Authentication failed, check your network connection or buy the damn game.");
        	return;
        }
    	
		JSONObject auth = new JSONObject();
		try {
		    auth.put("id", myProfile.getId());
		    auth.put("token", getSession().getToken());
		} catch (JSONException e) {
		    
		}   
		
		log.info("Using "+auth.toString());

		TokenGenerator tokenGenerator = new TokenGenerator("UON7iIE2AJT2eJcAXXWhRKUiXlwUe1Pu5LzkGYta");
		String token = tokenGenerator.createToken(auth);
        
        me = users.child(myProfile.getId());
		
    	me.auth(token, new Firebase.AuthListener() {

    	    @Override
    	    public void onAuthError(FirebaseError error) {
    	        log.error("Authentication Failed! " + error.getMessage());
    	        authSent = false;
    	    }

    	    @Override
    	    public void onAuthSuccess(Object authData) {
    	        log.info("Authentication Succeeded!");

    	        me.addListenerForSingleValueEvent(new ValueEventListener() {

					@Override
					public void onCancelled(FirebaseError arg0) {}

					@Override
					public void onDataChange(DataSnapshot data) {
						User user = (User) data.getValue();
						log.info("Got user data");
						if (user == null) {
							log.info("Data was null, creating new user at "+me.getPath());
							me.setValue(new User());
							ObjectMapper mapper = new ObjectMapper();
							try {
								log.info(mapper.writeValueAsString(new User()));
							} catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						me.push();
					}
    	        });
    	    }

    	    @Override
    	    public void onAuthRevoked(FirebaseError error) {
    	        log.error("Authentication status was cancelled! " + error.getMessage());
    	        authSent = false;
    	    }

    	});
    	
    	authSent = true;
    }
}
 