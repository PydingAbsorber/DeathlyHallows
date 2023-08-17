package com.pyding.deathlyhallows.client;

import com.emoniph.witchery.client.renderer.RenderBlockItem;
import com.pyding.deathlyhallows.DeathHallowsMod;
import com.pyding.deathlyhallows.blocks.VisConverterTile;
import com.pyding.deathlyhallows.client.handler.KeyHandler;
import com.pyding.deathlyhallows.client.render.block.ViscRender;
import com.pyding.deathlyhallows.client.render.entity.AnimaInteritusRender;
import com.pyding.deathlyhallows.client.render.entity.PlayerRender;
import com.pyding.deathlyhallows.client.render.entity.RenderAbsoluteDeath;
import com.pyding.deathlyhallows.client.render.entity.RenderNimbus;
import com.pyding.deathlyhallows.client.render.item.EldenWandRender;
import com.pyding.deathlyhallows.client.render.item.ViscItemRender;
import com.pyding.deathlyhallows.common.CommonProxy;
import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import com.pyding.deathlyhallows.entity.Nimbus;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;

import static com.pyding.deathlyhallows.DeathHallowsMod.elderWand;
import static com.pyding.deathlyhallows.DeathHallowsMod.visc;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        KeyHandler keyHandler = new KeyHandler();
        keyHandler.register();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        FMLCommonHandler.instance().bus().register(new PlayerRender());
        MinecraftForgeClient.registerItemRenderer(elderWand, new EldenWandRender());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(visc), new ViscItemRender());
        RenderingRegistry.registerEntityRenderingHandler(AbsoluteDeath.class,new RenderAbsoluteDeath());
        RenderingRegistry.registerEntityRenderingHandler(Nimbus.class,new RenderNimbus());
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new AnimaInteritusRender());
        this.bindRenderer(VisConverterTile.class,new ViscRender(),new Item[0]);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
    private void bindRenderer(Class clazz, TileEntitySpecialRenderer render, Item ... items) {
        ClientRegistry.bindTileEntitySpecialRenderer(clazz, render);
        Item[] arr$ = items;
        int len$ = items.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Item item = arr$[i$];
            if(item != null) {
                try {
                    MinecraftForgeClient.registerItemRenderer(item, new RenderBlockItem(render, (TileEntity)clazz.newInstance()));
                } catch (IllegalAccessException var9) {
                    ;
                } catch (InstantiationException var10) {
                    ;
                }
            }
        }

    }
}
