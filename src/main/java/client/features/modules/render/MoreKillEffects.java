package client.features.modules.render;

import client.Client;
import client.event.Event;
import client.event.listeners.EventPacket;
import client.event.listeners.EventUpdate;
import client.features.modules.Module;
import client.settings.ModeSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ClientPacketListener;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

public class MoreKillEffects extends Module {
   public static ModeSetting mode;
    public MoreKillEffects()
    {
        super("MoreKillEffects", 0, Category.RENDER);
    }
    @Override
    public void init()
    {
        mode = new ModeSetting("Mode","Vaporized", "BloodExplosion","HeartExplosion","Vaporized","WitherSmash");
addSetting(mode);
    }
    public void onEvent(Event<?> e)
    {
   if(e instanceof EventUpdate){
       Client.skywarsKillEffect.getKillEffectManager().setCurrentKillEffect(Client.skywarsKillEffect.getKillEffectManager().getKillEffectByName(mode.getMode()));

   }
        if(e instanceof EventPacket){
            if(e.isIncoming()){

                if(((EventPacket) e).getPacket() instanceof GameMessageS2CPacket packet){
                    Client.skywarsKillEffect.event.onChat((stripper(packet.content().getString())));
                }
            }
        }
    }
    private final Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

    public String stripper(String input){
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
