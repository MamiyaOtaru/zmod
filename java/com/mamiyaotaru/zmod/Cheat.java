package com.mamiyaotaru.zmod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import com.mamiyaotaru.zmod.utils.GLShim;
import com.mamiyaotaru.zmod.utils.MutableBlockPos;
import com.mumfrey.liteloader.transformers.event.EventInfo;

public class Cheat implements IMod {
    private final int GHAST=1, COW=2, SPIDER=3, SHEEP=4, 
            SKELLY=5, CREEPER=6, ZOMBIE=7, SLIME=8, 
            PIG=9, CHICKEN=10, SQUID=11, PIGZOMBIE=12, 
            PLAYER=13, LIVING=14, WOLF=15, CAVESPIDER=16, 
            ENDERMAN=17, SILVERFISH=18, LAVASLIME=19, REDCOW=20, 
            VILLAGER=21, SNOWMAN=22, BLAZE=23, DRAGON=24, 
            GOLEM=25, OCELOT=26, MAXTYPE=27;
	
    private boolean optCheat, optCheatFallDamage, optCheatDisableDamage, optCheatRestoreHealth, optCheatShowDangerous, optCheatShowNeutral, optCheatSeeIsToggle, optCheatShowMobsSize;
    private int optCheatShowOresRangeH = 16;
    private int optCheatShowOresRangeV = 64;
    private int optCheatShowMobsRange = 64;
    private final int cheatMax = 16384;
    private Mark cheatMobs[], cheatOres[], cheatMark[];
    private boolean showMobs = false;
    private boolean showOres = false;
    private boolean seeThrough = false;
    private int cheatCur = 0;
    private int cheatUpdate = 0;
    private float optCheatSeeDist = 4f;
    
    private static Cheat instance;
    private String tag = "";
    
    private Minecraft game;
    private EntityPlayerSP player;
    private WorldClient map;
    private MutableBlockPos blockPos = new MutableBlockPos(0, 0, 0);
    
    public KeyBinding keyBindOreToggle = new KeyBinding("Ore", Keyboard.KEY_O, "ZMod");
    public KeyBinding keyBindMonsterToggle = new KeyBinding("Monsters", Keyboard.KEY_M, "ZMod");
    public KeyBinding keyBindSeeThrough = new KeyBinding("See Through", Keyboard.KEY_X, "ZMod");
    public KeyBinding[] keyBindings = new KeyBinding[] {keyBindOreToggle, keyBindMonsterToggle, keyBindSeeThrough};
    
    private int oldOreToggleCode;
    private int oldMonsterToggleCode;
    private int oldSeeThroughCode;
    public boolean needsSave = false;
    
    public Cheat() {
    	instance = this;
    }

    @Override
    public void init(Minecraft minecraft) {
        cheatMobs = new Mark[MAXTYPE];
        cheatOres = new Mark[4096];
        cheatMark = new Mark[cheatMax];
        cheatCur = 0;
        String val[];
        val = ("15/0x008888, 82/0x00ffff, 14/0xffee00, 56/0xeeffff, 129/0x00ff00, 21/0x0000ff, 73/0xff0000, 52/0xff00ff, 16/0x444444").split("[\\t ]*,[\\t ]*"); // switched 48 to 129 (mossy cobble, to emerald)
        for (int i=0;i<val.length;i++) {
            String got[] = val[i].split("/");
            if (got.length == 2) {
                Mark color = new Mark();
                int id = 0;
                if (got[0].matches("^\\d+$")) {
        			id = Integer.parseInt(got[0]);
        		} 
        		else if (got[0] != null && got[0] != ""){
        			id = Block.getIdFromBlock(Block.getBlockFromName(got[0]));
        		}
                if (id>0 && id<cheatOres.length && color.loadColor(got[1])) {
                    cheatOres[id] = color;
                }
            } else {
                Common.err("error: config.txt @ optCheatOres - invalid ore/color pair \""+val[i]+"\"");
            }
        }
        val = ("1/0x000088, 3/0x880000, 5/0x888888, 6/0x008800, 7/0x888800, 8/0x880088, 12/0x008888, 11/0x000044, 2/0x444400, 4/0x444444, 9/0x440000, 10/0x004444, 14/0x004400, 15/0xffffff").split("[\\t ]*,[\\t ]*");
        for (int i=0;i<val.length;i++) {
            String got[] = val[i].split("/");
            if (got.length == 2) {
                Mark color = new Mark();
                int id = Integer.parseInt(got[0]);
                if (id>=0 && id<MAXTYPE && color.loadColor(got[1])) { 
                	cheatMobs[id] = color; 
                	continue; 
                }
            }
            Common.err("error: config.txt @ optCheatMobs - invalid mob/color pair \""+val[i]+"\"");
        }
		ArrayList<KeyBinding> tempBindings = new ArrayList<KeyBinding>();
		tempBindings.addAll(Arrays.asList(minecraft.gameSettings.keyBindings));
		tempBindings.addAll(Arrays.asList(keyBindings));
		minecraft.gameSettings.keyBindings = tempBindings.toArray(new KeyBinding[tempBindings.size()]);
    }
    
