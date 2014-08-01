package com.maxpowa.chime.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.maxpowa.chime.Chime;
import com.maxpowa.chime.data.User;
import com.maxpowa.chime.util.Configuration;
import com.maxpowa.chime.util.Utils;
import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScreenAddFriend extends GuiScreen implements IChimeGUI
{
    private final GuiScreen parentScreen;
    private User user;
    private GuiTextField uuidField;
    private GuiTextField usernameField;
	private String message = "";
	
	private ResourceLocation background = new ResourceLocation("textures/blocks/stone.png");

    public GuiScreenAddFriend(GuiScreen p_i1033_1_, User p_i1033_2_)
    {
        this.parentScreen = p_i1033_1_;
        this.user = p_i1033_2_;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.usernameField.updateCursorCounter();
        this.uuidField.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
	public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 125, this.height / 4 + 96 + 18, 250, 20, "Add friend"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 125, this.height / 4 + 120 + 18, 250, 20, "Cancel"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 125, this.height / 4 + 72 + 18, 250, 20, "Fetch UUID"));
        this.usernameField = new GuiTextField(this.fontRendererObj, this.width / 2 - 125, this.height / 4 + 15, 250, 15);
        this.usernameField.setFocused(true);
        this.usernameField.setText(this.user.getUsername());
        this.usernameField.setMaxStringLength(16);
        this.uuidField = new GuiTextField(this.fontRendererObj, this.width / 2 - 125, this.height / 4 + 55, 250, 15);
        this.uuidField.setMaxStringLength(36);
        this.uuidField.setText(this.user.getUUID());
        ((GuiButton)this.buttonList.get(0)).enabled = this.uuidField.getText().length() > 0 && this.uuidField.getText().split(":").length > 0 && this.usernameField.getText().length() > 0;
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
            if (button.id == 1)
            {
            	Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
            } else if (button.id == 0) {
                this.user.setUsername(this.usernameField.getText());
                this.user.setUUID(this.uuidField.getText());
                
                Chime.users.child(user.getUUID()+"/config").addListenerForSingleValueEvent(new ValueEventListener() {
                	
                	User user = GuiScreenAddFriend.this.user;
                	
					@Override
					public void onCancelled(FirebaseError data) {
						Utils.log.warn("Failed to add friend!");
					}

					@Override
					public void onDataChange(DataSnapshot data) {
						if (data.getValue(Configuration.class) == null || data.getValue(Configuration.class).isAllowingRequests()) {
			                Chime.public_requests.child("users/"+user.getUUID()+"/requests/"+Chime.myUser.getUUID()).setValue(Chime.myUser.getUsername());
			                Chime.me.child("friends/"+user.getUUID()).setValue(System.currentTimeMillis());
							Utils.log.info("Sent friend request to "+user.getUsername());
						} else {
							Utils.log.info(user.getUsername()+" is not accepting requests.");
						}
					}
                	
                });
                
                Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
            } else if (button.id == 2) {
            	 HttpProfileRepository profileRepo = new HttpProfileRepository();
                 Profile[] matchingProfiles = profileRepo.findProfilesByNames(this.usernameField.getText());
                 if (matchingProfiles.length > 0) {
                	 String tempid = matchingProfiles[0].getId();
                	 String uuid = tempid.replaceAll(                                            
                			    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",                            
                			    "$1-$2-$3-$4-$5"); 
                     this.uuidField.setText(uuid);
                     this.updateButtonStates();
                 } else {
                	 this.message = EnumChatFormatting.RED+" Error: Username does not exist";
                 }
                 
                 if (this.uuidField.getText().equalsIgnoreCase(Chime.myProfile.getId().toString())) {
                	 this.message = EnumChatFormatting.RED+" Error: You cannot add yourself as a friend!";
                 }
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        this.usernameField.textboxKeyTyped(p_73869_1_, p_73869_2_);
        this.uuidField.textboxKeyTyped(p_73869_1_, p_73869_2_);
        
        this.message = "";
        
        if (this.usernameField.isFocused() && this.uuidField.getText().length() > 0 && Character.isLetterOrDigit(p_73869_1_)) {
        	this.uuidField.setText("");
        }

        if (p_73869_2_ == 15)
        {
            this.usernameField.setFocused(!this.usernameField.isFocused());
            this.uuidField.setFocused(!this.uuidField.isFocused());
        }

        if (p_73869_2_ == 28 || p_73869_2_ == 156)
        {
        	if (((GuiButton)this.buttonList.get(0)).enabled) {
                this.actionPerformed((GuiButton)this.buttonList.get(0));
        	} else if (((GuiButton)this.buttonList.get(2)).enabled) {
                this.actionPerformed((GuiButton)this.buttonList.get(2));
        	}
        }
        
        this.updateButtonStates();
    }

    private void updateButtonStates() {
        if (this.usernameField.getText().length() > 0)
        	((GuiButton)this.buttonList.get(2)).enabled = true;
        else 
        	((GuiButton)this.buttonList.get(2)).enabled = false;

        if (this.uuidField.getText().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[0-9a-f]{12}") && !(this.uuidField.getText().equalsIgnoreCase(Chime.myProfile.getId().toString()))) {
        	((GuiButton)this.buttonList.get(0)).enabled = true;
        } else {
        	((GuiButton)this.buttonList.get(0)).enabled = false;
        }
    }

	/**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        this.uuidField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        this.usernameField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Add a friend", this.width / 2, this.height/4-30, 16777215);
        this.drawString(this.fontRendererObj, "Username" + message, this.width / 2 - 125, this.height/4+3, 10526880);
        this.drawString(this.fontRendererObj, "UUID (click Fetch UUID to auto-fill)", this.width / 2 - 125, this.height/4+43, 10526880);
        this.usernameField.drawTextBox();
        this.uuidField.drawTextBox();
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
    
    @Override
    public void drawBackground(int p_146278_1_)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(background);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(4210752);
        tessellator.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / f + (float)p_146278_1_));
        tessellator.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / f), (double)((float)this.height / f + (float)p_146278_1_));
        tessellator.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / f), (double)p_146278_1_);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)p_146278_1_);
        tessellator.draw();
    }
}