package com.morekilleffects.killEffect;

import com.morekilleffects.SkywarsKillEffect;
import com.morekilleffects.killEffect.killeffects.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KillEffectManager {
   private SkywarsKillEffect instance;
   private List killEffects = new ArrayList();
   private KillEffect currentKillEffect = null;

   public KillEffectManager(SkywarsKillEffect instance) {
      this.instance = instance;
      this.registerKillEffects();
   }

   public void registerKillEffects() {
      this.registerKillEffect(new WitherSmash(this.instance));
      this.registerKillEffect(new BloodExplosion(this.instance));
      this.registerKillEffect(new HeartExplosion(this.instance));
      this.registerKillEffect(new Vaporized(this.instance));
   }

   private void registerKillEffect(KillEffect killEffect) {
      this.killEffects.add(killEffect);
   }

   public KillEffect getKillEffectByName(String name) {
      Iterator var2 = this.killEffects.iterator();

      KillEffect killEffect;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         killEffect = (KillEffect)var2.next();
      } while(!killEffect.getEffectName().equalsIgnoreCase(name));

      return killEffect;
   }

   public String[] getKillEffects() {
      String[] killEffectString = new String[this.killEffects.size()];

      for(int i = 0; i < this.killEffects.size(); ++i) {
         killEffectString[i] = ((KillEffect)this.killEffects.get(i)).getEffectName();
      }

      return killEffectString;
   }

   public void setCurrentKillEffect(KillEffect killEffect) {
      this.currentKillEffect = killEffect;
   }

   public KillEffect getCurrentKillEffect() {
      return this.currentKillEffect == null ? null : this.currentKillEffect;
   }
}
