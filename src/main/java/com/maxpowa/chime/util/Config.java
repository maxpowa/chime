package com.maxpowa.chime.util;

import java.io.File;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {

    public static Configuration file;

    public static boolean ENABLE_UPDATE_CHECKER = true;

    public static void setupConfig(File suggestedConfigFile) {
        file = new Configuration(suggestedConfigFile);
        syncConfig();
    }

    public static void syncConfig() {
        Config.ENABLE_UPDATE_CHECKER = file.getBoolean("Enable Update Checker", Configuration.CATEGORY_GENERAL, true, "Enables or disables the update checker");
        if (file.hasChanged()) {
            file.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equals("Chime"))
            Config.syncConfig();
    }

}
