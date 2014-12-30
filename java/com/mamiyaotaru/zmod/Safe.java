package com.mamiyaotaru.zmod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.SpawnerAnimals;

import org.lwjgl.input.Keyboard;

import com.mamiyaotaru.zmod.utils.GLShim;
import com.mamiyaotaru.zmod.utils.MutableBlockPos;

public class Safe implements IMod {
    private String tagSafe;
    private Mark optSafeDangerColor, optSafeDangerColorSun;
    private boolean optSafeShowWithSun = true;
    private int optSafeLookupRadius = 16;
    private final int safeMax = 2048;
    private Mark safeMark[];
    private int safeCur = 0;
    private int safeUpdate = 0;
    
    private boolean enabled = false;
    private boolean ghostMode = false;
    
    private static Safe instance;
    private String tag = "";
    
    private WorldClient map;
    private MutableBlockPos blockPos = new MutableBlockPos(0, 0, 0);
    
    public KeyBinding keyBindToggle = new KeyBinding("Safe", Keyboard.KEY_L, "ZMod");
    public KeyBinding keyBindGhostModeToggle = new KeyBinding("Safe Ghost Mode", Keyboard.KEY_NONE, "ZMod");
    public KeyBinding[] keyBindings = new KeyBinding[] {keyBindToggle, keyBindGhostModeToggle};
    
    private int oldToggleCode;
    private int oldGhostModeToggleCode;
    public boolean needsSave = false;
    
    public Safe() {
    	instance = this;
    }
    
	@Override
	public void init(Minecraft minecraft) {
        safeMark = new Mark[safeMax];
        enabled = false;
        ghostMode = false;
        optSafeDangerColor = new Mark(0xff0000);
        optSafeDangerColorSun = new Mark(0xdddd00);
        tagSafe = "safe";
		ArrayList<KeyBinding> tempBindings = new ArrayList<KeyBinding>();
		tempBindings.addAll(Arrays.asList(minecraft.gameSettings.keyBindings));
		tempBindings.addAll(Arrays.asList(keyBindings));
		minecraft.gameSettings.keyBindings = tempBindings.toArray(new KeyBinding[tempBindings.size()]);
    }
	
	@Override
	public String getTag() {
		if (enabled && ghostMode)
			tag = "s(g) ";
		else if (enabled)
			tag = "s ";
		else
			tag = "";
		return tag;
	}
    
	@Override
	public void onTickInGame(Minecraft minecraft) {
		map = minecraft.theWorld;
		if (minecraft.currentScreen == null && keyBindToggle.isPressed()) {
			enabled = !enabled;
		}
		if (minecraft.currentScreen == null && keyBindGhostModeToggle.isPressed()) {
			ghostMode = !ghostMode;
		}
		if (oldToggleCode != keyBindToggle.getKeyCode() || oldGhostModeToggleCode != keyBindGhostModeToggle.getKeyCode()) {
			oldToggleCode = keyBindToggle.getKeyCode();
			oldGhostModeToggleCode = keyBindGhostModeToggle.getKeyCode();
			needsSave = true;
		}
	}
    
/*    private void optionsModSafe() { // TODO rewrite to use my own saving/loading of options
        boolean wasEnabled = modSafeEnabled;
        modSafeEnabled = ZMod.getSetBool(modSafeEnabled, "modSafeEnabled", false,     "Enable Safe mod");
        if (wasEnabled != modSafeEnabled) {
            if (modSafeEnabled && !modSafeActive) initModSafe(); 
            if (!modSafeEnabled && modSafeActive) quitModSafe();
        }
        keySafeShow = ZMod.getSetBind(keySafeShow, "keySafeShow",                  Keyboard.KEY_L, "Show / hide un-safe markers");
        keySafeGhost = ZMod.getSetBind(keySafeGhost, "keySafeGhost",                  Keyboard.KEY_K, "Toggle show marks through walls");
        optSafeShowWithSun = ZMod.getSetBool(optSafeShowWithSun, "optSafeShowWithSun", true, "Mark 'safe at midday' differently");
        optSafeLookupRadius = ZMod.getSetInt(optSafeLookupRadius, "optSafeLookupRadius", 16, 0, 64, "Un-safe lookup radius");
    }*/
    
