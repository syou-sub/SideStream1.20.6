package client.features.modules.misc;

import client.Client;
import client.event.Event;
import client.event.listeners.EventPacket;
import client.event.listeners.EventReceiveMessage;
import client.features.modules.Module;
import client.utils.ChatUtils;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;

import java.util.Objects;

public class NameProtect extends Module
{
	
	public NameProtect()
	{
		super("NameProtect", 0, Category.MISC);
	}
	
	@Override
	public void onEvent(Event<?> e)
	{
        if(e instanceof EventReceiveMessage){
            if(((EventReceiveMessage) e).getMessageString().contains(mc.player.getName().getLiteralString())){
                String temp = ((EventReceiveMessage) e).getMessageString();
                ChatUtils.printChatNoName(temp.replaceAll(String.valueOf(mc.player.getName().getLiteralString()), "\247d" + Client.NAME + "User" + "\247r"));
                e.setCancelled(true);
            }
        }
		super.onEvent(e);
	}
	
}
