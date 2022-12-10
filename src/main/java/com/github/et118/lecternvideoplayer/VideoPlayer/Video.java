package com.github.et118.lecternvideoplayer.VideoPlayer;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class Video implements Serializable{
    private byte[] videoData;
    private String name;
    private int width;
    private int height;
    private int totalFrames;
    private int currentFrame;

    public Video(String name,int width, int height, byte[] videoData){//List<List<Integer>> videoData) { //TODO Maybe add color
        this.name = name;
        this.width = width;
        this.height = height;
        this.videoData = videoData;
        this.totalFrames = videoData.length / (width*height);
        this.currentFrame = 0;
    }

    public String getNextFrame() {
        if(currentFrame >= totalFrames) {
            return null;
        }
        StringBuilder image = new StringBuilder();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                image.append((Byte.toUnsignedInt(videoData[currentFrame*width*height+x+y*width]) < 128) ? "◘" : "◌");
            }
            image.append("\n");
        }
        currentFrame++;

        return image.toString();
    }

    public void setFrame(int frame) {
        currentFrame = frame;
    }

    public void restart() {
        currentFrame = 0;
    }

    public void saveToFile() throws IOException {
        File folder = new File(FabricLoader.getInstance().getConfigDir().toUri().resolve("Videos"));
        File file = new File(FabricLoader.getInstance().getConfigDir().toUri().resolve("Videos/" + name + ".et118"));
        if(!folder.exists()) folder.mkdirs();
        if(file.exists()) file.delete();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(width);
        dos.writeInt(height);
        dos.writeInt(totalFrames);
        bos.write(videoData,0, videoData.length);
        dos.flush();
        dos.close();
    }

    public static Video loadFromFile(String name) throws IOException, ClassNotFoundException {
        File file = new File(FabricLoader.getInstance().getConfigDir().toUri().resolve("Videos/" + name + ".et118"));
        if(!file.exists()) return null;
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        DataInputStream dis = new DataInputStream(bis);
        int width = dis.readInt();
        int height = dis.readInt();
        int totalFrames = dis.readInt();
        byte[] videoData = new byte[width*height*totalFrames];
        bis.read(videoData,0,width*height*totalFrames);
        dis.close();
        return new Video(name,width,height,videoData);
    }
}
