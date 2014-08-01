package com.maxpowa.chime.gui;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.util.Utils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.ResourceLocation;

public class GuiBetaFullScreen extends GuiScreen implements GuiYesNoCallback {

	private static final String message = "Hey! Looks like you missed out, the beta is currently full. You can retry later though - a spot will likely open up. To retry, simply relaunch your minecraft and Chime will try to find an open beta slot. The faster we hit goals on Patreon, the faster Chime can become fully open and these slots will no longer be an issue - please seriously consider supporting Chime.";
	private GuiScreen parentScreen;
	private static final ResourceLocation headerImages = new ResourceLocation("chime:textures/gui/headers.png");
	
	private GuiButton continueButton = null;
	private GuiButton patreonButton = null;
	private URI clickedURI;
	
	public GuiBetaFullScreen(GuiScreen currentScreen) {
		this.parentScreen = currentScreen;
	}

	@SuppressWarnings("unchecked")
	@Override
    public void initGui() {
		continueButton = new GuiButton(10, this.width/2+2, this.height - 30, 100, 20, "Okay, continue");
		patreonButton = new GuiButton(12, this.width/2-102, this.height - 30, 100, 20, "Go to Patreon");
		this.buttonList.add(continueButton);
		this.buttonList.add(patreonButton);
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		List<String> split = mc.fontRenderer.listFormattedStringToWidth(message, (this.width/3)*2);
		int baseY = (this.height/2)-((split.size()/2)*10);
		int i = 0;
		for (String s : split) {
			this.drawCenteredString(mc.fontRenderer, s, this.width/2, baseY+(i*10), 0xFFFFFF);
			i++;
		}
		int bottomY = (this.height/2)+((split.size()/2)*10)+30;
		continueButton.yPosition = bottomY;
		patreonButton.yPosition = bottomY;
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		mc.getTextureManager().bindTexture(headerImages);
		// x, y, u, v, width, height, textureWidth, textureHeight
		Gui.func_146110_a(this.width-150, this.height-(split.size()*10)-100, 0, 89, 300, 89, 300, 178);
		GL11.glPopMatrix();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
    protected void actionPerformed(GuiButton button) {
		if (button.id == continueButton.id){
			mc.displayGuiScreen(parentScreen);
		} else if (button.id == patreonButton.id) {
			try {
				this.clickedURI = new URI("http://patreon.com/maxpowa");
			} catch (URISyntaxException e) {}
            this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, this.clickedURI.toString(), 0, false));
		}
    }

	@Override
    public void confirmClicked(boolean result, int code)
    {
        if (code == 0)
        {
            if (result)
            {
                this.openLink(this.clickedURI);
            }

            this.clickedURI = null;
            this.mc.displayGuiScreen(this);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void openLink(URI url)
    {
        try
        {
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {url});
        }
        catch (Throwable throwable)
        {
            Utils.log.error("Couldn\'t open link", throwable);
        }
    }
}