	@Override
	public String getTag() {
		tag = "";
		if (showOres)
			tag += "o ";
		if (showMobs)
			tag += "m ";
		if (seeThrough)
			tag += "x ";
		return tag;
	}
    
    @Override
    public void onTickInGame(Minecraft minecraft) {
		map = minecraft.theWorld;
		player = minecraft.thePlayer;
        
		if (minecraft.currentScreen == null && keyBindOreToggle.isPressed()) {
			showOres = !showOres;
		}
		if (minecraft.currentScreen == null && keyBindMonsterToggle.isPressed()) {
			showMobs = !showMobs;
		}
		if (oldOreToggleCode != keyBindOreToggle.getKeyCode() || oldMonsterToggleCode != keyBindMonsterToggle.getKeyCode() || oldSeeThroughCode != keyBindSeeThrough.getKeyCode()) {
			oldOreToggleCode = keyBindOreToggle.getKeyCode();
			oldMonsterToggleCode = keyBindMonsterToggle.getKeyCode();
			oldSeeThroughCode = keyBindSeeThrough.getKeyCode();
			needsSave = true;
		}
    }

    @Override
    public void drawMod(float x, float y, float z, float frameDelta) {
    	if (!showMobs && !showOres) 
    		return;
    	double posX = Common.getEntityPosX(player);
    	double posY = Common.getEntityPosY(player);
    	double posZ = Common.getEntityPosZ(player);
    	float px = (float)posX;
    	float py = (float)posY;
    	float pz = (float)posZ;
    	float mx;
    	float my;
    	float mz;
    	float dx;
    	float dy;
    	float dz;
    	GLShim.glDisable(GLShim.GL_TEXTURE_2D);
    	GLShim.glDisable(GLShim.GL_BLEND);
    	GLShim.glEnable(GLShim.GL_DEPTH_TEST);

    	GLShim.glDisable(GLShim.GL_DEPTH_TEST);
    	GLShim.glDisable(GLShim.GL_FOG);
    	if (showMobs) {
    		//System.out.println("showing mobs");
    		//List list = (List)((ArrayList)map.loadedEntityList).clone(); // WHAT
    		//List list = new ArrayList() // same as
    		//list.addAll(map.loadedEntityList);
    		List list = new ArrayList(map.loadedEntityList); // same as
    		Iterator it=list.iterator();
    		float range = optCheatShowMobsRange * optCheatShowMobsRange;
    		//GLShim.glEnable(GLShim.GL_LINE_STIPPLE);
    		//GLShim.glLineStipple(1, (short)0x5555);
    		GLShim.glBegin(GLShim.GL_LINES);
    		while (it.hasNext()) {
    			Entity ent = (Entity)it.next();
    			int type = getEntityType(ent);
    			if (cheatMobs[type] == null || (ent == player)) 
    				continue;
    			px = (float)Common.getEntityPosX(ent);
    			py = (float)Common.getEntityPosY(ent) - (float)ent.getYOffset();
    			pz = (float)Common.getEntityPosZ(ent);
    			mx = (float)Common.getEntityPrevPosX(ent);
    			my = (float)Common.getEntityPrevPosY(ent) - (float)ent.getYOffset();
    			mz = (float)Common.getEntityPrevPosZ(ent);
    			mx = mx + (px - mx) * frameDelta;
    			my = my + (py - my) * frameDelta;
    			mz = mz + (pz - mz) * frameDelta;
    			dx = mx - x; 
    			dy = my - y; 
    			dz = mz - z;
    			if (optCheatShowMobsRange > 0 && dx*dx + dy*dy + dz*dz > range) continue;
    			GLShim.glColor3ub(cheatMobs[type].r, cheatMobs[type].g, cheatMobs[type].b);
    			GLShim.glVertex3f(dx,dy,dz); 
    			GLShim.glVertex3f(dx,dy+(optCheatShowMobsSize || !(ent instanceof EntityLiving) ? ent.height : 2.0f),dz);
    		}
    		GLShim.glEnd();
    		//GLShim.glDisable(GLShim.GL_LINE_STIPPLE);
    	}
    	GLShim.glBegin(GLShim.GL_LINES);
    	if (showOres) {
    		//System.out.println("showing ores");
    		if (--cheatUpdate<0) {
    			cheatUpdate = 16;
    			cheatReCheck(Common.fix(x), Common.fix(y), Common.fix(z));
    		}
    		for (int i = 0; i < cheatCur; i++) {
    			Mark got = cheatMark[i];
    			if (got == null) 
    				continue;
    			GLShim.glColor3ub(got.r,got.g,got.b);
    			mx = got.x - x; my = got.y - y; mz = got.z - z;
    			GLShim.glVertex3f(mx+0.25f,my+0.25f,mz+0.25f); 
    			GLShim.glVertex3f(mx-0.25f,my-0.25f,mz-0.25f);
    			GLShim.glVertex3f(mx+0.25f,my+0.25f,mz-0.25f); 
    			GLShim.glVertex3f(mx-0.25f,my-0.25f,mz+0.25f);
    			GLShim.glVertex3f(mx+0.25f,my-0.25f,mz+0.25f); 
    			GLShim.glVertex3f(mx-0.25f,my+0.25f,mz-0.25f);
    			GLShim.glVertex3f(mx+0.25f,my-0.25f,mz-0.25f); 
    			GLShim.glVertex3f(mx-0.25f,my+0.25f,mz+0.25f);
    		}
    	}
    	GLShim.glEnd();
    }
    
