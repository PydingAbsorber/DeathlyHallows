package com.pyding.deathlyhallows.common;

import com.pyding.deathlyhallows.blocks.Screamily;
import com.pyding.deathlyhallows.blocks.Spawnlesia;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.signature.SubTileSignature;

public class RegisterFlowers {
    IIcon icon;
    public static void init(){
        registerFlower(Screamily.class,"screamily");
        registerFlower(Spawnlesia.class,"spawnlesia");
    }
    public static void registerFlower(Class<? extends SubTileEntity> subTile, String name) {
        BotaniaAPI.registerSubTile(name, subTile);
        BotaniaAPI.registerSubTileSignature(subTile, new DHSubTileSignature(name));
        BotaniaAPI.addSubTileToCreativeMenu(name);
    }
}
