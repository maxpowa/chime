package com.maxpowa.chime.gui;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.gui.buttons.GuiUpdateButton;
import com.maxpowa.chime.util.Utils;

public class GuiUpdateInfo extends GuiScreen implements IChimeGUI {

	private GuiScreen parentScreen;
	
	public GuiUpdateInfo(GuiScreen currentScreen) {
		
		GuiUpdateButton.openingUpdate = false;
	}
	
	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(12, 20, 20, "Test restart client"));
		this.buttonList.add(new GuiButton(13, 20, 40, "Test delete current jar"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 12) {
				restartForUpdate();
			} else if (button.id == 13) {
				deleteCurrentJar();
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		mc.fontRenderer.drawStringWithShadow("unimplemented", 10, 10, 0xFFFFFF);
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	
	
	public void restartForUpdate() {
		Minecraft.getMinecraft().shutdown();
		Minecraft.getMinecraft().shutdownMinecraftApplet();
	}
	
	public void deleteCurrentJar() {
		URL url = Chime.class.getProtectionDomain().getCodeSource().getLocation();
		Utils.log.info("File location:"+url.toString());
		File f;
		try {
		  f = new File(url.toURI());
		} catch(URISyntaxException | IllegalArgumentException e) {
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
	}
	
	@Override
	public GuiScreen getParentScreen() {
		return parentScreen;
	}

}