    private void cheatReCheck(int pX, int pY, int pZ) {
        cheatCur = 0;
        for (int x = pX - optCheatShowOresRangeH; x < pX + optCheatShowOresRangeH; ++x) 
        for (int y = pY - optCheatShowOresRangeV; y < pY + optCheatShowOresRangeV; ++y) 
        for (int z = pZ - optCheatShowOresRangeH; z < pZ + optCheatShowOresRangeH; ++z) {
            int id = Block.getIdFromBlock(map.getBlockState(blockPos.withXYZ(x,y,z)).getBlock());
            if (id > cheatOres.length) 
            	continue;
            Mark color = cheatOres[id];
            if (color == null) 
            	continue;
            cheatMark[cheatCur++] = new Mark(x,y,z,color);
            if (cheatCur == cheatMax) 
            	return;
        }
    }
    
    public static void startCheatRender(EventInfo<RenderGlobal> e, Entity arg1, double arg2, ICamera arg3, int arg4, boolean arg5) {
    	instance.doCheatRender(e, arg1, arg2, arg3, arg4, arg5);
    }

    public void doCheatRender(EventInfo<RenderGlobal> e, Entity arg1, double arg2, ICamera arg3, int arg4, boolean arg5) {
    	seeThrough = (Minecraft.getMinecraft().currentScreen == null && keyBindSeeThrough.getIsKeyPressed());
        try {
            if (seeThrough) {
                obliqueNearPlaneClip(0.0f, 0.0f, -1.0f, -optCheatSeeDist);
                orig_field_175612_E = Minecraft.getMinecraft().field_175612_E;
                // this var is accessed in RenderGlobal.setupTerrain.  If observing and in a solid block, it's set to false
                // if that happens, or if for some reason it was already false, culling does not take place
                // which is what we want so caves that should be hidden from us due to not being visible are drawn anyway 
                Minecraft.getMinecraft().field_175612_E = false;
                if (!lastSeeThrough)
                	arg1.posY = arg1.posY + .0001; // dirty the renderer on initial switch to xray mode
                lastSeeThrough = true;
            }
            else {
            	lastSeeThrough = false;
            }
        } 
        catch(Exception error) { 
        	Common.err("error: see-through setup failed", error); 
        }
    }
    
	private boolean orig_field_175612_E = true;
	private boolean lastSeeThrough = false;
	
    public static void stopCheatRender(EventInfo<RenderGlobal> e, Entity arg1, double arg2, ICamera arg3, int arg4, boolean arg5) {
    	instance.doPostCheatRender(e, arg1, arg2, arg3, arg4, arg5);
    }

    public void doPostCheatRender(EventInfo<RenderGlobal> e, Entity arg1, double arg2, ICamera arg3, int arg4, boolean arg5) {
    	// put variable back to however it was before so as not to affect anything other than rendering
    	// it isn't accessed anywhere else that I can see, but just to be safe
    	// but also to make sure culling (with its FPS increase) does in fact happen when we stop pressing the see through button
    	Minecraft.getMinecraft().field_175612_E = orig_field_175612_E;
    }
    
    private void obliqueNearPlaneClip(float a, float b, float c, float d) {
        float matrix[] = new float[16];
        float x, y, z, w, dot;
        FloatBuffer buf = makeBuffer(16);
        GLShim.glGetFloat(GLShim.GL_PROJECTION_MATRIX, buf);
        buf.get(matrix).rewind();
        x = (sgn(a) + matrix[8]) / matrix[0];
        y = (sgn(b) + matrix[9]) / matrix[5];
        z = -1.0F;
        w = (1.0F + matrix[10]) / matrix[14];
        dot = a*x + b*y + c*z + d*w;
        matrix[2] = a * (2f / dot);
        matrix[6] = b * (2f / dot);
        matrix[10] = c * (2f / dot) + 1.0F;
        matrix[14] = d * (2f / dot);
        buf.put(matrix).rewind();
        GLShim.glMatrixMode(GLShim.GL_PROJECTION);
        GLShim.glLoadMatrix(buf);
        GLShim.glMatrixMode(GLShim.GL_MODELVIEW);
    }
    
