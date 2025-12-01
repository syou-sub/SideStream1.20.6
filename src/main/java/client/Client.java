package client;

import client.alts.AltManager;
import client.command.CommandManager;
import client.config.ConfigManager;
import client.event.Event;
import client.event.listeners.EventMotion;
import client.features.modules.ModuleManager;
import client.mixin.mixininterface.IMinecraftClient;
import client.proxy.Config;
import client.ui.BackgroundManager;
import client.ui.HUDRenderer;
import client.utils.MCTimerUtil;
import client.utils.RotationUtils;
import com.morekilleffects.SkywarsKillEffect;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;

import java.io.File;

public class Client
{
	public static final String NAME = "SideStream";
	public static final String VERSION = "20251120";
	public static HUDRenderer hud2 = new HUDRenderer();
	public static String bgLocation = "client/bg.png";
	public static AltManager altManager;
	@Getter
	public static ConfigManager configManager;
	public static SkywarsKillEffect skywarsKillEffect;
	public static MinecraftClient mc = MinecraftClient.getInstance();
	public static MCTimerUtil timerUtil = new MCTimerUtil();
	public static IMinecraftClient IMC = (IMinecraftClient)mc;
	public static final File FOLDER = new File(mc.runDirectory, NAME);
	@Getter
	public static CommandManager commandManager;
	@Getter
	public static ModuleManager moduleManager;
	
	public static void init()
	{
		System.out.println("Starting " + NAME + " Build " + VERSION);
		initManagers();
		initFiles();
		
		System.out.println(NAME + " started successfully!");
	}
	
	public static Event<?> onEvent(Event<?> e)
	{
		if(e instanceof EventMotion) {
			RotationUtils.virtualYaw = ((EventMotion) e).getServerSideAngles()[0];
			RotationUtils.virtualPitch = ((EventMotion) e).getServerSideAngles()[1];
		}
		moduleManager.onEvent(e);
		return e;
	}
	public static void initManagers()
	{
		if(!FOLDER.exists())
		{
			FOLDER.mkdir();
		}
		altManager = new AltManager();
		configManager = new ConfigManager();
		moduleManager = new ModuleManager();
		configManager = new ConfigManager();
	commandManager = new CommandManager();
		
		Config.loadConfig();
	}	
	
	public static void shutdown()
	{}
	
	public static void initFiles()
	{
		BackgroundManager.loadBackgroundImage();
		skywarsKillEffect = new SkywarsKillEffect();
		
	}

}
