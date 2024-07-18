package com.pyding.deathlyhallows.client.render.multiblock;

import net.minecraft.init.Blocks;

public class ModMultiblocks {
    public static MultiblockSet temp;
    static{
        Multiblock mb = new Multiblock();
        for(int i=-7;i<8;i++){
            for(int j=-7;j<8;j++){
                mb.addComponent(i, 0, j, Blocks.obsidian, 0);
            }
        }
        temp=mb.makeSet();
    }
}
