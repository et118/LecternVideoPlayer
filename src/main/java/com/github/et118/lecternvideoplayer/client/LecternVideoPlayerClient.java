package com.github.et118.lecternvideoplayer.client;

import com.github.et118.lecternvideoplayer.VideoPlayer.VideoPlayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class LecternVideoPlayerClient implements ClientModInitializer {
    private static KeyBinding keyBinding;
    @Override
    public void onInitializeClient() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.lecternvideoplayer.interrupt", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_ESCAPE,"category.lecternvideoplayer.videocontrols"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(keyBinding.wasPressed()) VideoPlayer.stop();
        });
    }
}
