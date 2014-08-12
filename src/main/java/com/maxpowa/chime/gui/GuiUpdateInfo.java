package com.maxpowa.chime.gui;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.gui.buttons.GuiUpdateButton;
import com.maxpowa.chime.util.Utils;

public class GuiUpdateInfo extends GuiScreen implements IChimeGUI {

	private GuiScreen parentScreen;
	
	public GuiUpdateInfo(GuiScreen currentScreen) {
		URL url = Chime.class.getProtectionDomain().getCodeSource().getLocation();
		File f;
		try {
		  f = new File(url.toURI());
		} catch(URISyntaxException e) {
		  f = new File(url.getPath());
		}
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
        }
		GuiUpdateButton.openingUpdate = false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		mc.fontRenderer.drawStringWithShadow("unimplemented", 10, 10, 0xFFFFFF);
	}
	
	public void restartForUpdate() {
		Minecraft.getMinecraft().shutdown();
		Minecraft.getMinecraft().shutdownMinecraftApplet();
	}
	
	@Override
	public GuiScreen getParentScreen() {
		return parentScreen;
	}

}