	@Override
	public void drawMod(float x, float y, float z, float frameDelta) {
        if (!enabled) 
        	return;
        if (--safeUpdate<0) {
            safeUpdate = 16;
            reCheckSafe(Common.fix(x), Common.fix(y), Common.fix(z));
        }
        if (ghostMode) 
        	GLShim.glDisable(GLShim.GL_DEPTH_TEST);
        else           
        	GLShim.glEnable( GLShim.GL_DEPTH_TEST);
        GLShim.glDisable(GLShim.GL_TEXTURE_2D);
        GLShim.glDisable(GLShim.GL_BLEND);
        GLShim.glDisable(GLShim.GL_FOG);
        GLShim.glBegin(GLShim.GL_LINES);
        for (int i = 0; i < safeCur; ++i) {
            Mark got = safeMark[i];
            int r,g,b;
            if (got.r==1) {
                r = optSafeDangerColorSun.r; 
                g = optSafeDangerColorSun.g; 
                b = optSafeDangerColorSun.b;
            } 
            else {
                r = optSafeDangerColor.r; 
                g = optSafeDangerColor.g; 
                b = optSafeDangerColor.b;
            }
            GLShim.glColor3ub(r,g,b);
            float mx = got.x - x; 
            float my = got.y - y; 
            float mz = got.z - z;
            GLShim.glVertex3f(mx+0.4f,my,mz+0.4f); 
            GLShim.glVertex3f(mx-0.4f,my,mz-0.4f);
            GLShim.glVertex3f(mx+0.4f,my,mz-0.4f); 
            GLShim.glVertex3f(mx-0.4f,my,mz+0.4f);
        }
        GLShim.glEnd();
    }
        
    private boolean emptySpaceHere(int pX, int pY, int pZ) {
        double x = pX + 0.5, y = pY, z = pZ + 0.5;
        //double r = 0.3, h = 1.8; // skeleton size
        //double r = 0.35, h = 0.5; // cave spider size
        double r = 0.3, h = 0.5; // hybrid size
        AxisAlignedBB aabb = new AxisAlignedBB(x - r, y, z - r, x + r, y + h, z + r);
        return map.func_147461_a/*getCollidingBlockBounds*/(aabb).isEmpty() && !map.isAnyLiquid(aabb);
    }
    
    private boolean couldSpawnHere(int x, int y, int z) {
        try {
            return y >= 0
                && getBlockLightLevel(x,y,z) < 8
                && SpawnerAnimals.func_180267_a/*canCreatureTypeSpawnAtLocation*/(EntityLiving.SpawnPlacementType.ON_GROUND, map, blockPos.withXYZ(x,y,z))
                && emptySpaceHere(x,y,z);
        } 
        catch (Exception e) {
            return false;
        }
    }

    private void reCheckSafe(int pX, int pY, int pZ) {
        safeCur = 0;
        for (int x = pX-optSafeLookupRadius; x <= pX+optSafeLookupRadius; ++x)
        for (int y = pY-optSafeLookupRadius; y <= pY+optSafeLookupRadius; ++y)
        for (int z = pZ-optSafeLookupRadius; z <= pZ+optSafeLookupRadius; ++z) {
            if (couldSpawnHere(x,y,z)) {
                safeMark[safeCur] = new Mark(x,y,z, optSafeShowWithSun && (getSkyLightLevel(x,y,z) > 7));
                ++safeCur;
                if (safeCur == safeMax) return;
            }
        }
    }
    
    private int getBlockLightLevel(int x, int y, int z) {
        return map.getChunkFromBlockCoords(blockPos.withXYZ(x, y, z)).getLightFor(EnumSkyBlock.BLOCK, blockPos);
    }
    private int getSkyLightLevel(int x, int y, int z) {
        return map.getChunkFromBlockCoords(blockPos.withXYZ(x, y, z)).getLightFor(EnumSkyBlock.SKY, blockPos);
    }
    //private int getRealLightLevel(int x, int y, int z) {
    //    return map.getChunkFromBlockCoords(blockPos.withXYZ(x, y, z)).getBlockLightValue(x & 15, y, z & 15, 0);
    //}

    
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
				if(curLine[0].equals("Safe Key"))
					keyBindToggle.setKeyCode(Keyboard.getKeyIndex(curLine[1]));
				else if(curLine[0].equals("Safe Ghost Mode Key"))
					keyBindGhostModeToggle.setKeyCode(Keyboard.getKeyIndex(curLine[1]));
			}
			in.close();
		}
		catch (Exception e) {
		}
		oldToggleCode = keyBindToggle.getKeyCode();
		oldGhostModeToggleCode = keyBindGhostModeToggle.getKeyCode();
	}

    @Override
	public void saveSettings(PrintWriter out) {
		out.println("Safe Key:" + Common.getKeyDisplayString(keyBindToggle.getKeyCode()));
		out.println("Safe Ghost Mode Key:" + Common.getKeyDisplayString(keyBindGhostModeToggle.getKeyCode()));
	}
	

    
}
