package com.maxpowa.chime.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.data.RequestList;
import com.maxpowa.chime.data.ServerInfo.Type;
import com.maxpowa.chime.data.User;
import com.maxpowa.chime.data.UserList;
import com.maxpowa.chime.gui.buttons.GuiFaceButton;
import com.maxpowa.chime.gui.buttons.GuiTextButton;
import com.maxpowa.chime.gui.list.FriendListEntry;
import com.maxpowa.chime.gui.list.UserSelectionList;
import com.maxpowa.chime.listeners.ListenerRegistry;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFriendsList extends GuiScreen implements GuiYesNoCallback, IChimeGUI
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
        if (this.previousScreen instanceof GuiChat) {
        	this.previousScreen = null;
        }
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
        this.buttonList.add(this.joinButton = new GuiButton(1, this.width / 2 - 154, this.height - 52, 90, 20, "Join (SMP only)"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 60, this.height - 52, 120, 20, "Friend Requests ("+RequestList.requests.size()+")"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 60, this.height - 52, 90, 20, "Add friend"));
        this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 73, 20, "Refresh"));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 82, this.height - 28, 73, 20, "Cancel"));
        this.buttonList.add(new GuiTextButton(12, 10, 10, "Patreon", -1, 0.5f));
        this.buttonList.add(new GuiFaceButton(10, this.width - 27, 5, Chime.myUser));
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
        this.setSelected(this.selectionList.getSelectedIndex());
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
            	//chat
            }
            else if (button.id == 0)
            {
                this.mc.displayGuiScreen(this.previousScreen);
            }
            else if (button.id == 8)
            {
                this.refreshScreen();
            }
            else if (button.id == 10) {
            	this.editing = true;
            	GuiUserEditor editor = new GuiUserEditor(this, Chime.myUser);
            	mc.displayGuiScreen(editor);
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

            if (result) {
            	Chime.me.child("config").setValue(Chime.myUser.getConfig());
            	Chime.me.child("motd").setValue(Chime.myUser.getMotd());
            }
            
            this.mc.displayGuiScreen(this);
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
                    this.actionPerformed(this.joinButton);
            	} else if (keyCode == Keyboard.KEY_DELETE) {
            		this.actionPerformed(this.unfriendButton);
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
        
        if (Chime.myUser.getSkin() != null)
        {
            int headX = this.width - 27;
            
            this.mc.fontRenderer.drawString(Chime.myUser.getUsername(), headX-5-this.mc.fontRenderer.getStringWidth(Chime.myUser.getUsername()), 7, 0xFFFFFF);
    		this.mc.fontRenderer.drawString(Chime.myUser.getFormattedMotd(), headX-5-this.mc.fontRenderer.getStringWidth(Chime.myUser.getFormattedMotd()), 17, 8421504);
        }
        
        this.drawCenteredString(this.fontRendererObj, "Chime Friends List", this.width / 2, 6, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        GL11.glPopMatrix();
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
        	if (user.getCurrentServer().getType() == Type.MP && (user.getConfig() == null || user.getConfig().isAllowingJoins())) {
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

	@Override
	public GuiScreen getParentScreen() {
		return this.previousScreen;
	}
}