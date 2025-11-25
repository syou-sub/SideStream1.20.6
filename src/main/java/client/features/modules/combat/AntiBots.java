package client.features.modules.combat;

import client.event.Event;
import client.event.listeners.EventPacket;
import client.event.listeners.EventUpdate;
import client.features.modules.Module;
import client.features.modules.ModuleManager;
import client.settings.ModeSetting;
import client.settings.NumberSetting;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TrackedPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

class FlagData {
    int tick = 0;
    Vec3d motion = new Vec3d(0.0, 0.0, 0.0);
    float vl = 0.0f;
    float freqvl = 0.0f;
}
public final class AntiBots extends Module
{
	private Set<UUID> suspectList = new HashSet<>();
private static Set<UUID> botList = new HashSet<>();
private Map<Integer, Long> positionfreqmap = new HashMap<>();
// private Int2IntOpenHashMap vlSet;
private Map<Integer, FlagData> dataSet = new HashMap<>();

	
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
		if( e instanceof EventPacket)
		{
			setupPacketHandler((EventPacket) e);
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
  
	private boolean isADuplicate(GameProfile profile) {
    return mc.getNetworkHandler().getPlayerList().stream()
            .anyMatch(p -> 
                p.getProfile().getName().equals(profile.getName()) &&
                !p.getProfile().getId().equals(profile.getId())
            );
}
	private boolean isGameProfileUnique(GameProfile profile) {
    return mc.getNetworkHandler().getPlayerList().stream()
            .filter(p -> p.getProfile().getId().equals(profile.getId()))
            .count() == 1;
}
	private void setupPacketHandler(EventPacket event) {
    // Assuming you have a packet event system
        Packet<?> packet = event.getPacket();

        if (packet instanceof EntityS2CPacket) {
            EntityS2CPacket entityPacket = (EntityS2CPacket) packet;
            
            if (!entityPacket.isPositionChanged()) {
                return;
            }
            
            Entity target = entityPacket.getEntity(mc.world);
            if (target == null || target.distanceTo(mc.player) > 20) {
                return;
            }
            
            TrackedPosition tpos = new TrackedPosition();
            Vec3d curmot = tpos.withDelta(
                entityPacket.getDeltaX(),
                entityPacket.getDeltaY(),
                entityPacket.getDeltaZ());
            
            int entid = target.getId();
            long curmilli = System.currentTimeMillis();
            FlagData curdata = dataSet.getOrDefault(entid, new FlagData());

            if (positionfreqmap.containsKey(entid)) {
                if ((curdata.motion.y == 0.40625 && curmot.y == -0.40625) ||
                    (curdata.motion.y == 0.419921875 && curmot.y == -0.419921875)) {
                    // chat("haram motion detected|" + entid + "|" + curdata.vl);
                    curdata.vl += 1;
                }
                
                curdata.motion = new Vec3d(curmot.x, curmot.y, curmot.z);
                
                if (curdata.vl >= 1 && curdata.freqvl > 2) {
                    botList.add(target.getUuid());
                    // chat("antibot|" + entid + "|VL: " + curdata.vl + " FVL:" + curdata.freqvl);
                }
                
                if (curmilli - positionfreqmap.get(entid) < 50) {
                    curdata.freqvl += 1;
                } else {
                    float newVL = curdata.freqvl / 2;
                    curdata.freqvl = Math.max(newVL, 0f);
                    // chat("nuhuh|" + entid + "|" + curdata.freqvl);
                }
                
                dataSet.put(entid, curdata);
                positionfreqmap.put(entid, curmilli);
            } else {
                positionfreqmap.put(entid, curmilli);
            }
        }
        
        if (packet instanceof PlayerListS2CPacket) {
            PlayerListS2CPacket playerListPacket = (PlayerListS2CPacket) packet;
            
            for (PlayerListS2CPacket.Entry entry : playerListPacket.getPlayerAdditionEntries()) {
                GameProfile profile = entry.profile();
                if (profile == null) {
                    continue;
                }

                if (entry.latency() < 2 || 
                    (profile.getProperties() != null && !profile.getProperties().isEmpty()) || 
                    isGameProfileUnique(profile)) {
                    continue;
                }

                if (isADuplicate(profile)) {
                    botList.add(entry.profileId());
                    continue;
                }

                suspectList.add(entry.profileId());
            }
        } else if (packet instanceof PlayerRemoveS2CPacket) {
            PlayerRemoveS2CPacket removePacket = (PlayerRemoveS2CPacket) packet;
            
            for (UUID uuid : removePacket.profileIds()) {
                if (suspectList.contains(uuid)) {
                    suspectList.remove(uuid);
                }

                if (botList.contains(uuid)) {
                    botList.remove(uuid);
                }
            }
        }
}
	
	public static boolean isBot(PlayerEntity e)
	{
        PlayerListEntry ple = Objects.requireNonNull(mc.getNetworkHandler()).getPlayerListEntry(e.getUuid());
		if(!(ModuleManager.getModulebyClass(AntiBots.class).isEnabled())) return false;
		return switch(mode.getMode())
		{
            case "Shotbow" -> {
				/* 
                if( ple == null){
                    yield false;
                }
                yield  ple.getLatency() < 30 && hasLeatherArmor(e);
            }
				*/
				yield botList.contains(e.getUuid());
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
