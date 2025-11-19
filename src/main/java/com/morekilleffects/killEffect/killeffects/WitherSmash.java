//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Null\Downloads\mcp_stable-22-1.8.9"!

package com.morekilleffects.killEffect.killeffects;

import com.morekilleffects.SkywarsKillEffect;
import com.morekilleffects.killEffect.KillEffect;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WitherSmash extends KillEffect {
   public WitherSmash(SkywarsKillEffect instance) {
      super(instance, "WitherSmash");
   }
   MinecraftClient client = MinecraftClient.getInstance();

   public void play(Entity target) {
      final WitherSkullEntity witherSkull = new WitherSkullEntity(target.getWorld(), MinecraftClient.getInstance().player, target.getX(), target.getY(), target.getZ());
      float yaw = this.getInstance().mc.player.getYaw();
      final double addX = Math.cos(Math.toRadians((double)(yaw + 90.0F))) * 0.15;
      double addY = 0.05;
      final double addZ = Math.sin(Math.toRadians((double)(Math.abs(yaw) + 90.0F))) * 0.15;
      final double tempY = witherSkull.getY();
      this.playShootSound();
      (new Timer()).schedule(new TimerTask() {
         public void run() {
            label11: {
               if (!(witherSkull.getY() - tempY > 20.0)) {
                  double var10003 = witherSkull.getX() + addX;
                  if (WitherSmash.this.getInstance().mc.world.getBlockState(new net.minecraft.util.math.BlockPos((int) var10003, (int) (witherSkull.getY() + 1), (int) (witherSkull.getZ() + addZ))).getBlock() == Blocks.AIR) {
                     break label11;
                  }
               }

               WitherSmash.this.playExplosion(witherSkull);
               witherSkull.damage(null, 10000000);
               this.cancel();
            }

            witherSkull.setPos(witherSkull.getX() + addX, witherSkull.getY() + 0.05, witherSkull.getZ() + addZ);
            witherSkull.setYaw( witherSkull.getYaw() + 3.0F);
            witherSkull.setPitch(witherSkull.getPitch());
            witherSkull.getEntityWorld().addParticle(ParticleTypes.FLAME, witherSkull.getX(), witherSkull.getY(), witherSkull.getZ(), 0.0, 0.0, 0.0);
         }
      }, 1L, 10L);
   }

   private void playShootSound() {
      MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(
              SoundEvents.ENTITY_WITHER_SHOOT,
              1.0F // volume
      ));   }

   private void playExplosion(Entity entity) {
      // Play explosion sound
      client.getSoundManager().play(PositionedSoundInstance.master(
              SoundEvents.ENTITY_GENERIC_EXPLODE, // "random.explode" is now this
              1.0F
      ));

      // Spawn explosion particle (client-side only)
      if (client.world != null) {
         Vec3d pos = entity.getPos();
         ((ClientWorld) client.world).addParticle(
                 ParticleTypes.EXPLOSION,
                 pos.x, pos.y, pos.z,
                 0.0, 0.0, 0.0
         );
      }
   }
}
