package com.maxpowa.chime.gui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import com.maxpowa.chime.gui.buttons.GuiUpdateButton;
import com.maxpowa.chime.util.UpdateChecker;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.relauncher.FMLSecurityManager;

public class GuiUpdateInfo extends GuiScreen implements IChimeGUI {

	/*
	 * Download link: http://addons.cursecdn.com/files/2211%5C456/Chime-1.7.10-0.1.198.jar
	 * 				  http://addons.cursecdn.com/files/idsp/lit/filename
	 * 
	 * 
	 */
	
	private GuiScreen parentScreen;
	String path;
	File downloadLocation = null;
	
    long bytesDownloaded = 0;
    long totalBytes = 0;
	
    private ResourceLocation stone = new ResourceLocation("textures/blocks/stone.png");

	public GuiUpdateInfo(GuiScreen currentScreen) {
		try {
			downloadLocation = new File(Minecraft.getMinecraft().mcDataDir.getCanonicalPath(), "mods");
		} catch (IOException e) {
			Utils.log.info("Unable to locate mods folder. Disabling auto-update capabilities.");
			downloadLocation = null;
		}
		GuiUpdateButton.openingUpdate = false;
	}
	
	public void downloadFromStream(InputStream stream, File location) throws IOException {  
	    BufferedInputStream in = new BufferedInputStream(stream);  
	    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(location));
	    Utils.log.info(location.toString());
	    int curByte;
	    long startns = System.nanoTime();
	    while ((curByte = in.read()) != -1) {  
	        bytesDownloaded++;  
	        out.write(curByte);  
	        if (bytesDownloaded % 100000 == 0)
	        	Utils.log.info("Bytes downloaded: " + bytesDownloaded + " out of " + totalBytes);
	    }
	    out.flush();
	    out.close();
	    in.close();
	    Utils.log.info("Complete! Downloaded "+String.format("%.2f", totalBytes/1000000f)+" megabytes in "+String.format("%.3f", (System.nanoTime() - startns)/1000000000f)+" seconds.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(12, 20, 20, "Test restart client"));
		this.buttonList.add(new GuiButton(13, 20, 40, "Test delete current jar"));
		this.buttonList.add(new GuiButton(14, 20, 60, "Download latest jar"));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 12) {
				restartForUpdate();
			} else if (button.id == 13) {
				deleteCurrentJar();
			} else if (button.id == 14) {
				try {
					HttpURLConnection downconn = (HttpURLConnection) new URL(UpdateChecker.latest.getDownloadURL()).openConnection();
					bytesDownloaded = 0;
					Utils.log.info(downconn.getHeaderFields());
					totalBytes = downconn.getContentLengthLong();
					downloadFromStream(downconn.getInputStream(), new File(downloadLocation, UpdateChecker.latest.getFilename()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawWorldBackground(0);

		mc.fontRenderer.drawStringWithShadow("unimplemented", 10, 10, 0xFFFFFF);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void drawBackground(int z) {
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(stone);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(4210752);
        tessellator.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / f + (float)z));
        tessellator.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / f), (double)((float)this.height / f + (float)z));
        tessellator.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / f), (double)z);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)z);
        tessellator.draw();
	}

	public void restartForUpdate() {
		Utils.log.info("Restarting client to apply update. Please make sure that "+path+" is deleted.");
		if (Minecraft.getMinecraft().theWorld != null) {
			Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
			Minecraft.getMinecraft().loadWorld((WorldClient)null);
			Minecraft.getMinecraft().displayGuiScreen(this);
		}
		Minecraft.getMinecraft().shutdown();
		try {
			System.exit(0);
		} catch (FMLSecurityManager.ExitTrappedException e) {
			Utils.log.error("The following error message is normal if you are restarting to apply update.");
			Utils.log.error(e);
		}
	}

	public boolean deleteCurrentJar() {
		try {
			path = Utils.findPathJar();
		} catch (IllegalStateException e) {
			Utils.log.error(e);
			return true;
		}
		Utils.log.info(path);
		File f = new File(path);
		
		boolean deleted = false;
		try {
			deleted = f.delete();
		} catch (SecurityException e) {
			// ignore
		}

		// else delete the file when the program ends
		if (deleted) {
			Utils.log.info("Old version ("+f.getName()+") deleted");
		} else {
			f.deleteOnExit();
			Utils.log.error("Old version ("+f.getName()+") couldn't be deleted immediately, scheduling for deletion on JVM shutdown.");
			return true;
		}
		return false;
	}

	@Override
	public GuiScreen getParentScreen() {
		return parentScreen;
	}

}
