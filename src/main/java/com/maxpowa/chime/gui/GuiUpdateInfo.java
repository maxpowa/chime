package com.maxpowa.chime.gui;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;

import com.maxpowa.chime.gui.buttons.GuiUpdateButton;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.relauncher.FMLSecurityManager;

public class GuiUpdateInfo extends GuiScreen implements IChimeGUI {

	private GuiScreen parentScreen;
	String path;

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
			return false;
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
		}
		return true;
	}

	@Override
	public GuiScreen getParentScreen() {
		return parentScreen;
	}

}
