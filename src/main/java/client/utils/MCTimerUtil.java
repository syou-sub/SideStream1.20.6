package client.utils;

import client.mixin.client.MinecraftClientAccessor;
import client.mixin.client.RenderTickCounterAccessor;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
public class MCTimerUtil
{

    @Getter
    private static float timerSpeed = 1.0F;

    public static void setTimerSpeed(float timerSpeed)
	{
    MCTimerUtil.timerSpeed = timerSpeed;
      ((RenderTickCounterAccessor)((MinecraftClientAccessor) MinecraftClient.getInstance()).getRenderTickCounter()).setTickTime(1000.0F / timerSpeed / 20.0F);

      //  Client.IMC.setTickSpeedMultiplier(timerSpeed);
    }

}
