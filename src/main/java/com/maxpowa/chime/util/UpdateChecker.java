package com.maxpowa.chime.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxpowa.chime.Chime;

public class UpdateChecker {

	/*
	{
	  "title": "Chime",
	  "game": "Minecraft",
	  "category": "Cosmetic",
	  "url": "http:\/\/www.curse.com\/mc-mods\/minecraft\/223265-chime",
	  "thumbnail": "http:\/\/media-curse.cursecdn.com\/attachments\/124\/227\/8547408bf1bc3206e02abd7f621dbeb2.png",
	  "authors": [
	    "Maxpowa"
	  ],
	  "downloads": {
	    "monthly": 1,
	    "total": 1
	  },
	  "favorites": 0,
	  "likes": 0,
	  "updated_at": "2014-08-11T18:10:58+0000",
	  "created_at": "2014-08-11T16:19:48+0000",
	  "project_url": "http:\/\/www.curseforge.com\/projects\/223265\/",
	  "release_type": "Beta",
	  "license": "Custom License",
	  "download": {
	    "id": 2211322,
	    "url": "http:\/\/curse.com\/mc-mods\/minecraft\/223265-chime\/2211322",
	    "name": "Chime-1.7.10-0.1.178.jar",
	    "type": "beta",
	    "version": "1.7.10",
	    "downloads": 0,
	    "created_at": "2014-08-11T16:22:48+0000"
	  },
	  "versions": {
	    "1.7.10": [
	      {
	        "id": 2211322,
	        "url": "http:\/\/curse.com\/mc-mods\/minecraft\/223265-chime\/2211322",
	        "name": "Chime-1.7.10-0.1.178.jar",
	        "type": "beta",
	        "version": "1.7.10",
	        "downloads": 0,
	        "created_at": "2014-08-11T16:22:48+0000"
	      }
	    ]
	  },
	  "files": {
	    "2211322": {
	      "id": 2211322,
	      "url": "http:\/\/curse.com\/mc-mods\/minecraft\/223265-chime\/2211322",
	      "name": "Chime-1.7.10-0.1.178.jar",
	      "type": "beta",
	      "version": "1.7.10",
	      "downloads": 0,
	      "created_at": "2014-08-11T16:22:48+0000"
	    }
	  }
	}
	 */

	public static class GuiNotification extends Gui {
		private Minecraft mc;
		
	    private static final ResourceLocation notificationParts = new ResourceLocation("chime:textures/gui/updateNotification.png");
	    
	    String notification = "";
	    int notificationwidth = 0;
		
	    public GuiNotification(Minecraft par1Minecraft)
	    {
	        this.mc = par1Minecraft;
	    }
	    
	    public void drawScreen(int width, int height, int mouseX, int mouseY, float partialTicks) {
	    	if (!UpdateChecker.dismissed && UpdateChecker.latest != null) {
	    		
	    		if (notification.isEmpty()) {
	    			notification = formatNotification();
	    			notificationwidth = mc.fontRenderer.getStringWidth(notification);
	    		}
	    		
	    		GL11.glPushMatrix();
	    		
	    		Minecraft.getMinecraft().getTextureManager().bindTexture(notificationParts);
	    		//Draws a textured rectangle at z = 0.             x,  y,  u,  v,             width, height, textureWidth, textureHeight
	    		Gui.func_146110_a((width-notification.length())/2-10,  0,  0,  0,                10,     33,           21,            67);
	    		Gui.func_146110_a(   (width-notification.length())/2,  0,  0, 34, notificationwidth,     33,           21,            67);
	    		Gui.func_146110_a(   (width+notification.length())/2,  0, 11,  0,                10,     33,           21,            67);
	    		
	    		
	    		GL11.glPopMatrix();
	    	}
	    }
	    
	    public String formatNotification() {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append("A new Chime version is available! (")
	    		.append(latest.getVersion())
	    		.append(")");
	    	return sb.toString();
	    }
	}

	public static ChimeVersion latest = null;
	public static boolean dismissed = false;
	static ObjectMapper mapper = new ObjectMapper();
	public static GuiNotification notificationOverlay;

	public static void checkUpdate() {
		try {
			URLConnection versionData = new URL("http://widget.mcf.li/mc-mods/minecraft/223265-chime.json").openConnection();
			JsonNode object = mapper.readTree(new BufferedReader(new InputStreamReader(versionData.getInputStream())));
			if (object.get("versions").has(Chime.MC_VERSION)) {
				latest = mapper.treeToValue(object.get("versions").get(Chime.MC_VERSION).get(0), ChimeVersion.class);
			} else {
				Utils.log.info("Update checker was unable to find a release for this Minecraft version, showing latest release.");
				latest = mapper.treeToValue(object.get("download"), ChimeVersion.class);
			}
		} catch (IOException e) {
			Utils.log.error("Update checker failed to check for new version.",e);
		}
	}

	public static class ChimeVersion {
		Long id;
		String url;
		String name;
		String type;
		String version;
		Long downloads;
		String created_at;
		
		public Long getId() {
			return id;
		}
		public String getVersion() {
			return name.replace("Chime-", "").replace(".jar", "");
		}
		public String getMCVersion() {
			return version;
		}
		public String getType() {
			return type;
		}
		public String getSponsoredURL() {
			return url;
		}
		public String getDownloadURL() {
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append("http://addons.curse.cursecdn.com/files/")
				.append(new StringBuilder(id.toString()).insert(id.toString().length()-3, '/'))
				.append('/')
				.append(name);
			return urlBuilder.toString();
		}
		public Date getCreatedAtUTC() throws ParseException {
			DateFormat dateTime = new SimpleDateFormat("yyyy-mm-ddTHH:mm:ss");
			return dateTime.parse(created_at.split("[+-]", 2)[0]);
		}
	}

}
