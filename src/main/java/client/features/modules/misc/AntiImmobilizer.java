package client.features.modules.misc;

import client.event.listeners.EventUpdate;
import client.features.modules.Module;
import client.settings.BooleanSetting;
import client.settings.NumberSetting;
import client.utils.ChatUtils;
import client.utils.MoveUtils;
import client.utils.TimeHelper;
import net.minecraft.entity.effect.StatusEffects;

import java.util.Objects;

public class AntiImmobilizer extends Module {
    public boolean said = false;
    public NumberSetting delay;
    public BooleanSetting sendMessage;
    TimeHelper timer = new TimeHelper();
    public AntiImmobilizer() {
        super("AntiImmobilizer", 0, Category.MISC);
    }
    public void init(){
        super.init();
        sendMessage = new BooleanSetting("Send Message", true);
        this.delay = new NumberSetting("Chat Delay", 1000, 1000, 5000, 1000F);
addSetting(delay,sendMessage);
    }
    public void onUpdate(EventUpdate eventUpdate){
        if (mc.player != null && mc.player.getStatusEffect(StatusEffects.SLOWNESS) != null) {
            if(sendMessage.getValue()) {
                sendChat();
            }
            mc.player.removeStatusEffectInternal(StatusEffects.SLOWNESS);
            mc.player.setMovementSpeed((float) MoveUtils.getBaseMoveSpeed());
        } else {
            said = false;
        }
    }
    public void sendChat(){
        if(timer.hasReached(delay.getValue()) &&!said) {
            ChatUtils.sendPlayerMsg("!イモビは嫌いよ～ #SideStream Client. " + Math.random());
            said = true;
            timer.reset();
        }
    }

}
