package com.maxpowa.chime.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.data.User;

public class GuiHelper {

	public static void drawFace(Minecraft mc, User user, int x, int y, float scale) {
		GL11.glPushMatrix();
        GL11.glScalef(scale,  scale, 1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslatef(x/scale, y/scale, 1.0f);
        mc.getTextureManager().bindTexture(user.getSkin());
        Gui.func_146110_a(0, 0, 8.0F, 8.0F, 8, 8, user.getSkinWidth(), user.getSkinHeight());
        Gui.func_146110_a(0, 0, 40.0F, 8.0F, 8, 8, user.getSkinWidth(), user.getSkinHeight());
        GL11.glPopMatrix();
	}
	
}
