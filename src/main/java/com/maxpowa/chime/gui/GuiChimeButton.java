package com.maxpowa.chime.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;

import org.lwjgl.input.Mouse;

public class GuiChimeButton extends GuiButton {
	
    private boolean hovering = false;

    public GuiChimeButton(int id, int posX, int posY, String text) {
        super(id, posX, posY, Minecraft.getMinecraft().fontRenderer.getStringWidth(text)+10, 20, text);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
        hovering = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        
        if (Mouse.isButtonDown(0) && hovering && !(mc.currentScreen instanceof GuiIngameMenu /** change this to chime gui **/)) {
            // show guis
        }
    }
}
