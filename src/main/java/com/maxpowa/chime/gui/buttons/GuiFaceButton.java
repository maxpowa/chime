package com.maxpowa.chime.gui.buttons;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.data.User;
import com.maxpowa.chime.gui.GuiHelper;

public class GuiFaceButton extends GuiButton {

    User user;
    Color altColor = new Color(0);

    public GuiFaceButton(int id, int posX, int posY, User user) {
        super(id, posX - 1, posY - 1, 24, 24, "");
        this.user = user;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.drawSolidRect(this.xPosition, this.yPosition, this.xPosition + 24, this.yPosition + 24, user.averageColor.getRGB());
        GuiHelper.drawFace(mc, user, this.xPosition + 1, this.yPosition + 1, 2.75f);
    }

    public void drawSolidRect(int vertex1, int vertex2, int vertex3, int vertex4, int color) {
        GL11.glPushMatrix();
        Color color1 = new Color(color);
        Tessellator tess = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tess.startDrawingQuads();
        tess.setColorOpaque(color1.getRed(), color1.getGreen(), color1.getBlue());
        tess.addVertex(vertex1, vertex4, zLevel);
        tess.addVertex(vertex3, vertex4, zLevel);
        tess.addVertex(vertex3, vertex2, zLevel);
        tess.addVertex(vertex1, vertex2, zLevel);
        tess.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

}
