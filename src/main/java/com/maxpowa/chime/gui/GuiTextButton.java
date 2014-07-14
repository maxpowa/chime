package com.maxpowa.chime.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiTextButton extends GuiButton {

	private int textColor = 0xFFFFFF;
	private float scale = 1.0f;

	public GuiTextButton(int id, int xpos, int ypos, String text, int textColor) {
		super(id, xpos, ypos, text);
		this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
		this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
		this.textColor = textColor;
		this.scale = 1.0f;
	}
	
	public GuiTextButton(int id, int xpos, int ypos, String text, int textColor, float scale) {
		super(id, xpos, ypos, text);
		this.scale = scale;
		this.height = Math.round(Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT*this.scale);
		this.width = Math.round(Minecraft.getMinecraft().fontRenderer.getStringWidth(text)*this.scale);
		this.textColor = textColor;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible)
        {
			GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            this.getHoverState(this.field_146123_n);
            this.mouseDragged(mc, mouseX, mouseY);
            
            GL11.glScalef(scale, scale, 1.0f);
            
            int rgb = textColor;
            if (textColor < 0) {
            	int red   = Math.round(Math.round(Math.sin(System.currentTimeMillis()/100 + 0) * 127 + 128));
                int green = Math.round(Math.round(Math.sin(System.currentTimeMillis()/100 + 2*Math.PI/3) * 127 + 128));
                int blue  = Math.round(Math.round(Math.sin(System.currentTimeMillis()/100 + 4*Math.PI/3) * 127 + 128));
                rgb = red;
                rgb = rgb << 8;
                rgb |= green;
                rgb = rgb << 8;
                rgb |= blue;
            }
            
            this.drawCenteredString(mc.fontRenderer, this.displayString, Math.round((this.xPosition + this.width / 2)/this.scale), Math.round((this.yPosition + (this.height - 6) / 2)/this.scale), rgb);
            GL11.glPopMatrix();
        }
	}

}
