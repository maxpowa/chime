package com.maxpowa.chime.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.data.User;

public class GuiUserEditor extends GuiScreen {

	private User user;
	private GuiYesNoCallback parentScreen;
	private GuiTextField usernameField;
	private GuiTextField uuidField;
	private GuiTextField messageField;

	public GuiUserEditor(GuiYesNoCallback parentScreen, User user) {
        this.parentScreen = parentScreen;
        this.user = user;
	}
	
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
	@Override
	@SuppressWarnings("unchecked")
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

		this.usernameField = new GuiTextField(this.fontRendererObj, this.width / 2 - 26, this.height / 2 - 65, 186, 12);
        this.usernameField.setEnabled(false);
        this.usernameField.setText(this.user.getUsername());
        this.usernameField.setMaxStringLength(16);
        this.usernameField.setEnableBackgroundDrawing(false);
        this.usernameField.setDisabledTextColour(0x8F8F8F);
        
		this.uuidField = new GuiTextField(this.fontRendererObj, this.width / 2 - 53, this.height / 2 - 53, 213, 12);
        this.uuidField.setEnabled(false);
        this.uuidField.setText(this.user.getUUID());
        this.uuidField.setMaxStringLength(36);
        this.uuidField.setEnableBackgroundDrawing(false);
        this.uuidField.setDisabledTextColour(0x8F8F8F);
        
		this.messageField = new GuiTextField(this.fontRendererObj, this.width / 2 - 50, this.height / 2 - 41, 216, 12);
        this.messageField.setEnabled(Chime.myUser == this.user);
        this.messageField.setText(this.user.getMotd());
        this.messageField.setMaxStringLength(38);
        this.messageField.setEnableBackgroundDrawing(false);
        this.messageField.setDisabledTextColour(0x8F8F8F);

        this.buttonList.add(new GuiButton(1, this.width / 2 + 44, this.height / 2 + 61, 60, 20, "Save"));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 108, this.height / 2 + 61, 60, 20, "Cancel"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 79, this.height / 2 - 16, 60, 20, "Favorite"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 15, this.height / 2 - 16, 60, 20, "Current"));
    }
	
	public void updateScreen()
    {
        this.usernameField.updateCursorCounter();
        this.uuidField.updateCursorCounter();
        this.messageField.updateCursorCounter();
    }
	
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    
    protected void keyTyped(char value, int keyCode)
    {
        this.usernameField.textboxKeyTyped(value, keyCode);
        this.uuidField.textboxKeyTyped(value, keyCode);
        this.messageField.textboxKeyTyped(value, keyCode);
    }
    
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.usernameField.setCursorPositionZero();
        this.uuidField.setCursorPositionZero();
        this.messageField.setCursorPositionZero();
        this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.uuidField.mouseClicked(mouseX, mouseY, mouseButton);
        this.messageField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void actionPerformed(GuiButton button)
    {
    	if (button.id == 0) {
    		this.parentScreen.confirmClicked(false, 0);
    	} else {
    		this.parentScreen.confirmClicked(true, 0);
    	}
    }
	
    @Override
    public void drawWorldBackground(int p_146270_1_)
    {
        if (this.mc.theWorld != null)
        {
            //this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        }
        else
        {
            this.drawBackground(p_146270_1_);
        }
    }
    
    /**
     * Draws the screen and all the components in it.
     */
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        mc.getTextureManager().bindTexture(new ResourceLocation("chime:textures/gui/editbg.png"));
        Gui.func_146110_a(this.width / 2 - 213, this.height / 2 - 120, 0, 0, 427, 240, 427, 240);
        /*this.fontRendererObj, this.width / 2 - 86, this.height / 2 - 30, 250, 12*/
        this.drawString(mc.fontRenderer, "Username:", this.width / 2 - 82, this.height / 2 - 65, 0xFFFFFF);
        this.drawString(mc.fontRenderer, "UUID:", this.width / 2 - 82, this.height / 2 - 53, 0xFFFFFF);
        this.drawString(mc.fontRenderer, "MOTD:", this.width / 2 - 82, this.height / 2 - 41, 0xFFFFFF);
        this.drawString(mc.fontRenderer, "Join Server:", this.width / 2 - 82, this.height / 2 - 29, 0xFFFFFF);
        
        String title = "Chime User Information";
        mc.fontRenderer.drawString(title, (this.width-mc.fontRenderer.getStringWidth(title))/2, this.height / 2 - 82, 0x666666);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation("chime:textures/gui/windowParts.png"));
        this.drawTexturedModalRect(this.width / 2 - 82, this.height / 2 - 19, 0, 150, 130, 26);
        
        GuiHelper.drawFace(mc, user, this.width / 2 - 160, this.height / 2 - 66, 9);
        GL11.glPopMatrix();
        
        this.usernameField.drawTextBox();
        this.uuidField.drawTextBox();
        this.messageField.drawTextBox();
        
        super.drawScreen(mouseX, mouseY, partialTicks);
        
    }

}
