package com.maxpowa.chime.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.gui.GuiUpdateInfo;
import com.maxpowa.chime.util.UpdateChecker;

public class GuiUpdateButton extends GuiButton {

    private boolean hovering = false;
    private boolean hoveringClose = false;
    public static boolean openingUpdate = false;

    int offset = 0;

    private static final ResourceLocation notificationParts = new ResourceLocation("chime:textures/gui/updateNotification.png");

    String notification = "";

    public GuiUpdateButton(int posX, int posY) {
        super(-1, posX, posY, 18, 16, "");
    }

    private static boolean hasNewerVersion() {
        if (UpdateChecker.latest == null)
            return false;
        else if (UpdateChecker.latest.getVersion().equalsIgnoreCase(Chime.VERSION))
            return false;
        else if (UpdateChecker.latest.getBuildNumber() <= getBuildNumber(Chime.VERSION))
            return false;
        else
            return true;
    }

    public static int getBuildNumber(String version) {
        String[] splitVer = version.split("[.]");
        String b = splitVer[splitVer.length - 1];
        try {
            return Integer.parseInt(b);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        hovering = mouseX >= xPosition - 10 && mouseY >= yPosition && mouseX < xPosition + width - 3 && mouseY < yPosition + height;
        hoveringClose = mouseX >= xPosition + width - 3 && mouseY >= yPosition && mouseX < xPosition + width + 6 && mouseY < yPosition + height;

        if (Mouse.isButtonDown(0)) {
            if (hovering && !openingUpdate) {
                openingUpdate = true;
                mc.displayGuiScreen(new GuiUpdateInfo(mc.currentScreen));
            }
            if (hoveringClose)
                UpdateChecker.dismissed = true;
        }

        if (offset <= 33 && hasNewerVersion()) {

            if (notification.isEmpty()) {
                notification = formatNotification();
            }
            width = mc.fontRenderer.getStringWidth(notification);

            if (UpdateChecker.dismissed)
                offset++;

            if (mc.currentScreen != null)
                this.xPosition = ((mc.currentScreen.width - width) / 2);

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glScalef(0.5f, 0.5f, 1.0f);
            GL11.glTranslatef((xPosition) * 2f, (yPosition) * 2f, 0.0f);
            mc.getTextureManager().bindTexture(notificationParts);
            // Draws a textured rectangle at z = 0. x, y, u, v, width, height,
            // textureWidth, textureHeight
            Gui.func_146110_a(-20, 0 - offset * 2, 0, 0, 10, 33, 21, 68);
            Gui.func_146110_a(-10, 0 - offset * 2, 0, 34, width * 2 + 20, 33, 21, 67);
            Gui.func_146110_a(width * 2 + 10, 0 - offset * 2, 11, 0, 10, 33, 21, 68);
            GL11.glPushMatrix();
            GL11.glScalef(2.0f, 2.0f, 1.0f);
            mc.fontRenderer.drawString(EnumChatFormatting.YELLOW + notification + EnumChatFormatting.RED + " \u2715", -5, 4 - offset, 0xFFFFFF);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }

    public String formatNotification() {
        StringBuilder sb = new StringBuilder();
        sb.append("A new Chime version is available! (").append(UpdateChecker.latest.getVersion()).append(")");
        return sb.toString();
    }
}
