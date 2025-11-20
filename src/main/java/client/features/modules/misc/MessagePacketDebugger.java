package client.features.modules.misc;

import client.event.Event;
import client.event.listeners.EventPacket;
import client.features.modules.Module;
import client.utils.ChatUtils;

public class MessagePacketDebugger extends Module {
    public MessagePacketDebugger() {
        super("MessagePacketDebugger",0, Category.MISC);
    }
    public void onEvent(Event<?> e){
        if(e instanceof EventPacket){
            if(((EventPacket) e).getPacket().toString().contains("Message")){
                ChatUtils.printChat(((EventPacket) e).getPacket().toString());
            }
        }
    }
}
