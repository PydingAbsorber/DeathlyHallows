package com.pyding.deathlyhallows.utils;

import com.pyding.deathlyhallows.entities.EntityNimbus;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketKeyBindings;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class DHKeys {
	
	private static final String KEY_GROUP = "dh.keyGroup";
	private static final DHKeys INSTANCE = new DHKeys();

	public static final KeyBinding
			BROOM = new KeyBinding("dh.key.broom", Keyboard.KEY_C, KEY_GROUP);

	private DHKeys() {

	}

	public static void register() {
		ClientRegistry.registerKeyBinding(BROOM);
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent e) {
		if(BROOM.isPressed()) {
			DHPacketProcessor.sendToServer(new PacketKeyBindings(true, 1));
		}
		if(Minecraft.getMinecraft().thePlayer.ridingEntity instanceof EntityNimbus) {
			boolean sprint = Minecraft.getMinecraft().gameSettings.keyBindSprint.isPressed();
			Minecraft.getMinecraft().thePlayer.getEntityData().setBoolean("DHSprint", sprint);
			DHPacketProcessor.sendToServer(new PacketKeyBindings(sprint, 2));
		}
	}
	
}

