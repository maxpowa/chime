package com.maxpowa.chime.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;

public class GuiConfirmation extends GuiYesNo implements IChimeGUI {

	private String bodyText;
	private List<String> bodyTextList = new ArrayList<String>();

	public GuiConfirmation(GuiYesNoCallback parentScreen, String title, String body, String btnText1, String btnText2, int id) {
		super(parentScreen, title, body, btnText1, btnText2, id);
        this.parentScreen = parentScreen;
        this.field_146351_f = title;
        this.bodyText = body;
        this.confirmButtonText = btnText1;
        this.cancelButtonText = btnText2;
        this.field_146357_i = id;
	}
	
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
	@SuppressWarnings("unchecked")
	@Override
    public void initGui()
    {
		super.initGui();
		this.bodyTextList = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(bodyText, this.width - 60);
    }
	
    /**
     * Draws the screen and all the components in it.
     */
	@Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146351_f, this.width / 2, 70, 16777215);
        for (int y = 0; y < bodyTextList.size(); y++) {
        	this.drawCenteredString(this.fontRendererObj, bodyTextList.get(y), this.width / 2, 90 + (y*10), 16777215);
        }

        int k;

        for (k = 0; k < this.buttonList.size(); ++k)
        {
            ((GuiButton)this.buttonList.get(k)).drawButton(this.mc, p_73863_1_, p_73863_2_);
        }

        for (k = 0; k < this.labelList.size(); ++k)
        {
            ((GuiLabel)this.labelList.get(k)).func_146159_a(this.mc, p_73863_1_, p_73863_2_);
        }
    }

}
