//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Null\Downloads\mcp_stable-22-1.8.9"!

package com.morekilleffects.killEffect.killeffects;

import com.morekilleffects.SkywarsKillEffect;
import com.morekilleffects.killEffect.KillEffect;
import com.morekilleffects.killEffect.Location;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class BloodExplosion extends KillEffect {
   private Random rand = new Random();

   public BloodExplosion(SkywarsKillEffect instance) {
      super(instance, "BloodExplosion");
   }

   public void play(Entity location) {
      this.playEffect(location);
   }

   private void playEffect(Entity location) {
      this.playStepSound(location);
      this.playBlood(location);
   }
   private void playBlood(Entity target) {

      Location location = new Location(target.getX(), target.getY(), target.getZ());
      Box axisAlignedBB = target.getBoundingBox();
      int minX = this.floor_double(axisAlignedBB.minX);
      int minY = this.floor_double(axisAlignedBB.minY);
      int minZ = this.floor_double(axisAlignedBB.minZ);

      // Alternative: If you want block-breaking particles, use:
      BlockStateParticleEffect bloodParticle = new BlockStateParticleEffect(
          ParticleTypes.BLOCK,
         Blocks.REDSTONE_BLOCK.getDefaultState()
    );

      for (int i = 0; i < 200; ++i) {
         double x = randamDouble(true);
         double y = randamDouble(false) * 2.0;
         double z = randamDouble(true);

         double d0 = location.x + (location.x + x - minX + 0.5) / (i % 17);
         double d1 = location.y + (location.y + y - minY + 0.5) / (i % 17);
         double d2 = location.z + (location.z + z - minZ + 0.5) / (i % 17);

         // Add particle with motion
         MinecraftClient.getInstance().world.addParticle(
                 bloodParticle,
                 location.x + x,
                 location.y + y,
                 location.z + z,
                 (d0 - location.x - 0.5),
                 (d1 - location.y - 0.5),
                 (d2 - location.z - 0.5)
         );
      }
   }

   private void playStepSound(Entity target) {
      Location location = new Location(target.getX(), target.getY(), target.getZ());
      MinecraftClient client = MinecraftClient.getInstance();

      // Redstone Block (ID 152) → Blocks.REDSTONE_BLOCK
      BlockSoundGroup soundGroup = Blocks.REDSTONE_BLOCK.getStateWithProperties(Blocks.REDSTONE_BLOCK.getDefaultState()).getSoundGroup();

      client.getSoundManager().play(PositionedSoundInstance.master(
              soundGroup.getBreakSound(), // use getStepSound() for walk sounds
              soundGroup.getVolume() + 10.0f,
              soundGroup.getPitch() * 0.8f
      ));
}

private double randamDouble(boolean minus) {
      double randD = this.rand.nextDouble();
      int flag = this.rand.nextInt(2);
      if (flag == 0 && minus) {
         randD = -randD;
      }

      return randD;
   }

   private int floor_double(double value) {
      int i = (int)value;
      return value < (double)i ? i - 1 : i;
   }
}
