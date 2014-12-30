package com.mamiyaotaru.zmod.litemod;

import com.mumfrey.liteloader.core.runtime.Obf;

public class ZModObfuscationTable extends Obf {

	// these MUST be updated for every version of minecraft, to use the new searge and obfuscated names
	
	//public static ZModObfuscationTable EntityRenderer = new ZModObfuscationTable("net.minecraft.client.renderer.EntityRenderer", "cji"); // just inherit this from Obf
	public static ZModObfuscationTable renderRainSnow = new ZModObfuscationTable("func_78474_d", "d", "renderRainSnow");
	
	public static ZModObfuscationTable RenderGlobal = new ZModObfuscationTable("net.minecraft.client.renderer.RenderGlobal", "ckn");
	public static ZModObfuscationTable ICamera = new ZModObfuscationTable("net.minecraft.client.renderer.culling.ICamera", "cox");
	public static ZModObfuscationTable setupTerrain = new ZModObfuscationTable("func_174970_a", "a", "setupTerrain");
	
	protected ZModObfuscationTable(String name) {
		super(name, name, name);
	}
	
	protected ZModObfuscationTable(String seargeName, String obfName) {
		super(seargeName, obfName, seargeName);
	}
	
	protected ZModObfuscationTable(String seargeName, String obfName, String mcpName) {
		super(seargeName, obfName, mcpName);
	}

}
