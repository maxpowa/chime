package com.maxpowa.chime.init;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@SortingIndex(191919191)
@TransformerExclusions({ "com.maxpowa.chime.init" })
public class ChimeLoadingPlugin implements IFMLLoadingPlugin {

    File versionFile = new File("config/chime/removeOnStart.json");
    JsonNode toRemove = null;

    public ChimeLoadingPlugin() {
        log("Preparing to remove old version");
        if (versionFile.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                toRemove = mapper.readValue(versionFile, JsonNode.class);
                // log(toRemove.toString());
                File remove = new File("mods", toRemove.get("delete").asText());
                log(remove.getCanonicalPath());
                if (remove.exists()) {
                    try {
                        remove.delete();
                    } catch (SecurityException e) {
                        log(e.getMessage());
                    }
                } else {
                    log("Previous version was already removed");
                }
            } catch (IOException e) {
                log(e.getMessage());
            }
        } else {
            log("No old version found");
        }
    }

    public static void log(String s) {
        FMLLog.info("[Chime] %s", s);
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