    private float sgn(float f) { 
    	return f<0f ? -1f : (f>0f ? 1f : 0f); 
    }
    
    private FloatBuffer makeBuffer(int length) { 
    	return ByteBuffer.allocateDirect(length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer(); 
    }
    
    private int getEntityType(Entity ent) {
        if (!(ent instanceof EntityLiving)) return 0; // early out
        // subtypes
        if (ent instanceof EntityMagmaCube) return LAVASLIME;
        if (ent instanceof EntityPigZombie) return PIGZOMBIE;
        if (ent instanceof EntityCaveSpider) return CAVESPIDER;
        if (ent instanceof EntityMooshroom) return REDCOW;
        // night dwellers
        if (ent instanceof EntityZombie) return ZOMBIE;
        if (ent instanceof EntityEnderman) return ENDERMAN;
        if (ent instanceof EntitySkeleton) return SKELLY;
        if (ent instanceof EntityCreeper) return CREEPER;
        // the rest
        if (ent instanceof EntitySlime) return SLIME;
        if (ent instanceof EntitySquid) return SQUID;
        if (ent instanceof EntitySpider) return SPIDER;
        if (ent instanceof EntitySheep) return SHEEP;
        if (ent instanceof EntityVillager) return VILLAGER;
        if (ent instanceof EntitySnowman) return SNOWMAN;
        if (ent instanceof EntityChicken) return CHICKEN;
        if (ent instanceof EntityPig) return PIG;
        if (ent instanceof EntityCow) return COW;
        if (ent instanceof EntityWolf) return WOLF;
        if (ent instanceof EntityBlaze) return BLAZE;
        if (ent instanceof EntityIronGolem) return GOLEM;
        if (ent instanceof EntityOcelot) return OCELOT;
        // rare or junk
        if (ent instanceof EntityGhast) return GHAST;
        if (ent instanceof EntityPlayer) return PLAYER;
        if (ent instanceof EntitySilverfish) return SILVERFISH;
        if (ent instanceof EntityGiantZombie) return ZOMBIE;
        if (ent instanceof EntityDragon) return DRAGON;
        // unknown living
        return LIVING;
    }


    // settings
    @Override
    public boolean needsSave() {
    	return needsSave;
    }
    
    @Override
	public void loadSettings(File settingsFile) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(settingsFile));
			String sCurrentLine;
			while ((sCurrentLine = in.readLine()) != null) {
				String[] curLine = sCurrentLine.split(":");
				if(curLine[0].equals("Ore Key"))
					keyBindOreToggle.setKeyCode(Keyboard.getKeyIndex(curLine[1]));
				else if(curLine[0].equals("Monster Highlight Key"))
					keyBindMonsterToggle.setKeyCode(Keyboard.getKeyIndex(curLine[1]));
				else if(curLine[0].equals("See Through Key"))
					keyBindSeeThrough.setKeyCode(Keyboard.getKeyIndex(curLine[1]));
				else if(curLine[0].equals("Ore Colors")) {
					String val[];
					val = curLine[1].split("[\\t ]*,[\\t ]*");
					for (int i=0;i<val.length;i++) {
						String got[] = val[i].split("/");
						if (got.length == 2) {
							Mark color = new Mark();
							int id = 0;
							if (got[0].matches("^\\d+$")) {
								id = Integer.parseInt(got[0]);
							} 
							else if (got[0] != null && got[0] != ""){
								id = Block.getIdFromBlock(Block.getBlockFromName(got[0]));
							}
							if (id>0 && id<cheatOres.length && color.loadColor(got[1])) {
								cheatOres[id] = color;
							}
						} 
						else {
							Common.err("error: config.txt @ optCheatOres - invalid ore/color pair \""+val[i]+"\"");
						}
					}
				}
			}
			in.close();
		}
		catch (Exception e) {
		}
		oldOreToggleCode = keyBindOreToggle.getKeyCode();
		oldMonsterToggleCode = keyBindMonsterToggle.getKeyCode();
	}

    @Override
	public void saveSettings(PrintWriter out) {
		out.println("Ore Key:" + Common.getKeyDisplayString(keyBindOreToggle.getKeyCode()));
		out.println("Monster Highlight Key:" + Common.getKeyDisplayString(keyBindMonsterToggle.getKeyCode()));
		out.println("See Through Key:" + Common.getKeyDisplayString(keyBindSeeThrough.getKeyCode()));
	}
	

}
