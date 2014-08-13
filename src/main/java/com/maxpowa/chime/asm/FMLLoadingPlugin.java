package com.maxpowa.chime.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"com.maxpowa.chime.asm"})
public class FMLLoadingPlugin implements IFMLLoadingPlugin
{
    public FMLLoadingPlugin()
    {
        //TODO: delete existing old versions here, if they aren't deleted already.
    	System.out.println("Loading plugin");
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}