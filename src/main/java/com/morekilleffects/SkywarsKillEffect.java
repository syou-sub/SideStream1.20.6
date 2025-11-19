package com.morekilleffects;

import com.morekilleffects.killEffect.KillEffectManager;
import net.minecraft.client.MinecraftClient;

public class SkywarsKillEffect {
   public final MinecraftClient mc = MinecraftClient.getInstance();
   private KillEffectManager killEffectManager;
   private boolean enabled = true;
   public Event event;

public SkywarsKillEffect(){
   preInit();
}
   public void preInit() {
      killEffectManager = new KillEffectManager(this);
    event = new Event(this);
   }

   public KillEffectManager getKillEffectManager() {
      return this.killEffectManager;
   }


   public boolean isEnabled() {
      return this.enabled;
   }
}
