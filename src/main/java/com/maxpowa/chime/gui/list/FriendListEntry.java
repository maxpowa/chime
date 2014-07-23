package com.maxpowa.chime.gui.list;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.data.User;
import com.maxpowa.chime.data.ServerInfo.Type;
import com.maxpowa.chime.gui.GuiFriendsList;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FriendListEntry implements GuiListExtended.IGuiListEntry
{
	private final GuiFriendsList parentScreen;
	private final Minecraft mc;
	private User userData;
	private long lastTick;

	protected FriendListEntry(GuiFriendsList p_i45048_1_, User p_i45048_2_)
	{
		this.parentScreen = p_i45048_1_;
		this.userData = p_i45048_2_;
		this.mc = Minecraft.getMinecraft();
	}

	public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
	{

		this.mc.fontRenderer.drawString(this.userData.getUsername(), p_148279_2_ + 32 + 3, p_148279_3_ + 1, 16777215);

		this.mc.fontRenderer.drawString(this.userData.getMotd(), p_148279_2_ + 35, p_148279_3_ + 12, 8421504);

		String prefix = this.userData.isOnline() ? "Playing" : "Last seen";
		String activity = this.userData.getCurrentServer().toString();
		if (activity.equalsIgnoreCase("Minecraft") && !this.userData.isOnline())
			activity = Utils.millisToTimeSpan(System.currentTimeMillis() - this.userData.lastSeen())+ " ago";
		else if (activity.equalsIgnoreCase("singleplayer") && !this.userData.isOnline())
			activity = Utils.millisToTimeSpan(System.currentTimeMillis() - this.userData.lastSeen())+ " ago playing singleplayer";
		else if (!this.userData.isOnline() && this.userData.getCurrentServer().getType() == Type.MP || this.userData.getCurrentServer().getType() == Type.LAN)
			activity = Utils.millisToTimeSpan(System.currentTimeMillis() - this.userData.lastSeen())+ " ago "+activity;
		String output = EnumChatFormatting.YELLOW+prefix+" "+activity;
		this.mc.fontRenderer.drawString(output, p_148279_2_ + 35, p_148279_3_ + 23, 8421504);

		String s2 = this.userData.isOnline() ? EnumChatFormatting.GREEN + "Online" : EnumChatFormatting.RED + "Offline";
		int i2 = this.mc.fontRenderer.getStringWidth(s2);
		this.mc.fontRenderer.drawString(s2, p_148279_2_ + p_148279_4_ - i2 - 5, p_148279_3_ + 1, 8421504);

        if (this.userData.getSkin() != null)
        {
            GL11.glPushMatrix();
            GL11.glScalef(4.0f, 4.0f, 1.0f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(this.userData.getSkin());
            GL11.glTranslatef(p_148279_2_/4.0f, p_148279_3_/4.0f, 1.0f);
            Gui.func_146110_a(0, 0, 8.0F, 8.0F, 8, 8, this.userData.getSkinWidth(), this.userData.getSkinHeight());
            Gui.func_146110_a(0, 0, 40.0F, 8.0F, 8, 8, this.userData.getSkinWidth(), this.userData.getSkinHeight());
            GL11.glPopMatrix();
        }
	}

	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
	{
		this.parentScreen.setSelected(p_148278_1_);

		if (Minecraft.getSystemTime() - this.lastTick < 250L)
		{
			this.parentScreen.getUserList();
		}

		this.lastTick = Minecraft.getSystemTime();
		return false;
	}

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}

	public User getUser()
	{
		return this.userData;
	}
}