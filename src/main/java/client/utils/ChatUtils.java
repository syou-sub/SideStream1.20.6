package client.utils;

import client.Client;
import client.mixin.mixininterface.IChatHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class ChatUtils implements MCUtil
{
	public static String name = Client.NAME;
	public static String chatPrefix  = "["+"§b"+name.substring(0, 1).replaceAll(name.substring(0, 1),
			name.substring(0, 1))
			+ name.substring(1).replaceAll(name.substring(1),
			"\247f" + name.substring(1)) + "]";
	
	public static void printChat(String text)
	{
		
		mc.inGameHud.getChatHud().addMessage(Text.of(chatPrefix+ " " + text));
		
	}
    public static void printDebugMessage(String text)
    {

        mc.inGameHud.getChatHud().addMessage(Text.of(chatPrefix+ " [Debug] " + text));

    }
	
	public static void printChatNoName(String text)
	{
		
		mc.inGameHud.getChatHud().addMessage(Text.of((text)));
		
	}
    private static MutableText getCustomPrefix(String prefixTitle, Formatting prefixColor) {
        MutableText prefix = Text.empty();
        prefix.setStyle(prefix.getStyle().withFormatting(Formatting.GRAY));

        prefix.append("[");

        MutableText moduleTitle = Text.literal(prefixTitle);
        moduleTitle.setStyle(moduleTitle.getStyle().withFormatting(prefixColor));
        prefix.append(moduleTitle);

        prefix.append("] ");

        return prefix;
    }
    private static Text getPrefix() {
       return Text.of(chatPrefix);
    }
    public static void sendMsg(int id, @Nullable String prefixTitle, @Nullable Formatting prefixColor, Text msg) {
        if (mc.world == null) return;

        MutableText message = Text.empty();
        message.append(getPrefix());
        if (prefixTitle != null) message.append(getCustomPrefix(prefixTitle, prefixColor));
        message.append(msg);

      //  if (!Config.get().deleteChatFeedback.get()) id = 0;

        ((IChatHud) mc.inGameHud.getChatHud()).meteor$add(message, id);
    }

	
	public static void sendChat(String text)
	{
        Objects.requireNonNull(mc.player).sendMessage(Text.of(text));
	}
}
