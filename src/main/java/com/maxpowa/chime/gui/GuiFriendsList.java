package com.maxpowa.chime.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.listeners.ListenerRegistry;
import com.maxpowa.chime.util.ServerInfo.Type;
import com.maxpowa.chime.util.User;
import com.maxpowa.chime.util.UserList;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFriendsList extends GuiScreen implements GuiYesNoCallback
{
    private GuiScreen previousScreen;
    private UserSelectionList selectionList;
    private UserList userList;
    private GuiButton chatButton;
    private GuiButton joinButton;
    private GuiButton unfriendButton;
    private boolean removingFriend;
    private boolean editing;
    private boolean loaded;

    public GuiFriendsList(GuiScreen parentScreen)
    {
        this.previousScreen = parentScreen;
        FMLClientHandler.instance().setupServerList();
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
            this.userList = new UserList();
            this.userList.loadUserList();

            this.selectionList = new UserSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
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
        this.buttonList.add(this.chatButton = new GuiButton(7, this.width / 2 - 154, this.height - 28, 73, 20, "[Redacted]"));
        this.buttonList.add(this.unfriendButton = new GuiButton(2, this.width / 2 - 75, this.height - 28, 73, 20, "Un-friend"));
        this.buttonList.add(this.joinButton = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, "Join (SMP only)"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, "Friend Requests"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, "Add friend"));
        this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 73, 20, "Refresh"));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 82, this.height - 28, 73, 20, "Cancel"));
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

            if (button.id == 2 && iguilistentry instanceof FriendListEntry)
            {
                String s4 = ((FriendListEntry)iguilistentry).getUser().getUsername();

                if (s4 != null)
                {
                    this.removingFriend = true;
                    String s = "Are you sure you want to remove this friend?";
                    String s1 = "You may have to request friendship again if you remove " + s4 + " from your friends list.";
                    String s2 = EnumChatFormatting.RED+"Remove";
                    String s3 = "Cancel";
                    GuiConfirmation guiyesno = new GuiConfirmation(this, s, s1, s2, s3, this.selectionList.getSelectedIndex());
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            else if (button.id == 1)
            {
            	ServerData sd = ((FriendListEntry)iguilistentry).getUser().getCurrentServer().getServerData();

            	FMLClientHandler.instance().connectToServer(this, sd);
            }
            else if (button.id == 4)
            {
                this.mc.displayGuiScreen(new GuiFriendRequests(this));
            }
            else if (button.id == 3)
            {
                this.mc.displayGuiScreen(new GuiScreenAddFriend(this, new User()));
            }
            else if (button.id == 7)
            {
                this.editing = true;
                // maybe chat?
            }
            else if (button.id == 0)
            {
                this.mc.displayGuiScreen(this.previousScreen);
            }
            else if (button.id == 8)
            {
                this.refreshScreen();
            }
            else if (button.id == 12) {
            	Utils.log.info("Click!");
            }
        }
    }

    private void refreshScreen()
    {
        this.mc.displayGuiScreen(new GuiFriendsList(this.previousScreen));
    }

    @SuppressWarnings("unused")
	public void confirmClicked(boolean result, int id)
    {
        GuiListExtended.IGuiListEntry iguilistentry = this.selectionList.getSelectedIndex() < 0 ? null : this.selectionList.getListEntry(this.selectionList.getSelectedIndex());

        if (this.removingFriend)
        {
            this.removingFriend = false;

            if (result) {
	            User tmp = ((FriendListEntry)this.selectionList.getListEntry(id)).getUser();
            	Utils.log.info("Removing "+tmp.getUsername()+" ("+tmp.getUUID()+") from friends list");
	            Chime.users.child(tmp.getUUID()+"/friends/"+Chime.myUser.getUUID()).removeValue();
	            Chime.me.child("friends/"+tmp.getUUID()).removeValue();
	            ListenerRegistry.removeFriend(tmp.getUUID());
            }
            
            this.mc.displayGuiScreen(this);
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
                    this.actionPerformed((GuiButton)this.buttonList.get(2));
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
        
        this.drawCenteredString(this.fontRendererObj, "Chime Friends List", this.width / 2, 12, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public void setSelected(int p_146790_1_)
    {
        this.selectionList.setSelectedIndex(p_146790_1_);
        GuiListExtended.IGuiListEntry iguilistentry = p_146790_1_ < 0 ? null : this.selectionList.getListEntry(p_146790_1_);
        this.joinButton.enabled = false;
        this.chatButton.enabled = false;
        this.unfriendButton.enabled = false;       

        if (iguilistentry != null)
        {
        	User user = ((FriendListEntry)iguilistentry).getUser();
        	if (user.getCurrentServer().getType() == Type.MP/**&& user.isOnline()**/) {
        		this.joinButton.enabled = true;
        	}
            //TODO: CHAT this.chatButton.enabled = true;
            this.unfriendButton.enabled = true;
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

    public UserList getUserList()
    {
        return this.userList;
    }
}