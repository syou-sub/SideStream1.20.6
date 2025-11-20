package client.features.modules.misc;

import client.event.Event;
import client.event.listeners.EventPacket;
import client.event.listeners.EventReceiveMessage;
import client.features.modules.Module;
import client.utils.ChatUtils;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class MessageDebugger extends Module
{

    public MessageDebugger()
    {
        super("MessageDebugger", 0, Category.MISC);
    }

    @Override
    public void onEvent(Event<?> e)
    {
        if(e instanceof EventReceiveMessage) {
            if (!((EventReceiveMessage) e).getMessageString().contains(ChatUtils.chatPrefix)) {
                ChatUtils.printDebugMessage("Chat Literal String :" + ((EventReceiveMessage) e).getMessageLiteralString());
                ChatUtils.printDebugMessage("Chat String :" + ((EventReceiveMessage) e).getMessageString());
            }
        }
        super.onEvent(e);
    }

}
