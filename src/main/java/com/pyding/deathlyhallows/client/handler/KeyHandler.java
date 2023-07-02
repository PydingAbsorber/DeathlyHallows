package com.pyding.deathlyhallows.client.handler;

import baubles.common.network.PacketHandler;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.pyding.deathlyhallows.DeathHallowsMod;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
    public static boolean isKeyPressed = false;
    public boolean isKeyPressed() {
        return this.isKeyPressed;
    }

    public void setKeyPressed(boolean set) {
        this.isKeyPressed = set;
    }
    private static KeyBinding binding = new KeyBinding("dh.key.wand1", Keyboard.KEY_V, "dh.key.wand");

    public void register() {
        ClientRegistry.registerKeyBinding(binding);
        FMLCommonHandler.instance().bus().register(this);
    }
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (binding.isPressed()) {
            setKeyPressed(true); //fuck this server packets ya ebal
        }
    }
}

