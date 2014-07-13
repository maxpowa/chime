package com.maxpowa.chime.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiChimeButton extends GuiButton {
	
    private boolean hovering = false;

    public GuiChimeButton(int posX, int posY) {
        super(-1, posX, posY, 20, 20, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    	GL11.glPushMatrix();
    	GL11.glDisable(GL11.GL_LIGHTING);
    	float tempZ = this.zLevel;
    	this.zLevel = 1000;
        super.drawButton(mc, mouseX, mouseY);
        hovering = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        
        mc.getTextureManager().bindTexture(new ResourceLocation("chime:textures/gui/windowParts.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(xPosition+3, yPosition+3, 76, 70, 14, 14);
        
        if (Mouse.isButtonDown(0) && hovering && !(mc.currentScreen instanceof GuiFriendsList /** change this to chime gui **/)) {
            mc.displayGuiScreen(new GuiFriendsList(mc.currentScreen));
        }
        this.zLevel = tempZ;
        GL11.glPopMatrix();
    }
}
