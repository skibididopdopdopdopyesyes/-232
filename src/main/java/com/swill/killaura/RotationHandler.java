package com.swill.killaura;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;

public class RotationHandler {
    private static float silentYaw = 0;
    private static float silentPitch = 0;
    private static boolean active = false;
    private static int delay = 0;
    
    public static float[] getRotationsToEntity(Entity target) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || target == null) return null;
        
        Vec3d playerPos = mc.player.getEyePos();
        Vec3d targetPos = target.getBoundingBox().getCenter();
        
        double diffX = targetPos.x - playerPos.x;
        double diffY = targetPos.y - playerPos.y;
        double diffZ = targetPos.z - playerPos.z;
        
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, dist));
        
        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }
    
    public static void setSilentRotation(float[] rotations) {
        if (rotations == null || rotations.length < 2) return;
        silentYaw = rotations[0];
        silentPitch = rotations[1];
        delay = 1;
        active = true;
    }
    
    public static void applySilentRotation() {
        if (delay > 0) delay--;
        if (active && delay == 0 && MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.setYaw(silentYaw);
            MinecraftClient.getInstance().player.setPitch(silentPitch);
            active = false;
        }
    }
}
