package com.mamiyaotaru.zmod;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;

import org.lwjgl.input.Keyboard;

public class Common {
	
    protected static double getEntityPrevPosX(Entity ent) {
    	return ent.lastTickPosX; 
    }
    protected static double getEntityPrevPosY(Entity ent) {
    	return ent.lastTickPosY; 
    }
    protected static double getEntityPrevPosZ(Entity ent) {
    	return ent.lastTickPosZ; 
    }
    protected static double getEntityPosX(Entity ent) { 
    	return ent.posX; 
    }
    protected static double getEntityPosY(Entity ent) { 
    	return ent.posY; 
    }
    protected static double getEntityPosZ(Entity ent) { 
    	return ent.posZ; 
    }
    protected static int fix(double d) { 
    	return (int)Math.floor(d); 
    } // returns correct integer coordinate
	
    /**
     * Represents a key or mouse button as a string. Args: key
     */
    public static String getKeyDisplayString(int par0)
    {
    	// MC GUI used to use
        //return par0 < 0 ? StatCollector.translateToLocalFormatted("key.mouseButton", new Object[] {Integer.valueOf(par0 + 101)}): Keyboard.getKeyName(par0);
    	// now uses
        return par0 < 0 ? I18n.format("key.mouseButton", new Object[] {Integer.valueOf(par0 + 101)}): Keyboard.getKeyName(par0);
        // both call identical code, using a different copy of the translation HashMap.  Why are there two paths?
    }
    
    protected static void err(String text) {
        System.err.println(text);
    }
    
    protected static void err(String text, Exception e) {
        System.err.println(text);
        e.printStackTrace();
    }

}
