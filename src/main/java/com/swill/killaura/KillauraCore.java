package com.swill.killaura;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class KillauraCore {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private boolean enabled = false;
    private long lastAttack = 0;
    private Entity currentTarget = null;
    
    public void toggle() { 
        enabled = !enabled; 
        if (!enabled) currentTarget = null;
    }
    
    public boolean isEnabled() { return enabled; }
    
    public void update() {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.isSpectator() || mc.player.isCreative()) return;
        
        if (currentTarget == null || !isValidTarget(currentTarget)) {
            currentTarget = getBestTarget();
        }
        
        if (currentTarget != null) {
            attack(currentTarget);
        }
    }
    
    private void attack(Entity target) {
        int delay = 505 + (int)(Math.random() * 20);
        if (System.currentTimeMillis() - lastAttack < delay) return;
        
        float[] rot = RotationHandler.getRotationsToEntity(target);
        if (rot != null) {
            RotationHandler.setSilentRotation(rot);
        }
        
        mc.interactionManager.attackEntity(mc.player, target);
        lastAttack = System.currentTimeMillis();
    }
    
    private PlayerEntity getBestTarget() {
        double range = 3.05;
        PlayerEntity best = null;
        double bestDist = range + 1;
        
        if (mc.world == null) return null;
        
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (!player.isAlive()) continue;
            if (player.isSpectator()) continue;
            if (player.getHealth() <= 0) continue;
            
            double dist = mc.player.distanceTo(player);
            if (dist < range && dist < bestDist) {
                bestDist = dist;
                best = player;
            }
        }
        return best;
    }
    
    private boolean isValidTarget(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;
        LivingEntity living = (LivingEntity) entity;
        if (living == mc.player) return false;
        if (!living.isAlive()) return false;
        if (living.isSpectator()) return false;
        if (living.getHealth() <= 0) return false;
        if (mc.player.distanceTo(living) > 3.3) return false;
        return true;
    }
}
