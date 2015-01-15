package com.maxpowa.chime.gui;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.io.Files;
import com.maxpowa.chime.gui.buttons.GuiUpdateButton;
import com.maxpowa.chime.util.UpdateChecker;
import com.maxpowa.chime.util.Utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.FMLSecurityManager;

public class GuiUpdateInfo extends GuiScreen implements IChimeGUI {

    /*
     * Download link:
     * http://addons.cursecdn.com/files/2211/456/Chime-1.7.10-0.1.198.jar
     * http://addons.cursecdn.com/files/Xchars/3chars/filename
     */

    private GuiScreen parentScreen;
    String path;
    File downloadLocation = null;

    static long bytesDownloaded = 0;
    static long totalBytes = 0;

    private ResourceLocation stone = new ResourceLocation("textures/blocks/stone.png");

    public GuiUpdateInfo(GuiScreen currentScreen) {
        try {
            downloadLocation = new File(Minecraft.getMinecraft().mcDataDir.getCanonicalPath(), "mods");
        } catch (IOException e) {
            Utils.log.info("Unable to locate mods folder. Disabling auto-update capabilities.");
            downloadLocation = null;
        }
        GuiUpdateButton.openingUpdate = false;
    }

    public static void downloadFromStream(InputStream stream, File location) throws IOException {
        BufferedInputStream in = new BufferedInputStream(stream);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(location));
        Utils.log.info(location.toString());
        int curByte;
        long startns = System.nanoTime();
        while ((curByte = in.read()) != -1) {
            GuiUpdateInfo.bytesDownloaded++;
            out.write(curByte);
            if (GuiUpdateInfo.bytesDownloaded % 100000 == 0)
                Utils.log.info("Bytes downloaded: " + GuiUpdateInfo.bytesDownloaded + " out of " + GuiUpdateInfo.totalBytes);
        }
        out.flush();
        out.close();
        in.close();
        Utils.log.info("Complete! Downloaded " + String.format("%.2f", GuiUpdateInfo.totalBytes / 1000000f) + " megabytes in " + String.format("%.3f", (System.nanoTime() - startns) / 1000000000f) + " seconds.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(12, 20, 20, "Test restart client"));
        this.buttonList.add(new GuiButton(13, 20, 40, "Test delete current jar"));
        this.buttonList.add(new GuiButton(14, 20, 60, "Download latest jar"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 12) {
                restartForUpdate();
            } else if (button.id == 13) {
                deleteCurrentJar();
            } else if (button.id == 14) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            HttpURLConnection downconn = (HttpURLConnection) new URL(UpdateChecker.latest.getDownloadURL()).openConnection();
                            bytesDownloaded = 0;
                            // Utils.log.info(downconn.getHeaderFields());
                            totalBytes = getContentLengthLong(downconn);
                            downloadFromStream(downconn.getInputStream(), new File(downloadLocation, UpdateChecker.latest.getFilename()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    public long getContentLengthLong(URLConnection conn) {
        return Utils.getHeaderFieldLong(conn, "content-length", -1);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawWorldBackground(0);

        if (GuiUpdateInfo.totalBytes > 0) {
            if (GuiUpdateInfo.totalBytes == GuiUpdateInfo.bytesDownloaded) {
                mc.fontRenderer.drawStringWithShadow("Done!", 10, 10, 0xFFFFFF);
            } else {
                mc.fontRenderer.drawStringWithShadow((GuiUpdateInfo.bytesDownloaded / GuiUpdateInfo.totalBytes * 100) + "%", 10, 10, 0xFFFFFF);
            }
        } else {
            mc.fontRenderer.drawStringWithShadow("Waiting...", 10, 10, 0xFFFFFF);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawBackground(int z) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(stone);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(4210752);
        tessellator.addVertexWithUV(0.0D, (double) this.height, 0.0D, 0.0D, (double) ((float) this.height / f + (float) z));
        tessellator.addVertexWithUV((double) this.width, (double) this.height, 0.0D, (double) ((float) this.width / f), (double) ((float) this.height / f + (float) z));
        tessellator.addVertexWithUV((double) this.width, 0.0D, 0.0D, (double) ((float) this.width / f), (double) z);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double) z);
        tessellator.draw();
    }

    public void restartForUpdate() {
        Utils.log.info("Restarting client to apply update. Please make sure that " + path + " is deleted.");
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
            Minecraft.getMinecraft().loadWorld((WorldClient) null);
            Minecraft.getMinecraft().displayGuiScreen(this);
        }
        Minecraft.getMinecraft().shutdown();
        
        try{
            Desktop.getDesktop().open(new File(Utils.findPathJar()).getParentFile());
        }catch (Exception e) {
            Utils.log.error("Tried to open mods folder to ensure old version deletion, but failed.");
            e.printStackTrace();
        }
        
        try {
            FMLCommonHandler.instance().exitJava(0, false);
        } catch (FMLSecurityManager.ExitTrappedException e) {
            Utils.log.error("The following error message is normal if you are restarting to apply update.");
            Utils.log.error(e.getMessage());
        } 
    }

    public void createStartupFile() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"current\":\"").append(UpdateChecker.latest.getFilename()).append("\", \"delete\":\"");
        try {
            sb.append(new File(Utils.findPathJar()).getName());
        } catch (IllegalStateException ex) {
            sb.append("no_filename");
        }

        sb.append("\" }");
        // Utils.log.info(sb.toString());
        try {
            Files.write(sb.toString(), new File("config/chime/removeOnStart.json"), Charset.forName("UTF-8"));
        } catch (IOException e) {
            Utils.log.warn("Unable to create removeOnStart.json, previous versions may persist.");
        }
    }

    public boolean deleteCurrentJar() {
        try {
            path = Utils.findPathJar();
        } catch (IllegalStateException e) {
            Utils.log.error(e);
            return true;
        }
        Utils.log.info(path);
        File f = new File(path);

        boolean deleted = false;
        try {
            deleted = f.delete();
        } catch (SecurityException e) {
            // ignore
        }
        
        try {
            FileUtils.forceDelete(f);
            deleted= true;
        }catch (Exception e) {
            // ignore
        }

        // else delete the file when the program ends
        if (deleted) {
            Utils.log.info("Old version (" + f.getName() + ") deleted");
        } else {
            f.deleteOnExit();
            try {
                FileUtils.forceDeleteOnExit(f);
            } catch (IOException e) {
                // meh
            }
            Utils.log.error("Old version (" + f.getName() + ") couldn't be deleted immediately, scheduling for deletion on JVM shutdown.");
            createStartupFile();
            Utils.log.info("Just in case, the previous version will also attempt to be deleted on next startup");
            return true;
        }
        return false;
    }

    @Override
    public GuiScreen getParentScreen() {
        return parentScreen;
    }

}
