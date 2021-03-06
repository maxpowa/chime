package com.maxpowa.chime.gui.list;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.maxpowa.chime.data.RequestList;
import com.maxpowa.chime.gui.GuiFriendRequests;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FriendRequestList extends GuiListExtended {
    private ResourceLocation bg = new ResourceLocation("chime:textures/gui/bgTrans.png");
    private final GuiFriendRequests parentScreen;
    private final List<FriendRequestEntry> userHolder = Lists.newArrayList();
    private int selectedIndex = -1;

    private ResourceLocation stone = new ResourceLocation("textures/blocks/stone.png");

    public FriendRequestList(GuiFriendRequests p_i45049_1_, Minecraft p_i45049_2_, int p_i45049_3_, int p_i45049_4_, int p_i45049_5_, int p_i45049_6_, int p_i45049_7_) {
        super(p_i45049_2_, p_i45049_3_, p_i45049_4_, p_i45049_5_, p_i45049_6_, p_i45049_7_);
        this.parentScreen = p_i45049_1_;
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
        if (p_148180_1_ < this.userHolder.size()) {
            return (GuiListExtended.IGuiListEntry) this.userHolder.get(p_148180_1_);
        }
        return null;
    }

    public int getSize() {
        return this.userHolder.size();
    }

    public void setSelectedIndex(int p_148192_1_) {
        this.selectedIndex = p_148192_1_;
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int p_148131_1_) {
        return p_148131_1_ == this.selectedIndex;
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    @Override
    protected void drawContainerBackground(Tessellator tessellator) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (Minecraft.getMinecraft().theWorld != null)
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.bg);
        else
            Minecraft.getMinecraft().getTextureManager().bindTexture(stone);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f1 = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(2105376);
        tessellator.addVertexWithUV((double) this.left, (double) this.bottom, 0.0D, (double) ((float) this.left / f1), (double) ((float) (this.bottom + (int) this.getAmountScrolled()) / f1));
        tessellator.addVertexWithUV((double) this.right, (double) this.bottom, 0.0D, (double) ((float) this.right / f1), (double) ((float) (this.bottom + (int) this.getAmountScrolled()) / f1));
        tessellator.addVertexWithUV((double) this.right, (double) this.top, 0.0D, (double) ((float) this.right / f1), (double) ((float) (this.top + (int) this.getAmountScrolled()) / f1));
        tessellator.addVertexWithUV((double) this.left, (double) this.top, 0.0D, (double) ((float) this.left / f1), (double) ((float) (this.top + (int) this.getAmountScrolled()) / f1));
        tessellator.draw();
        GL11.glPopMatrix();
    }

    @Override
    protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int p_148120_3_, int p_148120_4_) {
        int i1 = this.getSize();
        Tessellator tessellator = Tessellator.instance;

        for (int j1 = 0; j1 < i1; ++j1) {
            int k1 = p_148120_2_ + j1 * this.slotHeight + this.headerPadding;
            int l1 = this.slotHeight - 4;

            if (k1 <= this.bottom && k1 + l1 >= this.top) {
                if (this.isSelected(j1)) {
                    GL11.glPushMatrix();
                    int i2 = this.left + (this.width / 2 - this.getListWidth() / 2);
                    int j2 = this.left + this.width / 2 + this.getListWidth() / 2;
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.25F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    tessellator.setColorRGBA_I(0xFFFFFF, 80);
                    tessellator.addVertexWithUV((double) i2, (double) (k1 + l1 + 2), 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV((double) j2, (double) (k1 + l1 + 2), 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV((double) j2, (double) (k1 - 2), 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((double) i2, (double) (k1 - 2), 0.0D, 0.0D, 0.0D);
                    tessellator.setColorRGBA_I(0, 128);
                    tessellator.addVertexWithUV((double) (i2 + 1), (double) (k1 + l1 + 1), 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV((double) (j2 - 1), (double) (k1 + l1 + 1), 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV((double) (j2 - 1), (double) (k1 - 1), 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((double) (i2 + 1), (double) (k1 - 1), 0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glPopMatrix();
                }

                this.drawSlot(j1, p_148120_1_, k1, l1, tessellator, p_148120_3_, p_148120_4_);
            }
        }
    }

    public void addUserList(RequestList userList) {
        this.userHolder.clear();

        for (int i = 0; i < userList.countUsers(); ++i) {
            this.userHolder.add(new FriendRequestEntry(this.parentScreen, userList.getUserData(i)));
        }
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 30;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return super.getListWidth() + 85;
    }
}