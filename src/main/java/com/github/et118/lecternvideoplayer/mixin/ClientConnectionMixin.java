package com.github.et118.lecternvideoplayer.mixin;

import com.github.et118.lecternvideoplayer.VideoPlayer.VideoPlayer;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.WritableBookItem;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at=@At("HEAD"),method = "handlePacket",cancellable = true)
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if(packet.getClass().equals(OpenScreenS2CPacket.class)) { //Allows you to freely move around without opening a screen
            if(((OpenScreenS2CAccessor)packet).getScreenHandlerId().equals(ScreenHandlerType.LECTERN)) {
                VideoPlayer.syncId = ((OpenScreenS2CAccessor)packet).getSyncId();
                if(VideoPlayer.stopScreen) {
                    ci.cancel();
                }
            }
        }
        if(packet.getClass().equals(ScreenHandlerSlotUpdateS2CPacket.class)) { //Removes flickering when opening and closing lectern
            if(!VideoPlayer.stopScreen) {
                if(((ScreenHandlerSlotUpdateS2CPacket)packet).getItemStack().getItem().getClass().equals(AirBlockItem.class)) {
                    ci.cancel();
                }
            }
        }
    }
}
