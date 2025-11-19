//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Null\Downloads\mcp_stable-22-1.8.9"!

package com.morekilleffects;

import client.features.modules.ModuleManager;
import client.features.modules.render.MoreKillEffects;
import com.morekilleffects.killEffect.KillEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class Event {
   private SkywarsKillEffect instance;
   private String[] triggers;
   private final String[] defaultTriggers = new String[]{"killed"};

   public Map playerList = new HashMap();

   public Event(SkywarsKillEffect instance) {
      this.instance = instance;}

   public void onChat(String event) {
      if (ModuleManager.getModulebyClass(MoreKillEffects.class).isEnabled()) {
         String[] var3 = this.defaultTriggers;
         int var4 = var3.length;

         for (int var5 = 0; var5 < var4; ++var5) {
            String trigger = var3[var5];
            String[] splitMessage = event.split(" ");
            if (event.contains(trigger) && (event.contains(this.instance.mc.player.getName().getLiteralString()))) {
               KillEffect killEffect = this.instance.getKillEffectManager().getCurrentKillEffect();
               if (killEffect != null) {
                  // killEffect.play((Entity)this.playerList.get());               }

                  for (AbstractClientPlayerEntity entity : MinecraftClient.getInstance().world.getPlayers()) {
                     if (entity.getName().getString().contains(splitMessage[2])) {
                        killEffect.play(entity);
                     }
                  }
               }
            }

         }
      }


   }
}
