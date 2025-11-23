package client.features.modules.combat;

import client.event.Event;
import client.event.listeners.EventUpdate;
import client.features.modules.Module;
import client.features.modules.ModuleManager;
import client.settings.ModeSetting;
import client.settings.NumberSetting;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.Collection;
import java.util.Objects;

public final class AntiBots extends Module
{
	
	public AntiBots()
	{
		super("AntiBots", 0, Category.COMBAT);
	}

	public static ModeSetting mode;
    public static NumberSetting ping;
	
	@Override
	public void init()
	{
		super.init();
        ping = new NumberSetting("Ping ", 50, 0,300,10);
		mode = new ModeSetting("Mode ", "Shotbow",
			new String[]{"Hypixel", "Shotbow", "ShotbowTeams", "Ping"});
		addSetting(mode,ping);
	}
	
	public void onEvent(Event<?> e)
	{
		if(e instanceof EventUpdate)
		{
			setTag(mode.getMode());
			switch(mode.getMode())
			{
				case "Hypixel":
				
				break;
				case "Mineplex":
				break;
				case "Shotbow":
				
				break;
			}
		}
		
	}
	
	public static boolean isHypixelBot(PlayerEntity player)
	{
		final String valid =
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_";
		final String name = player.getName().getString();
		
		for(int i = 0; i < name.length(); i++)
		{
			final String c = String.valueOf(name.charAt(i));
			if(!valid.contains(c))
			{
				return true;
			}
		}
		
		if(player.age < 20 && (int)player.getX() == (int)mc.player.getX()
			&& (int)player.getZ() == (int)mc.player.getZ()
			&& player.isInvisible())
			return true;
		return false;
	}
	
	private static boolean isNoArmor(final PlayerEntity entity)
	{
		for(int i = 0; i < 4; ++i)
		{
			
			if(entity.getInventory().getStack(i).getItem() != null)
			{
				return false;
			}
		}
		return true;
	}
    private static boolean hasLeatherArmor(final PlayerEntity entity)
    {
        return entity.getInventory().getStack(1).getItem() == Items.LEATHER_CHESTPLATE || entity.getInventory().getStack(0).getItem() == Items.LEATHER_HELMET || entity.getInventory().getStack(2).getItem() == Items.LEATHER_LEGGINGS || entity.getInventory().getStack(1).getItem() == Items.LEATHER_BOOTS;
    }
    public static boolean isDuplicated (PlayerEntity entity){
        String entityName = entity.getName().getLiteralString();
        return false;

    }
	
	public static boolean isBot(PlayerEntity e)
	{
        PlayerListEntry ple = Objects.requireNonNull(mc.getNetworkHandler()).getPlayerListEntry(e.getUuid());
		if(!(ModuleManager.getModulebyClass(AntiBots.class).isEnabled())) return false;
		return switch(mode.getMode())
		{
            case "Shotbow" -> {
                if( ple == null){
                    yield false;
                }
                yield  ple.getLatency() < 30 && hasLeatherArmor(e);
            }
			case "Hypixel" -> isHypixelBot(e);
			case "ShotbowTeams" -> e.getTeamColorValue() == 16777215;
            case "Ping" -> {
                if( ple == null){
                    yield false;
                }
                yield ple.getLatency() < ping.getValue();
            }
            default -> false;
		};
	}
	
}
