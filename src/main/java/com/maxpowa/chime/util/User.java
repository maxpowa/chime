package com.maxpowa.chime.util;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"online"})
public class User {

	@JsonIgnore
	private DynamicTexture skin = null;
	@JsonIgnore
	private ResourceLocation resourceLocation;
	@JsonIgnore
	private BufferedImage bufferedimage;
	@JsonIgnore
	private boolean preparingSkin;
	
	private long lastSeen = 0L;
	private String username = "Steve";
	private String motd = "I'm new here!";
	private HashMap<String, String> blocks = new HashMap<String,String>();
	private HashMap<String, String> friends = new HashMap<String,String>();
	private ServerInfo currentServer = new ServerInfo();
	private String UUID = "";
	
	public String getMotd() {
		return motd;
	}
	public void setMotd(String motd) {
		this.motd = motd;
	}
	public HashMap<String, String> getBlocks() {
		return blocks;
	}
	public void setBlocks(HashMap<String, String> blocks) {
		this.blocks = blocks;
	}
	public HashMap<String, String> getFriends() {
		return friends;
	}
	public void setFriends(HashMap<String, String> friends) {
		this.friends = friends;
	}
	public long getLastSeen() {
		return System.currentTimeMillis();
	}
	public void setLastSeen(long lastSeen) {
		this.lastSeen = lastSeen;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
		this.resourceLocation = new ResourceLocation("skins/" + username);
	}
	public String getUUID() {
		return this.UUID;
	}
	public void setUUID(String uuid) {
		this.UUID = uuid;
	}
	public ServerInfo getCurrentServer() {
		return currentServer;
	}
	public void setCurrentServer(ServerInfo currentServer) {
		this.currentServer = currentServer;
	}
	@JsonIgnore
	public void prepareSkin() {
		this.preparingSkin = true;
		this.skin = (DynamicTexture)Minecraft.getMinecraft().getTextureManager().getTexture(this.resourceLocation);
		try
		{
			URL url = new URL(String.format("http://skins.minecraft.net/MinecraftSkins/%s.png",this.getUsername()));
			this.bufferedimage = ImageIO.read(url);
		}
		catch (Exception exception)
		{
			Utils.log.error("Invalid icon for user " + this.getUsername() + " (" + this.getUUID() + ")", exception);
			this.bufferedimage = null;
			return;
		}

		if (this.skin == null)
		{
			this.skin = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
			Minecraft.getMinecraft().getTextureManager().loadTexture(this.resourceLocation, this.skin);
		}

		bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.skin.getTextureData(), 0, bufferedimage.getWidth());
		this.skin.updateDynamicTexture();
		this.preparingSkin = false;
	}
	@JsonIgnore
	public ResourceLocation getSkin() {
		if (bufferedimage == null && !preparingSkin)
			this.prepareSkin();
		return this.resourceLocation;
	}
	@JsonIgnore
	public int getSkinWidth() {
		return this.bufferedimage == null ? 0 : this.bufferedimage.getWidth();
	}
	@JsonIgnore
	public int getSkinHeight() {
		return this.bufferedimage == null ? 0 : this.bufferedimage.getHeight();
	}
	@JsonIgnore
	public long lastSeen() {
		return this.lastSeen;
	}
	@JsonIgnore
	public boolean isOnline() {
		return ((this.lastSeen + 30000L) >= System.currentTimeMillis());
	}
	
}
