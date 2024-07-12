package com.pyding.deathlyhallows.client.handler;

import com.pyding.deathlyhallows.network.KeyPacket;
import com.pyding.deathlyhallows.network.NetworkHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
    public static boolean isKeyPressed = false;
    public static boolean isKeyPressed2 = false;

    public boolean isKeyPressed() {
        return isKeyPressed;
    }

    public void setKeyPressed(boolean set) {
        isKeyPressed = set;
    }

    public boolean isKeyPressed2() {
        return isKeyPressed2;
    }

    public void setKeyPressed2(boolean set) {
        isKeyPressed2 = set;
    }

    private static final KeyBinding binding = new KeyBinding("dh.key.wand1", Keyboard.KEY_V, "dh.key.wand");
    private static final KeyBinding binding2 = new KeyBinding("dh.key.broom", Keyboard.KEY_C, "dh.key.wand");

    public static String getKeyDescription() {
        return binding.getKeyDescription();
    }

    public static String getKey2Description() {
        return binding2.getKeyDescription();
    }

    public void register() {
        ClientRegistry.registerKeyBinding(binding);
        ClientRegistry.registerKeyBinding(binding2);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (binding.isPressed()) {
            KeyPacket packet = new KeyPacket(true, 1);
            NetworkHandler.sendToServer(packet);
        }
        if (binding2.isPressed()) {
            KeyPacket packet = new KeyPacket(true, 2);
            NetworkHandler.sendToServer(packet);
        }
    }
}

