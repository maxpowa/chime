package com.maxpowa.chime.gui.buttons;

import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.gui.GuiHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiCheckButton extends GuiButton {

	private boolean state;
	private boolean drawTooltip = false;
	private String detail;
	private static final ResourceLocation check = new ResourceLocation("textures/gui/container/beacon.png");
	private static final ResourceLocation bg = new ResourceLocation("textures/gui/achievement/achievement_background.png");

	public GuiCheckButton(int id, int posX, int posY, String detail, boolean state) {
		super(id, posX, posY, 26, 26, "");
		if (detail.length() > 0)
			drawTooltip  = true;
		this.detail = detail;
		this.state = state;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        final boolean hovering = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
		GL11.glPushMatrix();
		this.zLevel = 0;
		if (hovering) {
			GL11.glColor4f(0.74f, 0.79f, 1.0f, 1.0f);
		} else {
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
		mc.getTextureManager().bindTexture(bg);
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 26, 202, 26, 26);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(check);
        if (this.state)
        	this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, 88, 219, 21, 22);
        else
        	this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, 110, 219, 21, 22);
        
        if (hovering && drawTooltip)
			GuiHelper.renderTooltip(mouseX, mouseY, mc.fontRenderer.listFormattedStringToWidth(this.detail, 120));
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
