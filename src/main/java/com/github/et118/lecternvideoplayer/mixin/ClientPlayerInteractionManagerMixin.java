package com.github.et118.lecternvideoplayer.mixin;

import com.github.et118.lecternvideoplayer.VideoPlayer.Video;
import com.github.et118.lecternvideoplayer.VideoPlayer.VideoPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(at=@At("HEAD"),method="interactBlock",cancellable = true)
    public void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if(VideoPlayer.isPlaying) {
            VideoPlayer.stopScreen = false;
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
