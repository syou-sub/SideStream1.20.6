package client.features.modules.misc;

import client.Client;
import client.event.Event;
import client.event.listeners.EventPacket;
import client.event.listeners.EventReceiveMessage;
import client.event.listeners.EventUpdate;
import client.features.modules.Module;
import client.settings.BooleanSetting;
import client.settings.ModeSetting;
import client.settings.NumberSetting;
import client.utils.ChatUtils;
import client.utils.TimeHelper;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.ArrayList;
import java.util.Random;

public class AutoMessage extends Module
{
public String loserName = null;
public NumberSetting delay;
public TimeHelper timer = new TimeHelper();
public ModeSetting mode;
public ModeSetting messageMode;
public BooleanSetting advertise;
public ArrayList<String> loserList = new ArrayList<>();

    public AutoMessage()
    {
        super("AutoMessage", 0, Category.MISC);
    }

    public void init(){
        super.init();
        mode = new ModeSetting("Mode", "Global", "Tell","Global");
        this.delay = new NumberSetting("Message Delay", 1000, 1000, 5000, 1000F);
        messageMode = new ModeSetting("Message Mode" ,"Admin", "Admin");
        advertise = new BooleanSetting("Advertise", true);
        addSetting(delay,mode,messageMode,advertise);
    }
    @Override
    public void onEvent(Event<?> e)
    {
        if(e instanceof EventReceiveMessage) {
            String message = ((EventReceiveMessage) e).getMessageString();
            if (!message.contains(ChatUtils.chatPrefix)) {
                String[] messageArray = null;
               if(message.startsWith(mc.player.getName().getLiteralString()) && message.contains("killed"))
               {
                 messageArray = message.split(" ");
                 String user  = messageArray[2].replaceAll("\\(.+?\\)", "");
               loserName = user;
               loserList.add(user);
               }
            }
        }
        super.onEvent(e);
        if(e instanceof EventUpdate){
            setTag(mode.getMode() + " ["+loserList.size()+"]");
            if(!loserList.isEmpty() && timer.hasReached(delay.getValue())){
               sendMessage(loserList.getFirst());
               loserList.removeFirst();
                timer.reset();
                loserName = null;
            }
        }
    }
    public void sendMessage(String loserName){
        String msgToSay = "";
        if(messageMode.is("Admin")){
            msgToSay = getRandomAdmin();
        } else if(messageMode   .is("")){

        }
       msgToSay = advertise.getValue()? (msgToSay +" "+ " by "+ Client.NAME +" "+ "Client." ): msgToSay;
        if(mode.is("Global")){
            ChatUtils.sendPlayerMsg("!" + loserName+ " "+ msgToSay+".");
        } else if (mode.is("Tell")) {
            ChatUtils.sendPlayerMsg("/tell " + loserName+ " "+ msgToSay+".");
        }

    }
    public String getRandomAdmin() {
        String[] strings = getAdministrators();
        Random random = new Random();
        int randomIndex = random.nextInt(strings.length);
        return strings[randomIndex];
    }

    public String[] getAdministrators()
    {
        return new String[]{"ACrispyTortilla", "ArcticStorm141", "ArsMagia",
                "Captainbenedict", "Carrots386", "DJ_Pedro", "DocCodeSharp",
                "FullAdmin", "Galap", "HighlifeTTU", "ImbC", "InstantLightning",
                "JTGangsterLP6", "Kevin_is_Panda", "Kingey", "Marine_PvP",
                "MissHilevi", "Mistri", "Mosh_Von_Void", "Navarr", "PokeTheEye",
                "Rafiki2085", "Robertthegoat", "Sevy13", "andrew323", "dLeMoNb",
                "lazertester", "noobfan", "skillerfox3", "storm345", "windex_07",
                "AlecJ", "JACOBSMILE", "Wayvernia", "gunso_", "Hughzaz",
                "Murgatron", "SaxaphoneWalrus", "_Ahri", "SakuraWolfVeghetto",
                "SnowVi1liers", "jiren74", "Dange", "Tatre", "Pichu2002",
                "LegendaryAlex", "LaukNLoad", "M4bi", "HellionX2", "Ktrompfl",
                "Bupin", "Murgatron", "Outra", "CoastinJosh", "sabau", "Axyy",
                "lPirlo", "ImAbbyy"};
    }

}
