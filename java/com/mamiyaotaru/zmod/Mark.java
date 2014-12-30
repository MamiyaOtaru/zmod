package com.mamiyaotaru.zmod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public final class Mark {
	public float x,y,z;
	public int min, max;
	public int r,g,b,a;
	public Mark() { 
	}
	// safe mark
	public Mark(int bx, int by, int bz, boolean sun) { 
		x = 0.5f + bx; y = by + 0.13f; z = 0.5f + bz; r = sun ? (byte)1 : (byte)0; 
	}
	// ore mark
	public Mark(int bx, int by, int bz, Mark c) { 
		x = 0.5f + bx; y = 0.5f + by; z = 0.5f + bz; r = c.r; g = c.g; b = c.b; 
	}
	// range mark
	public Mark(int a, int b) { 
		min = a; max = b; 
	}
	// color mark
	public Mark(int color) { 
		loadColor(color); 
	}
	public void loadColor(int color) {
		r = ((color>>16) & 255); 
		g = ((color>>8) & 255);
		b = (color & 255); 
		//System.out.println("r: " + r + ", g: " + g + ", b: " + b);
	}
	public boolean loadColor(String color) {
		int c = Integer.decode(color);
		if (c < 0) return false;
		loadColor(c);
		return true;
	}
	// item mark
	public static Mark makeItem(int id) {
		Mark mark = new Mark();
		Item item;
		item = ZMod.getItem(id); 
		mark.setMaxStack(ZMod.getItemMax(item)); 
		mark.setMaxDamage(ZMod.getItemDmgCap(item));
		if (id >= 4096) return mark;
		Block block = ZMod.getBlock(id);
		mark.setLightEmission(ZMod.getBlockLight(block)); 
		mark.setLightReduction(ZMod.getBlockOpacity(block));
		//mark.setStrength(ZMod.getBlockStrength(block)); 
		//mark.setResistance(ZMod.getBlockResist(block)); 
		mark.setSlipperiness(ZMod.getBlockSlip(block));
		return mark;
	}
	public void setMaxStack(int val) { r = (byte)val; }
	public void setMaxDamage(int val) { max = val; }
	public void setLightEmission(int val) { g = (byte)val; }
	public void setLightReduction(int val) { min = val; }
	public void setStrength(float val) { x = val; }
	public void setResistance(float val) { y = val; }
	public void setSlipperiness(float val) { z = val; }
	public void setFireBurn(int val) { b = (byte)val; }
	public void setFireSpread(int val) { a = (byte)val; }
	public void activate(int id) {
		Item item;
		item = ZMod.getItem(id); 
		/*ZMod.setItemMax(item, r); // TODO figure out what if anything this is for
		ZMod.setItemDmgCap(item, max);
		if (id >= 4096) return;
		Block block = ZMod.getBlock(id); 
		ZMod.setBlockLight(id, g); 
		ZMod.setBlockOpacity(id, min);
		ZMod.setBlockStrength(block, x); 
		ZMod.setBlockResist(block, y); 
		ZMod.setBlockSlip(block, z);
		ZMod.setFireSpread(id, a); 
		ZMod.setFireBurn(id, b);*/
	}


	private static final class Text {
		public String msg; public int x, y, color;
		public Text(String pmsg, int px, int py, int pcolor) { msg = pmsg; x = px; y = py; color = pcolor; }
	}
}

