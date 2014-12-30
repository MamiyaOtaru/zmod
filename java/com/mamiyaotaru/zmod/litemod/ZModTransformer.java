package com.mamiyaotaru.zmod.litemod;

import static com.mumfrey.liteloader.core.runtime.Methods.glClear;

import com.mumfrey.liteloader.transformers.event.Event;
import com.mumfrey.liteloader.transformers.event.EventInjectionTransformer;
import com.mumfrey.liteloader.transformers.event.MethodInfo;
import com.mumfrey.liteloader.transformers.event.inject.BeforeInvoke;
import com.mumfrey.liteloader.transformers.event.inject.BeforeReturn;
import com.mumfrey.liteloader.transformers.event.inject.MethodHead;

public class ZModTransformer extends EventInjectionTransformer {

	@Override
	protected void addEvents() {
		

		// hook into weather drawing
		Event renderRainSnowEvent = Event.getOrCreate("renderRainSnow");
		MethodInfo renderRainSnowMethod = new MethodInfo(ZModObfuscationTable.EntityRenderer, ZModObfuscationTable.renderRainSnow, Void.TYPE, Float.TYPE);
		//BeforeReturn beforeReturn = new BeforeReturn();
		MethodHead methodHead = new MethodHead();
		this.addEvent(renderRainSnowEvent, renderRainSnowMethod, methodHead);
		
		renderRainSnowEvent.addListener(new MethodInfo("com.mamiyaotaru.zmod.litemod.LiteModZMod", "onWeatherRender"));
		
		// hook into terrain sorting
		Event setupTerrainEvent = Event.getOrCreate("setupTerrain", true); // cancelable
		MethodInfo setupTerrainMethod = new MethodInfo(ZModObfuscationTable.RenderGlobal, ZModObfuscationTable.setupTerrain, Void.TYPE, ZModObfuscationTable.Entity, Double.TYPE, ZModObfuscationTable.ICamera, Integer.TYPE, Boolean.TYPE);
		this.addEvent(setupTerrainEvent, setupTerrainMethod, methodHead);
		setupTerrainEvent.addListener(new MethodInfo("com.mamiyaotaru.zmod.Cheat", "startCheatRender"));

		Event postSetupTerrainEvent = Event.getOrCreate("setupTerrain"); 
		BeforeReturn beforeReturn = new BeforeReturn();
		this.addEvent(postSetupTerrainEvent, setupTerrainMethod, beforeReturn);
		postSetupTerrainEvent.addListener(new MethodInfo("com.mamiyaotaru.zmod.Cheat", "stopCheatRender"));
	}

}
