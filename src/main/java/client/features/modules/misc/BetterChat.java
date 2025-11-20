package client.features.modules.misc;

import client.features.modules.Module;
import client.settings.NumberSetting;
import client.utils.ChatUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class BetterChat extends Module {
    public final IntList lines = new IntArrayList();
    public NumberSetting longerChatLines;
    public BetterChat() {
        super("BetterChat", 0, Category.MISC);
    }
    public void init(){
        super.init();
        longerChatLines = new NumberSetting("ExtraLines", 1000,0,1000,1);
        addSetting(longerChatLines);
    }
    public void removeLine(int index) {
        if (index >= lines.size()) {
                ChatUtils.printChat("Issue detected with the anti-spam system! Likely a compatibility issue with another mod. Disabling anti-spam to protect chat integrity.");
              toggle();
            return;
        }

        lines.removeInt(index);
    }
    public int getExtraChatLines() {
        return (int) longerChatLines.getValue();
    }
}
