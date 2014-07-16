package com.maxpowa.chime.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import com.maxpowa.chime.util.User;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FriendsListEntry implements GuiListExtended.IGuiListEntry
{
    private final GuiFriendsList parentScreen;
    private final Minecraft mc;
    private User userData;
    private long lastTick;
    private String base64Icon;
    private DynamicTexture dynamicTexture;
    private ResourceLocation resourceLocation;

    protected FriendsListEntry(GuiFriendsList p_i45048_1_, User p_i45048_2_)
    {
        this.parentScreen = p_i45048_1_;
        this.userData = p_i45048_2_;
        this.mc = Minecraft.getMinecraft();
        this.resourceLocation = new ResourceLocation("users/" + p_i45048_2_.getUUID() + "/icon");
        this.dynamicTexture = (DynamicTexture)this.mc.getTextureManager().getTexture(this.resourceLocation);
    }

    @SuppressWarnings("unchecked")
	public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
    {
    	
        this.mc.fontRenderer.drawString(this.userData.getUsername(), p_148279_2_ + 32 + 3, p_148279_3_ + 1, 16777215);

        this.mc.fontRenderer.drawString(this.userData.getMotd(), p_148279_2_ + 35, p_148279_3_ + 12, 8421504);
        
        String prefix = this.userData.isOnline() ? "Playing" : "Last seen";
        String activity = this.userData.getCurrentServer().toString();
        if (activity.equalsIgnoreCase("Minecraft") && !this.userData.isOnline())
        	activity = Utils.millisToTimeSpan(System.currentTimeMillis() - this.userData.lastSeen())+ " ago";
        else if (activity.equalsIgnoreCase("singleplayer") && !this.userData.isOnline())
        	activity = "playing singleplayer";
        String output = EnumChatFormatting.YELLOW+prefix+" "+activity;
        this.mc.fontRenderer.drawString(output, p_148279_2_ + 35, p_148279_3_ + 23, 8421504);

        String s2 = this.userData.isOnline() ? EnumChatFormatting.GREEN + "Online" : EnumChatFormatting.RED + "Offline";
        int i2 = this.mc.fontRenderer.getStringWidth(s2);
        this.mc.fontRenderer.drawString(s2, p_148279_2_ + p_148279_4_ - i2 - 5, p_148279_3_ + 1, 8421504);

        /**
        if (this.userData.getBase64EncodedIconData() != null && !this.userData.getBase64EncodedIconData().equals(this.base64Icon))
        {
            this.base64Icon = this.userData.getBase64EncodedIconData();
            this.prepareIcon();
            this.parentScreen.func_146795_p().saveServerList();
        }

        if (this.dynamicTexture != null)
        {
            this.mc.getTextureManager().bindTexture(this.resourceLocation);
            Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        }
        **/
    }

    /**
    private void prepareIcon()
    {
        if (this.userData.getBase64EncodedIconData() == null)
        {
            this.mc.getTextureManager().deleteTexture(this.resourceLocation);
            this.dynamicTexture = null;
        }
        else
        {
            ByteBuf bytebuf = Unpooled.copiedBuffer(this.userData.getBase64EncodedIconData(), Charsets.UTF_8);
            ByteBuf bytebuf1 = Base64.decode(bytebuf);
            BufferedImage bufferedimage;
            label74:
            {
                try
                {
                    bufferedimage = ImageIO.read(new ByteBufInputStream(bytebuf1));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break label74;
                }
                catch (Exception exception)
                {
                    logger.error("Invalid icon for user " + this.userData.getUsername() + " (" + this.userData.getUUID() + ")", exception);
                    this.userData.func_147407_a((String)null);
                }
                finally
                {
                    bytebuf.release();
                    bytebuf1.release();
                }

                return;
            }

            if (this.dynamicTexture == null)
            {
                this.dynamicTexture = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.mc.getTextureManager().loadTexture(this.resourceLocation, this.dynamicTexture);
            }

            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.dynamicTexture.getTextureData(), 0, bufferedimage.getWidth());
            this.dynamicTexture.updateDynamicTexture();
        }
    }
    **/

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