package com.mamiyaotaru.zmod;

import java.io.File;
import java.io.PrintWriter;

import net.minecraft.client.Minecraft;

public interface IMod {

	void init(Minecraft minecraft);
	
	String getTag();

	void onTickInGame(Minecraft minecraft);

	void drawMod(float x, float y, float z, float frameDelta);
	
	boolean needsSave();

	void loadSettings(File settingsFile);

	void saveSettings(PrintWriter out);

}
