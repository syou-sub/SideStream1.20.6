package client.event.listeners;

import client.event.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.util.math.MatrixStack;

@Getter
public class EventRenderGame extends Event<EventRenderGame>
{
	
	@Setter
	float partialTicks;
	MatrixStack stack;
	
	public EventRenderGame(float partialTicks, MatrixStack stack)
	{
		this.partialTicks = partialTicks;
		this.stack = stack;
	}
	
}
