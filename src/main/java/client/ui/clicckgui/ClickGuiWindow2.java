package client.ui.clicckgui;

import client.Client;
import client.features.modules.Module;
import client.settings.*;

import client.utils.ChatUtils;
import client.utils.Colors;
import client.utils.font.Fonts;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static client.utils.RenderingUtils.drawRect;
import static client.utils.RenderingUtils.renderRect;

public class ClickGuiWindow2
{

    MinecraftClient mc = MinecraftClient.getInstance();

    private NumberSetting doubleSetting;
    public Module module;
    private static final int defaultColor = new Color(0, 200, 255,158).getRGB();
    private static final int outlineColor1 = Colors.getColor(0, 0, 0, 50);

    private float x, y, lastX, lastY;
    private boolean dragging = false, expand = true;

    private List<File> configs;
    public int width;
    public int height;

    public ClickGuiWindow2(float x, float y)
    {
        this.x = x;
        this.y = y;
        Client.configManager.initCustomConfigs();
        configs = Client.configManager.getCustomConfigs();

    }

    public void init()
    {
        Client.configManager.initCustomConfigs();
        configs = Client.configManager.getCustomConfigs();
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void render(MatrixStack stack, int mouseX, int mouseY, float delta)
    {
        if(doubleSetting != null)
        {
            doubleSetting.setValue(x, 120, mouseX);
        }

        if(dragging)
        {
            x = mouseX + lastX;
            y = mouseY + lastY;
        }
        renderRect(stack,x - 2, y - 2, x + 122, y + 20, defaultColor);
        renderRect(stack,x - 1, y - 1, x + 121, y + 19, outlineColor1);
        renderRect(stack,x, y, x + 120, y + 18, 0xff262626);
        renderRect(stack,  x - 1, y + 17, x + 121, y + 18,outlineColor1);
        Fonts.font.drawString(stack,"CONFIG", x + 4, y + 4, -1);

        if(!expand)
        {
            return;
        }

        float currentY = y + 18;
        for (File configFile : configs) {
            drawRect(stack, defaultColor, x - 2, currentY, x + 122,
                    currentY + 20);
            drawRect(stack, outlineColor1, x - 1, currentY, x + 121,
                    currentY + 19);
            Fonts.font.drawString(stack, configFile.getName(),
                    x + 116 - Fonts.font.getStringWidth(configFile.getName()),
                    currentY + 4, -1);
            currentY += 18;

        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button)
    {
        if(ClickUtil.isHovered(x, y, 140, 18, mouseX, mouseY))
        {
            // default width 140 height 18
            if(button == 0)
            {
                lastX = (float)(x - mouseX);
                lastY = (float)(y - mouseY);
                dragging = true;
            }else
            {
                expand = !expand;
            }
            return;
        }

        if(!expand)
        {
            return;
        }

        double currentY = y + 18;
        for (File configFile : configs) {
            if (ClickUtil.isHovered2(x - 2, currentY, x + 122, currentY + 20,
                    mouseX, mouseY)) {
                if (button == 0) {
                        if(Client.getConfigManager().loadConfig(configFile.getName())) {
                            Client.configManager.initCustomConfigs();
                            ChatUtils.printChat("Successfully loaded " + configFile.getName() + " config.");
                        } else {
                            ChatUtils.printChat("Failed to load " + configFile.getName() + " config.");
                        }
                } else{
                    if(Client.getConfigManager().saveConfig(configFile.getName())) {
                        Client.configManager.initCustomConfigs();
                        ChatUtils.printChat("Successfully saved " + configFile.getName() + " config.");
                    } else {
                        ChatUtils.printChat("Failed to save " + configFile.getName() + " config.");
                    }
                }
                return;
            }
            currentY += 18;
        }
    }


    public void mouseReleased(double mouseX, double mouseY, int button)
    {
        dragging = false;
        doubleSetting = null;
    }

    public void mouseScrolled(double mouseX, double mouseY, double amount,
                              double verticalAmount)
    {

    }


}
