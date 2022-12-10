package com.github.et118.lecternvideoplayer.mixin;

import com.github.et118.lecternvideoplayer.VideoPlayer.VideoPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    private static MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(at=@At(value="RETURN"),method="tick")
    public void onTick(CallbackInfo ci) {
        VideoPlayer.tick();
    }

    @Inject(at={@At(value="HEAD")}, method={"sendChatMessage"}, cancellable=true)
    public void onChatMessage(String message, @Nullable Text preview, CallbackInfo ci) {
        if (message.startsWith(".play")) {
            List<String> words = new ArrayList<String>(List.of(message.split(" ")));
            if(words.size() <= 1) {
                mc.inGameHud.getChatHud().addMessage(Text.of("No song name specified"));
                ci.cancel();
                return;
            }
            words.remove(0);
            VideoPlayer.play(String.join("", words));
            ci.cancel();
            return;
        }
        if (message.equals((Object)".settings")) {
            //TODO Add settings menu
            ci.cancel();
        }
    }
}