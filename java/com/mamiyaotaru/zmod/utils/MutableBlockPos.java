package com.mamiyaotaru.zmod.utils;

import net.minecraft.util.BlockPos;

public class MutableBlockPos extends BlockPos {

    public int x;
    public int y;
    public int z;

    public MutableBlockPos(int x, int y, int z) {
        super(0, 0, 0);
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public com.mamiyaotaru.zmod.utils.MutableBlockPos withXYZ(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public void setXYZ(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

}
