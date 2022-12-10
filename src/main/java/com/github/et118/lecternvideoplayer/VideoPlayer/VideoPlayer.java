package com.github.et118.lecternvideoplayer.VideoPlayer;

import com.github.et118.lecternvideoplayer.mixin.ClientWorldInvoker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.network.packet.c2s.play.ButtonClickC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VideoPlayer {
    private static MinecraftClient mc = MinecraftClient.getInstance();
    public static boolean isPlaying = false;
    private static Video playingVideo;
    private static LecternBlockEntity lecternBlockEntity;
    private static PendingUpdateManager pendingUpdateManager;
    public static int syncId;
    public static boolean stopScreen = false;
    public static void tick() {
        if(!isPlaying) return;
        Vec3d pos = new Vec3d(lecternBlockEntity.getPos().getX(),lecternBlockEntity.getPos().getY(),lecternBlockEntity.getPos().getZ());
        BlockHitResult block = new BlockHitResult(pos, Direction.UP,new BlockPos(lecternBlockEntity.getPos()),false);
        interactBlock(block);
        syncId++;
        mc.getNetworkHandler().sendPacket((Packet)new ButtonClickC2SPacket(syncId,3));//mc.player.currentScreenHandler.syncId+1,3));
        List<String> pages = new ArrayList<>();
        String frame = playingVideo.getNextFrame();
        if(frame==null) {
            stop();
        } else {
            pages.add(frame);
        }
        mc.getNetworkHandler().sendPacket((Packet)new BookUpdateC2SPacket(0, pages, Optional.empty()));
        interactBlock(block);
    }

    public static void play(String name) {
        if(isPlaying) stop();
        Runnable loadingThread = new Runnable() {
            @Override
            public void run() {
                try {
                    playingVideo = Video.loadFromFile(name);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                lecternBlockEntity = findNearbyLectern();
                if(lecternBlockEntity == null) {
                    mc.inGameHud.getChatHud().addMessage(Text.of("No lectern is close enough"));
                    return;
                }
                if(playingVideo != null) {
                    mc.inGameHud.getChatHud().addMessage(Text.of("Loaded song successfully"));
                } else {
                    mc.inGameHud.getChatHud().addMessage(Text.of("No song with the specified name was found"));
                    return;
                }
                pendingUpdateManager = ((ClientWorldInvoker)mc.world).invokeGetPendingUpdateManager();
                isPlaying = true;
                syncId = 0;
                stopScreen = true;
            }
        };
        new Thread(loadingThread).start();
    }

    public static void stop() {
        isPlaying = false;
        playingVideo = null;
        lecternBlockEntity = null;
        stopScreen = false;
    }

    private static LecternBlockEntity findNearbyLectern() {
        for(int x = 0; x < 10; x++) {
            for(int y = 0; y < 10; y++) {
                for(int z = 0; z < 10; z++) {
                    BlockPos blockPos = mc.player.getBlockPos().add(x-5,y - 5,z-5);
                    Optional<LecternBlockEntity> blockEntity = mc.world.getBlockEntity(blockPos, BlockEntityType.LECTERN);
                    if(blockEntity.isPresent()) return blockEntity.get();
                }
            }
        }
        return null;
    }
    private static void interactBlock(BlockHitResult block) {
        //Packets for calling: mc.interactionManager.interactBlock();
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(0));
        mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND,block,pendingUpdateManager.incrementSequence().getSequence()));
    }
}
