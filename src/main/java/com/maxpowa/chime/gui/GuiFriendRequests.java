package com.maxpowa.chime.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.data.RequestList;
import com.maxpowa.chime.data.User;
import com.maxpowa.chime.gui.buttons.GuiTextButton;
import com.maxpowa.chime.gui.list.FriendRequestEntry;
import com.maxpowa.chime.gui.list.FriendRequestList;
import com.maxpowa.chime.listeners.ListenerRegistry;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFriendRequests extends GuiScreen implements GuiYesNoCallback, IChimeGUI
{
    private GuiScreen previousScreen;
    private FriendRequestList selectionList;
    private RequestList userList;
    private boolean blockYesNo;
    private boolean acceptRequest;
    private boolean rejectRequest;
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

            this.selectionList = new FriendRequestList(this, this.mc, this.width, this.height, 32, this.height - 36, 36);
            this.selectionList.addUserList(this.userList);
        }
        else
        {
            this.selectionList.func_148122_a(this.width, this.height, 32, this.height - 36);
        }

        this.createButtons();
    }

    @SuppressWarnings("unchecked")
	public void createButtons()
    {
        this.buttonList.add(this.acceptButton = new GuiButton(0, this.width / 2 - 154, this.height - 28, 73, 20, EnumChatFormatting.GREEN+"Accept"));
        this.buttonList.add(this.rejectButton = new GuiButton(1, this.width / 2 - 75, this.height - 28, 73, 20, EnumChatFormatting.RED+"Reject"));
        this.buttonList.add(this.blockButton = new GuiButton(2, this.width / 2 + 4, this.height - 28, 73, 20, "Block"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 82, this.height - 28, 73, 20, "Cancel"));
        this.buttonList.add(new GuiTextButton(12, 10, 10, "Patreon", -1, 0.5f));
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
        	// Initiate user block confirmation dialog
            if (button.id == 2)
            {
                String s4 = ((FriendRequestEntry)this.selectionList.getListEntry(this.selectionList.getSelectedIndex())).getUser().getUsername();

                if (s4 != null)
                {
                    this.blockYesNo = true;
                    String s = "Are you sure you want to block "+s4+"?";
                    String s1 = "You will no longer recieve requests from this user and they will not be able to access any of your information.";
                    String s2 = EnumChatFormatting.RED+"Block";
                    String s3 = "Cancel";
                    GuiConfirmation guiyesno = new GuiConfirmation(this, s, s1, s2, s3, this.selectionList.getSelectedIndex());
                    this.mc.displayGuiScreen(guiyesno);
                }
            }
            // Jump to confirmClicked because we don't need a dialog here.
            else if (button.id == 1)
            {
            	this.rejectRequest = true;
            	this.confirmClicked(true, this.selectionList.getSelectedIndex());
            }
            else if (button.id == 0)
            {
            	this.acceptRequest = true;
            	this.confirmClicked(true, this.selectionList.getSelectedIndex());
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

    public void confirmClicked(boolean result, int id)
    {
        if (this.blockYesNo)
        {
            this.blockYesNo = false;
            
            if (result) {
	            User tmp = ((FriendRequestEntry)this.selectionList.getListEntry(id)).getUser();
            	Utils.log.info("Blocking "+tmp.getUsername()+" ("+tmp.getUUID()+")");
	            Chime.public_requests.child("users/"+Chime.myUser.getUUID()+"/requests/"+tmp.getUUID()).removeValue();
	            Chime.me.child("blocks/"+tmp.getUUID()).setValue(System.currentTimeMillis());
	            RequestList.requests.remove(tmp.getUUID());
            }
            
            this.loaded = false;
            this.mc.displayGuiScreen(this);
        }
        else if (this.rejectRequest)
        {
            this.rejectRequest = false;
            
            if (result) {
	            User tmp = ((FriendRequestEntry)this.selectionList.getListEntry(id)).getUser();
            	Utils.log.info("Rejecting "+tmp.getUsername()+" ("+tmp.getUUID()+")");
	            Chime.public_requests.child("users/"+Chime.myUser.getUUID()+"/requests/"+tmp.getUUID()).removeValue();
            }

            this.mc.displayGuiScreen(new GuiFriendRequests(this.previousScreen));
        }
        else if (this.acceptRequest)
        {
            this.acceptRequest = false;
            
            if (result) {
	            User tmp = ((FriendRequestEntry)this.selectionList.getListEntry(id)).getUser();
            	Utils.log.info("Accepting "+tmp.getUsername()+" ("+tmp.getUUID()+")");
	            Chime.public_requests.child("users/"+Chime.myUser.getUUID()+"/requests/"+tmp.getUUID()).removeValue();
	            Chime.me.child("friends/"+tmp.getUUID()).setValue(System.currentTimeMillis());
	            RequestList.requests.remove(tmp.getUUID());
	            ListenerRegistry.addFriend(tmp.getUUID());
            }

            this.mc.displayGuiScreen(new GuiFriendRequests(this.previousScreen));
        } 
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char c, int keyCode)
    {
        int j = this.selectionList.getSelectedIndex();
        //GuiListExtended.IGuiListEntry iguilistentry = j < 0 ? null : this.selectionList.getListEntry(j);
        
        if (keyCode == Keyboard.KEY_F5)
        {
            this.refreshScreen();
        }
        else
        {
            if (j >= 0)
            {
            	if (keyCode == Keyboard.KEY_UP) {
            		if (j > 0) {
            			this.setSelected(j-1);
            		}
            	} else if (keyCode == Keyboard.KEY_DOWN) {
            		if (j < this.selectionList.getSize()-1) {
            			this.setSelected(j+1);
            		}
            	} else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                    this.actionPerformed(this.acceptButton);
            	} else if (keyCode == Keyboard.KEY_DELETE) {
            		this.actionPerformed(this.rejectButton);
            	} else {
                    super.keyTyped(c, keyCode);
                }
            }
            else
            {
                super.keyTyped(c, keyCode);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
    	GL11.glPushMatrix();
    	GL11.glEnable(GL11.GL_BLEND); 
        //this.drawDefaultBackground();
        this.selectionList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        
        this.drawCenteredString(this.fontRendererObj, "Chime Friend Requests", this.width / 2, 12, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        GL11.glPopMatrix();
    }

    public void setSelected(int p_146790_1_)
    {
        this.selectionList.setSelectedIndex(p_146790_1_);
        GuiListExtended.IGuiListEntry iguilistentry = p_146790_1_ < 0 ? null : this.selectionList.getListEntry(p_146790_1_);
        this.acceptButton.enabled = false;
        this.rejectButton.enabled = false;
        this.blockButton.enabled = false;

        this.acceptButton.displayString = "Accept";
        this.rejectButton.displayString = "Reject";

        if (iguilistentry != null)
        {
            this.acceptButton.enabled = true;
            this.rejectButton.enabled = true;
            this.blockButton.enabled = true;
            this.acceptButton.displayString = EnumChatFormatting.GREEN+"Accept";
            this.rejectButton.displayString = EnumChatFormatting.RED+"Reject";
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