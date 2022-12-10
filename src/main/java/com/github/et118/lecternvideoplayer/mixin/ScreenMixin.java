package com.github.et118.lecternvideoplayer.mixin;

import com.github.et118.lecternvideoplayer.VideoPlayer.VideoPlayer;
import net.minecraft.block.LecternBlock;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(at=@At("HEAD"),method="keyPressed")
    public void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if(keyCode == 256) {
            if(VideoPlayer.isPlaying) VideoPlayer.stopScreen = true;
        }
    }
}
