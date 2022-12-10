package com.github.et118.lecternvideoplayer.mixin;

import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OpenScreenS2CPacket.class)
public interface OpenScreenS2CAccessor {
    @Accessor
    int getSyncId();
    @Accessor
    ScreenHandlerType<?> getScreenHandlerId();
}
