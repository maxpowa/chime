package com.maxpowa.chime.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.ServerListEntryNormal;

import org.lwjgl.input.Keyboard;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.util.RequestList;
import com.maxpowa.chime.util.User;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFriendRequests extends GuiScreen implements GuiYesNoCallback
{
    private GuiScreen previousScreen;
    private FriendRequestList selectionList;
    private RequestList userList;
    private boolean blockYesNo;
    private boolean addingServer;
    private boolean editing;
    private boolean directConnect;
    private boolean loaded;
	private GuiButton acceptButton;
	private GuiButton rejectButton;
	private GuiButton blockButton;

    public GuiFriendRequests(GuiScreen parentScreen)
    {
        this.previousScreen = parentScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        if (!this.loaded)
        {
            this.loaded = true;
            this.userList = new RequestList();
            this.userList.loadUserList();

            this.selectionList = new FriendRequestList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.selectionList.addUserList(this.userList);
        }
        else
        {
            this.selectionList.func_148122_a(this.width, this.height, 32, this.height - 64);
        }

        this.createButtons();
    }

    @SuppressWarnings("unchecked")
	public void createButtons()
    {
        this.buttonList.add(this.acceptButton = new GuiButton(0, this.width / 2 - 154, this.height - 28, 70, 20, "Accept"));
        this.buttonList.add(this.rejectButton = new GuiButton(1, this.width / 2 - 74, this.height - 28, 70, 20, "Reject"));
        this.buttonList.add(this.blockButton = new GuiButton(2, this.width / 2 + 4, this.height - 28, 70, 20, "Block"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 76, this.height - 28, 75, 20, "Cancel"));
        this.buttonList.add(new GuiTextButton(12, 10, 10, "Please donate to keep this service running!", -1, 0.5f));
        this.setSelected(this.selectionList.getSelectedIndex());
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        this.userList.loadUserList();
        this.selectionList.addUserList(this.userList);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            GuiListExtended.IGuiListEntry iguilistentry = this.selectionList.getSelectedIndex() < 0 ? null : this.selectionList.getListEntry(this.selectionList.getSelectedIndex());

            if (button.id == 2)
            {
                String s4 = ((FriendRequestEntry)iguilistentry).getUser().getUsername();

                if (s4 != null)
                {
                    this.blockYesNo = true;
                    String s = "Are you sure you want to remove this friend?";
                    String s1 = "You may have to request friendship again if you remove " + s4 + " from your friends list.";
                    String s2 = "Remove";
                    String s3 = "Cancel";
                    GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, this.selectionList.getSelectedIndex());
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            else if (button.id == 1)
            {
                // join a server
            }
            else if (button.id == 4)
            {
                this.directConnect = true;
                // direct connect
            }
            else if (button.id == 3)
            {
                this.mc.displayGuiScreen(this.previousScreen);
            }
            else if (button.id == 12) {
            	Utils.log.info("Click!");
            }
        }
    }

    private void refreshScreen()
    {
        this.mc.displayGuiScreen(new GuiFriendRequests(this.previousScreen));
    }

    public void confirmClicked(boolean p_73878_1_, int p_73878_2_)
    {
        GuiListExtended.IGuiListEntry iguilistentry = this.selectionList.getSelectedIndex() < 0 ? null : this.selectionList.getListEntry(this.selectionList.getSelectedIndex());

        if (this.blockYesNo)
        {
            this.blockYesNo = false;
            
            User tmp = ((FriendRequestEntry)iguilistentry).getUser();
            Chime.public_requests.child(Chime.myUser.getUUID()+"/requests/"+tmp.getUUID()).removeValue();
            Chime.me.child("blocks/"+tmp.getUUID()).setValue(System.currentTimeMillis());
            
            this.mc.displayGuiScreen(this);
        }
        else if (this.directConnect)
        {
            this.directConnect = false;

            // whatever we want to do when dc calls back
        }
        else if (this.addingServer)
        {
            this.addingServer = false;
        }
        else if (this.editing)
        {
            this.editing = false;

            // whatever we want to do when editing calls back
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        int j = this.selectionList.getSelectedIndex();
        GuiListExtended.IGuiListEntry iguilistentry = j < 0 ? null : this.selectionList.getListEntry(j);

        if (p_73869_2_ == 63)
        {
            this.refreshScreen();
        }
        else
        {
            if (j >= 0)
            {
                if (p_73869_2_ == 200)
                {
                    if (isShiftKeyDown())
                    {
                        if (j > 0 && iguilistentry instanceof ServerListEntryNormal)
                        {
                            this.userList.swapUsers(j, j - 1);
                            this.setSelected(this.selectionList.getSelectedIndex() - 1);
                            this.selectionList.scrollBy(-this.selectionList.getSlotHeight());
                            this.selectionList.addUserList(this.userList);
                        }
                    }
                    else if (j > 0)
                    {
                        this.setSelected(this.selectionList.getSelectedIndex() - 1);
                        this.selectionList.scrollBy(-this.selectionList.getSlotHeight());

                        if (this.selectionList.getListEntry(this.selectionList.getSelectedIndex()) instanceof ServerListEntryLanScan)
                        {
                            if (this.selectionList.getSelectedIndex() > 0)
                            {
                                this.setSelected(this.selectionList.getSize() - 1);
                                this.selectionList.scrollBy(-this.selectionList.getSlotHeight());
                            }
                            else
                            {
                                this.setSelected(-1);
                            }
                        }
                    }
                    else
                    {
                        this.setSelected(-1);
                    }
                }
                else if (p_73869_2_ == 208)
                {
                    if (isShiftKeyDown())
                    {
                        if (j < this.userList.countUsers() - 1)
                        {
                            this.userList.swapUsers(j, j + 1);
                            this.setSelected(j + 1);
                            this.selectionList.scrollBy(this.selectionList.getSlotHeight());
                            this.selectionList.addUserList(this.userList);
                        }
                    }
                    else if (j < this.selectionList.getSize())
                    {
                        this.setSelected(this.selectionList.getSelectedIndex() + 1);
                        this.selectionList.scrollBy(this.selectionList.getSlotHeight());

                        if (this.selectionList.getListEntry(this.selectionList.getSelectedIndex()) instanceof ServerListEntryLanScan)
                        {
                            if (this.selectionList.getSelectedIndex() < this.selectionList.getSize() - 1)
                            {
                                this.setSelected(this.selectionList.getSize() + 1);
                                this.selectionList.scrollBy(this.selectionList.getSlotHeight());
                            }
                            else
                            {
                                this.setSelected(-1);
                            }
                        }
                    }
                    else
                    {
                        this.setSelected(-1);
                    }
                }
                else if (p_73869_2_ != 28 && p_73869_2_ != 156)
                {
                    super.keyTyped(p_73869_1_, p_73869_2_);
                }
                else
                {
                    this.actionPerformed((GuiButton)this.buttonList.get(1));
                }
            }
            else
            {
                super.keyTyped(p_73869_1_, p_73869_2_);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        //this.drawDefaultBackground();
        this.selectionList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        
        this.drawCenteredString(this.fontRendererObj, "Chime Friend Requests", this.width / 2, 12, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public void setSelected(int p_146790_1_)
    {
        this.selectionList.setSelectedIndex(p_146790_1_);
        GuiListExtended.IGuiListEntry iguilistentry = p_146790_1_ < 0 ? null : this.selectionList.getListEntry(p_146790_1_);
        this.acceptButton.enabled = false;
        this.rejectButton.enabled = false;
        this.blockButton.enabled = false;

        if (iguilistentry != null)
        {
            this.acceptButton.enabled = true;
            this.rejectButton.enabled = true;
            this.blockButton.enabled = true;
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        this.selectionList.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
        this.selectionList.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_);
    }

    public RequestList getUserList()
    {
        return this.userList;
    }
}