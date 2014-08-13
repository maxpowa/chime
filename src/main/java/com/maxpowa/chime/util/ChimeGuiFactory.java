package com.maxpowa.chime.util;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;

public class ChimeGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {return ChimeConfigGui.class;}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {return null;}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {return null;}

	public static class ChimeConfigGui extends GuiConfig {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public ChimeConfigGui(GuiScreen parentScreen) {
			super(parentScreen, new ConfigElement(Config.file.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), "Chime", false,
					false, "Chime Config");
		}
		
	}
	
}
