package com.mamiyaotaru.zmod;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.mamiyaotaru.zmod.utils.GLShim;
import com.mamiyaotaru.zmod.utils.ReflectionUtils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Timer;

public class ZMod {
	
	private static Minecraft game;
	private static Timer timer;
    private static WorldClient map;
	private static EntityPlayerSP player;
    private static EntityLivingBase cheatView = null; // TODO wtf is this
    
    private static ArrayList<IMod> mods = new ArrayList<IMod>();
    private static String tag = "";
	
	private static float prevTick = 0;
	
    public static void init(Minecraft minecraft) {
    	game = minecraft;
    	timer = (Timer)ReflectionUtils.getPrivateFieldValueByType(game, Minecraft.class, Timer.class);
    	Safe safe = new Safe();
    	mods.add(safe);
    	Cheat cheat = new Cheat();
    	mods.add(cheat);
    	for (IMod mod: mods) {
    		mod.init(minecraft);
    	}
    	loadSettings();
    }
    
	public static void onTickInGame(Minecraft minecraft) {
		player = minecraft.thePlayer;
    	map = minecraft.theWorld;
    	for (IMod mod: mods) {
    		mod.onTickInGame(minecraft);
    	}
    	boolean needsSave = false;
    	for (IMod mod: mods) {
    		needsSave = needsSave || mod.needsSave();
    	}
    	if (needsSave)
    		saveSettings();
	}
	
	public static void drawTag() {
		tag = "";
    	for (IMod mod: mods) {
    		tag += mod.getTag();
    	}
		game.fontRendererObj.drawStringWithShadow(tag, 5, 5, 0xffffff);
	}
    
    public static void drawModsRender() {
    	float delta = timer.renderPartialTicks;
        //minecraft.gameSettings.clouds = origClouds;
        //minecraft.gameSettings.renderDistance = origFog;
        if (player == null || map == null || game.entityRenderer == null) 
        	return;
        try {
            // update time
            long curTick = System.nanoTime();
            float seconds = ((float)(curTick - prevTick)) * 0.000000001f;
            if (seconds > 1f) 
            	seconds = 0f;
            float frameDelta = delta;
            prevTick = curTick;
            // draw in 3d
            // update player position
            float px = (float)Common.getEntityPosX(player);
            float py = (float)Common.getEntityPosY(player);
            float pz = (float)Common.getEntityPosZ(player);
            float mx = (float)Common.getEntityPrevPosX(player);
            float my = (float)Common.getEntityPrevPosY(player);
            float mz = (float)Common.getEntityPrevPosZ(player);
            if (cheatView != null) {
                px = (float)Common.getEntityPosX(cheatView);
                py = (float)Common.getEntityPosY(cheatView);
                pz = (float)Common.getEntityPosZ(cheatView);
                mx = (float)Common.getEntityPrevPosX(cheatView);
                my = (float)Common.getEntityPrevPosY(cheatView);
                mz = (float)Common.getEntityPrevPosZ(cheatView);
            }
            float x = mx + (px - mx) * delta, y = my + (py - my) * delta, z = mz + (pz - mz) * delta;
            
            // handle mods
            boolean gltex2d = GLShim.glGetBoolean(GLShim.GL_TEXTURE_2D);
            boolean gldepth = GLShim.glGetBoolean(GLShim.GL_DEPTH_TEST);
            boolean glblend = GLShim.glGetBoolean(GLShim.GL_BLEND);
            boolean glfog   = GLShim.glGetBoolean(GLShim.GL_FOG);
            try {
            	for (IMod mod: mods) {
            		mod.drawMod(x, y, z, frameDelta);
            	}
            }
            catch(Exception error) {
            	Common.err("error: \"safe\" draw failed", error); 
            }
            // put previous GL fog, blend, deopth etc back how they were before mods drew 
            if (glfog)   
            	GLShim.glEnable( GLShim.GL_FOG);
            else         
            	GLShim.glDisable(GLShim.GL_FOG);
            if (glblend) 
            	GLShim.glEnable( GLShim.GL_BLEND);
            else         
            	GLShim.glDisable(GLShim.GL_BLEND);
            if (gldepth) 
            	GLShim.glEnable( GLShim.GL_DEPTH_TEST);
            else         
            	GLShim.glDisable(GLShim.GL_DEPTH_TEST);
            if (gltex2d) 
            	GLShim.glEnable( GLShim.GL_TEXTURE_2D);
            else        
            	GLShim.glDisable(GLShim.GL_TEXTURE_2D);
        } 
        catch(Exception error) { 
        	Common.err("error: draw-handle failed", error); 
        }
    }
    
    protected static Block getBlock(int id) { 
    	return Block.getBlockById(id); 
    }
    protected static boolean getBlockIsSpawn(int id) {
    	Block block = getBlock(id);
    	return block != null && block.getMaterial().isOpaque() && block.isFullCube(); 
    }

    protected static float getBlockSlip(Block block) { 
    	return block.slipperiness; 
    }
    private static Material getBlockMaterial(Block block) { 
    	return block.getMaterial(); 
    }
    private static boolean getBlockIsOpaque(Block block) {
    	return block.isOpaqueCube(); 
    }
    protected static int getBlockOpacity(Block block) { 
    	return block.getLightOpacity(); 
    }
    protected static int getBlockLight(Block block) { 
    	return block.getLightValue(); 
    }

    protected static Item getItem(int id) {
    	return Item.getItemById(id); 
    }
    protected static int getItemMax(Item item) { 
    	return item == null ? 0 : item.getItemStackLimit(); 
    }
    protected static int getItemDmgCap(Item item) { 
    	return item.getMaxDamage(); 
    }
    
    // settings
	/** 
	 * load all configuration variables from config file
	 */
	public static void loadSettings() {
		//File propertiesFile = new File(parentDirectory, "/mods/VoxelMods/voxeltextures.properties");
		//settingsFile = new File(getAppDir("minecraft/mods/VoxelMods"), "voxelmap.properties");
		File settingsFile = new File(game.mcDataDir, "mods/zmod/zmod.properties");
		if(settingsFile.exists()) {
	    	for (IMod mod: mods) {
	    		mod.loadSettings(settingsFile);
	    	}
		}

	}
	
	public static void saveSettings() {
		File settingsFileDir = new File(game.mcDataDir, "/mods/zmod/");
		if (!settingsFileDir.exists())
			settingsFileDir.mkdirs();
		File settingsFile = new File(settingsFileDir, "zmod.properties");
		try {
			PrintWriter out = new PrintWriter(new FileWriter(settingsFile));
	    	for (IMod mod: mods) {
	    		mod.saveSettings(out);
	    	}
			out.close();
		} catch (Exception local) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§EError Saving Settings " + local.getLocalizedMessage()));
		}
	}
    


}
