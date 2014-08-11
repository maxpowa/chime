package com.maxpowa.chime.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.maxpowa.chime.util.Notification;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiNotification extends Gui
{
    private static final ResourceLocation notificationTextures = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    private static final ResourceLocation windowParts = new ResourceLocation("chime:textures/gui/windowParts.png");

    /** Holds the instance of the game (Minecraft) */
    private Minecraft mc;

    /** Holds the latest width scaled to fit the game window. */
    private int notificationWindowWidth;

    /** Holds the latest height scaled to fit the game window. */
    private int notificationWindowHeight;
    private String notificationGetLocalText;
    private String notificationStatName;
    private long notificationTime;
    
    private Notification theNotification;

    /**
     * Holds a instance of RenderItem, used to draw the notification icons on screen (is based on ItemStack)
     */
    private RenderItem itemRender;
    private boolean haveNotification;
	private boolean notificationIsPermanent;

    public GuiNotification(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
        this.itemRender = new RenderItem();
    }

    /**
     * Queue a notification to be displayed.
     */
    public void queueTemporaryNotification(Notification notify)
    {
        this.queueTemporaryNotification(notify, false);
    }

    /**
     * Queue a notification to be displayed. (Optionally multiline)
     */
    public void queueTemporaryNotification(Notification notify, boolean isMultiline)
    {
        this.notificationGetLocalText = notify.title;
        this.notificationStatName = notify.desc;
        this.notificationTime = Minecraft.getSystemTime();
        this.theNotification = notify;
        this.haveNotification = isMultiline;
    }
    
    /**
     * Queue a notification to be displayed. (Optionally multiline)
     */
    public void showPermanentNotification(Notification notify, boolean isMultiline)
    {
        this.notificationGetLocalText = notify.title;
        this.notificationStatName = notify.desc;
        this.notificationTime = Minecraft.getSystemTime();
        this.theNotification = notify;
        this.haveNotification = isMultiline;
    }
    
    public void hidePermanentNotification() {
    	this.showPermanentNotification(null, false);
    }

//    /**
//     * Queue a information about a notification to be displayed.
//     */
//    public void queueNotificationInformation(Notification notify)
//    {
//        this.notificationGetLocalText = notify.title;
//        this.notificationStatName = notify.desc;
//        this.notificationTime = Minecraft.getSystemTime() - 2500L;
//        this.theNotification = notify;
//        this.haveNotification = true;
//    }

    /**
     * Update the display of the notification window to match the game window.
     */
    private void updateNotificationWindowScale()
    {
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.notificationWindowWidth = this.mc.displayWidth;
        this.notificationWindowHeight = this.mc.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.notificationWindowWidth = scaledresolution.getScaledWidth();
        this.notificationWindowHeight = scaledresolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double)this.notificationWindowWidth, (double)this.notificationWindowHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    /**
     * Updates the small notification tooltip window, showing a queued notification if is needed.
     */
    public void updateNotificationWindow()
    {
        this.zLevel += 100;
        if (this.theNotification != null && (this.notificationTime != 0L || this.notificationIsPermanent))
        {
            double d0 = (double)(Minecraft.getSystemTime() - this.notificationTime) / 6000.0D;

            if (!this.haveNotification && (d0 < 0.0D || d0 > 1.0D) && !this.notificationIsPermanent)
            {
                this.notificationTime = 0L;
                this.theNotification = null;
            }
            else
            {
                if (mc.guiAchievement != null) {
                    if (ReflectionHelper.<Long, GuiAchievement>getPrivateValue(GuiAchievement.class, mc.guiAchievement, "field_146263_l", "bcn.l", "notificationTime") != 0L)
                        ReflectionHelper.setPrivateValue(GuiAchievement.class, mc.guiAchievement, 0L, "field_146263_l", "bcn.l", "notificationTime");
                }
                this.updateNotificationWindowScale();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                double d1 = d0 * 2.0D;

                if (d1 > 1.0D)
                {
                    d1 = 2.0D - d1;
                }

                d1 *= 4.0D;
                d1 = 1.0D - d1;

                if (d1 < 0.0D)
                    d1 = 0.0D;

                d1 *= d1;
                d1 *= d1;
                int i = this.notificationWindowWidth - 160;
                int j = 0 - (int)(d1 * 36.0D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                this.mc.getTextureManager().bindTexture(notificationTextures);
                GL11.glDisable(GL11.GL_LIGHTING);
                this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

                if (this.haveNotification)
                {
                    this.mc.fontRenderer.drawSplitString(this.notificationStatName, i + 30, j + 7, 120, 0xFFFFFF);
                }
                else
                {
                    this.mc.fontRenderer.drawString(this.notificationGetLocalText, i + 30, j + 7, -256);
                    this.mc.fontRenderer.drawString(this.notificationStatName, i + 30, j + 18, 0xFFFFFF);
                }

                RenderHelper.enableGUIStandardItemLighting();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                switch (this.theNotification.type) {
                    case MESSAGE:
                        this.mc.getTextureManager().bindTexture(windowParts);
                        this.drawTexturedModalRect(i + 8, j + 8, 16, 102, 16, 16);
                        break;
                    case FRIENDREQUEST:
                        this.itemRender.renderItemIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), new ItemStack(Items.diamond), i + 8, j + 8);
                        break;
                    case ONLINE:
                        this.mc.getTextureManager().bindTexture(windowParts);
                        this.drawTexturedModalRect(i + 8, j + 8, 16, 118, 16, 16);
                        break;
                    case OFFLINE:
                        this.mc.getTextureManager().bindTexture(windowParts);
                        this.drawTexturedModalRect(i + 8, j + 8, 0, 118, 16, 16);
                        break;
                    case STATUS:
                        this.mc.getTextureManager().bindTexture(windowParts);
                        this.drawTexturedModalRect(i + 8, j + 8, 60, 70, 16, 16);
                    	break;
                }
                
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
        this.zLevel -= 100;
    }
}
