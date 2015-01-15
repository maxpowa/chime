package com.maxpowa.chime.gui;

import net.minecraft.client.gui.GuiScreen;

// This indicates that the Chime button should not be shown on the implementing gui.
public interface IChimeGUI {

    // Return the parent screen
    GuiScreen getParentScreen();

}
