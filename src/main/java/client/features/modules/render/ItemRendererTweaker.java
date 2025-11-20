package client.features.modules.render;

import client.features.modules.Module;
import client.settings.NumberSetting;

public class ItemRendererTweaker extends Module {
    public static NumberSetting mainHandItemScale;
    public static NumberSetting mainHandX;
    public static NumberSetting mainHandY;
    public static NumberSetting mainHandPositiveX;
    public static NumberSetting mainHandPositiveY;
    public static NumberSetting mainHandPositiveZ;
    public ItemRendererTweaker() {
        super("ItemRendererTweaker", 0,Category.RENDER);
    }
    @Override
    public void init()
    {
        super.init();
        mainHandItemScale = new NumberSetting("ItemScale", 0, -5 , 5,0.1);
        mainHandX = new NumberSetting("MainHandX", 0, -5 , 5,0.1);
        mainHandY = new NumberSetting("MainHandY", 0.5, 0 , 1,0.1);
        mainHandPositiveX = new NumberSetting("MainHandPositiveX", 0, -50 , 50,0.1);
        mainHandPositiveY = new NumberSetting("MainHandPositiveY", 0, -50, 50,0.1);
        mainHandPositiveZ = new NumberSetting("MainHandPositiveZ", 0, -50, 50,0.1);
        addSetting(mainHandItemScale,mainHandX,mainHandY,mainHandPositiveX,mainHandPositiveY,mainHandPositiveZ);
    }
}
