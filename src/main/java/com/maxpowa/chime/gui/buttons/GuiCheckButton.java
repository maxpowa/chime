package com.maxpowa.chime.gui.buttons;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiCheckButton extends GuiButton {

	private boolean state;
	private static final ResourceLocation check = new ResourceLocation("textures/gui/container/beacon.png");
	private static final ResourceLocation bg = new ResourceLocation("textures/gui/achievement/achievement_background.png");

	public GuiCheckButton(int id, int posX, int posY, boolean state) {
		super(id, posX, posY, 26, 26, "");
		this.state = state;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		GL11.glPushMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(bg);
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 26, 202, 26, 26);
        mc.getTextureManager().bindTexture(check);
        if (this.state)
        	this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, 88, 219, 21, 22);
        else
        	this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, 110, 219, 21, 22);
        GL11.glPopMatrix();
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean val = super.mousePressed(mc, mouseX, mouseY);
		if (val)
			this.state = !this.state;
		return val;
	}
	
	public boolean getState() {
		return this.state;
	}
	
	public void setState(boolean state) {
		this.state = state;
	}
	
}
