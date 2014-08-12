package com.maxpowa.chime.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.gui.GuiFriendsList;
import com.maxpowa.chime.gui.IChimeGUI;

public class GuiChimeButton extends GuiButton {
	
    private boolean hovering = false;

    public GuiChimeButton(int posX, int posY) {
        super(-1, posX, posY, 18, 20, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    	GL11.glPushMatrix();
    	GL11.glDisable(GL11.GL_LIGHTING);
    	GL11.glEnable(GL11.GL_BLEND); 
    	float tempZ = this.zLevel;
    	this.zLevel = 1000;
        //super.drawButton(mc, mouseX, mouseY);
        hovering = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        
        mc.getTextureManager().bindTexture(new ResourceLocation("chime:textures/gui/icon.png"));
        if (!hovering) {
        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        } else {
        	GL11.glColor4f(0.5F, 1.0F, 0.5F, 1.0F);
        }
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslatef((xPosition)*2f, (yPosition)*2f, 0.0f);
        Gui.func_146110_a(0, 0, 0, 0, 34, 39, 34.0f, 39.0f);
        
        if (Mouse.isButtonDown(0) && hovering && !(mc.currentScreen instanceof IChimeGUI /** change this to chime gui **/)) {
            mc.displayGuiScreen(new GuiFriendsList(mc.currentScreen));
        } else if (mc.currentScreen instanceof IChimeGUI) {
        	mc.displayGuiScreen(new GuiFriendsList(((IChimeGUI)mc.currentScreen).getParentScreen()));
        }
        this.zLevel = tempZ;
        GL11.glPopMatrix();
    }
}
