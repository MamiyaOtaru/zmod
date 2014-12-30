package com.mamiyaotaru.zmod.litemod;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;

import com.mamiyaotaru.zmod.ZMod;
import com.mumfrey.liteloader.HUDRenderListener;
import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.RenderListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.transformers.event.EventInfo;

public class LiteModZMod implements Tickable, InitCompleteListener, HUDRenderListener, RenderListener {
	public static LiteModZMod instance;
	Minecraft minecraft;
	
	private long lastTick = 0;
	private boolean hasVoxelPlugins = true;
	
	public LiteModZMod() {
	}
		
	@Override
	public String getName() {
		return "ZMod";
	}
	
	@Override
	public String getVersion() {
		return "1.0.0";
	}
	
	@Override
	public void init(File configPath) {
		instance = this;
	}
	
	@Override
	public void onInitCompleted(Minecraft minecraft, LiteLoader loader) {
		this.minecraft = minecraft;
		ZMod.init(minecraft);
		System.out.println("ZMOD inited");
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
	}
	
	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame,	boolean clock) { // from initCompleteListener, which extends tickable
		ZMod.onTickInGame(minecraft);
	}

	@Override
	public void onRender() { // pre render anything
	}

	@Override
	public void onRenderGui(GuiScreen currentScreen) { // post render world, pre gui
	}

	@Override
	public void onRenderWorld() { // before rendering the world (and before setting up viewport)
	}

	@Override
	public void onSetupCameraTransform() { // after camera is transformed, before world is drawn.  Fails ghost mode
	}
	
	@Override
	public void onPreRenderHUD(int screenWidth, int screenHeight) {
	}

	@Override
	public void onPostRenderHUD(int screenWidth, int screenHeight) {
		boolean inGame = minecraft.getRenderViewEntity() != null && minecraft.getRenderViewEntity().worldObj != null;// && !minecraft.gameSettings.hideGUI;
		if (inGame) 
			ZMod.drawTag();
	}
	
    public static void onWeatherRender(EventInfo<EntityRenderer> e, float arg1) {
		ZMod.drawModsRender();
    }

}
