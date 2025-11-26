package client.features.modules.combat;

import client.event.Event;
import client.event.listeners.EventRender3D;
import client.event.listeners.EventRenderGame;
import client.features.modules.Module;
import client.settings.BooleanSetting;
import client.settings.ModeSetting;
import client.settings.NumberSetting;
import client.utils.PlayerHelper;
import client.utils.RandomUtils;
import client.utils.RotationUtils;
import client.utils.ServerHelper;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder.Living;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class AimAssist extends Module
{
	
	public static List<LivingEntity> targets = new ArrayList<>();
	public static Entity target = null;
	BooleanSetting ignoreTeamsSetting;
	NumberSetting rangeSetting;
	BooleanSetting targetMonstersSetting;
	BooleanSetting targetAnimalsSetting;
	NumberSetting fov;
	ModeSetting sortmode;
	BooleanSetting ignoreBreaking;
	NumberSetting randomYaw;
	BooleanSetting setPitchSetting;
	public static BooleanSetting clickAim;
		 public static NumberSetting yawSpeed1;
		 public static NumberSetting yawSpeed2;
		 public static NumberSetting pitchSpeed1;
		 public static NumberSetting pitchSpeed2;
		 public static NumberSetting pitchOffset;
		 public static BooleanSetting assistonTargetYaw;

	public AimAssist()
	{
		super("Aim Assist", 0, Category.COMBAT);
	}
	
	@Override
	public void init()
	{
		super.init();
		 yawSpeed1 = new NumberSetting("YawSpeed1", 9.0D, 1.0D, 20.0D, 0.01D);
   yawSpeed2 = new NumberSetting("YawSpeed2", 5.0D, 0.5D, 20D, 0.01D);
   pitchSpeed1 = new NumberSetting("PitchSpeed1", 45.0D, 5.0D, 100.0D, 1.0D);
   pitchSpeed2 = new NumberSetting("PitchSpeed2", 15.0D, 2.0D, 97.0D, 1.0D);
     pitchOffset = new NumberSetting("pitchOffSet (blocks)", 4.0D, Integer.valueOf(-2), Integer.valueOf(2), 0.05D);
		this.targetMonstersSetting =
			new BooleanSetting("Target Monsters", true);
			assistonTargetYaw = new BooleanSetting("Assist on Target Yaw", true);
		this.targetAnimalsSetting = new BooleanSetting("Target Animals", false);
		this.ignoreTeamsSetting = new BooleanSetting("Ignore Teams", true);
		this.rangeSetting = new NumberSetting("Range", 5.0, 3.0, 8.0, 0.1);
		this.fov = new NumberSetting("FOV", 90.0D, 15.0D, 360.0D, 1.0D);
		sortmode = new ModeSetting("SortMode", "Angle", new String[]{"Angle","HurtTime","Distance", "Cycle"});
		ignoreBreaking = new BooleanSetting("Ignore Breaking", true);
		randomYaw = new NumberSetting("Random Yaw", 0.0, 0.0, 10.0, 0.1);
		setPitchSetting = new BooleanSetting("Set Pitch", false);
		clickAim = new BooleanSetting("Click Aim", true);
		addSetting(yawSpeed1,yawSpeed2, pitchSpeed1, pitchSpeed2, clickAim,pitchOffset,assistonTargetYaw, ignoreTeamsSetting,
			rangeSetting, targetAnimalsSetting, targetMonstersSetting, fov, sortmode,ignoreBreaking, randomYaw,setPitchSetting);
	}
	
	@Override
	public void onDisabled()
	{
		targets.clear();
		target = null;
	}
	
	@Override
	public void onEvent(Event<?> e)
	{
		if(e instanceof EventRenderGame) {
			setTag(sortmode.getValue()+" " + "["+targets.size()+"]");
			targets = initTargets();
			if (!targets.isEmpty()) {
				target = targets.getFirst();
				if (target == null || !canAssist()) {
					return;
				}
				if (mc.player == null)
					return;
				if(clickAim.isEnabled() && !mc.options.attackKey.isPressed()) {
					return;
				}
 double diff = RotationUtils.getWrappedYawEntity(target);
			//	float diff = calculateYawChangeToDst(target);
				
			//	float aimSpeed = RandomUtils.nextFloat((float) yawSpeed1.getValue(), (float) yawSpeed2.getValue());
/* 
				if (diff < -6) {
					aimSpeed -= diff / 12f;
					float newYaw = mc.player.getYaw(tickDelta) - aimSpeed;
			newYaw -= RandomUtils.nextFloat(-((float) randomYaw.value), ((float) randomYaw.value));
					newYaw = angleStep(newYaw);
					newYaw = MathHelper.wrapDegrees(newYaw);
					newYaw = MathHelper.clamp(newYaw, -90f, 90f);
					newYaw = RotationUtils.getFixedSensitivityAngle(mc.player.getYaw(), newYaw);
					mc.player.setYaw(newYaw);
				} else if (diff > 6) {
					aimSpeed += diff / 12f;
					float newYaw = mc.player.getYaw() + aimSpeed;
				    newYaw += RandomUtils.nextFloat(-((float) randomYaw.value), ((float) randomYaw.value));
					newYaw = angleStep(newYaw);
					newYaw = MathHelper.wrapDegrees(newYaw);
					newYaw = MathHelper.clamp(newYaw, -90f, 90f);
					newYaw = RotationUtils.getFixedSensitivityAngle(mc.player.getYaw(), newYaw);
					mc.player.setYaw(newYaw);
				}



				 if (Math.abs(diff) > 1.0F) {
					 float newYaw = mc.player.getYaw() + (diff > 0 ? aimSpeed : -aimSpeed);
					 newYaw += RandomUtils.nextFloat(-((float) randomYaw.getValue()), ((float) randomYaw.getValue()));
					 newYaw = angleStep(newYaw);
					 newYaw = MathHelper.wrapDegrees(newYaw);
					 newYaw = MathHelper.clamp(newYaw, -90f, 90f);
					 newYaw = RotationUtils.getFixedSensitivityAngle(mc.player.getYaw(), newYaw);
					 mc.player.setYaw(newYaw);
				 }
*/
  if ((Math.abs(diff) > 1.0F  &&  assistonTargetYaw.isEnabled()) || Math.abs(diff) > 6.0F) {
                        double fails = diff * (ThreadLocalRandom.current().nextDouble(yawSpeed2.getValue() - 1.47328D, yawSpeed2.getValue() + 2.48293D) / 100.0D);
                       // double var10000 = fails + RandomUtils.random.current().nextDouble(yawSpeed1.getValue() - 4.723847D, yawSpeed1.getValue());
                        float aimSpeed = (float)(-(fails + diff / (101.0D - (double)((float)ThreadLocalRandom.current().nextDouble(yawSpeed1.getValue() - 4.723847D,yawSpeed1.getValue())))));
						float newYaw = mc.player.getYaw() + aimSpeed + RandomUtils.nextFloat(-((float) randomYaw.getValue()), ((float) randomYaw.getValue()));
                        mc.player.setYaw(newYaw);
                     }
				if (setPitchSetting.isEnabled()) {
				 double var10 = RotationUtils.getFixedRotationDifferencePitch(target, (float) pitchOffset.getValue()) * (ThreadLocalRandom.current().nextDouble(pitchSpeed2.getValue() - 1.47328D, pitchSpeed2.getValue() + 2.48293D) / 100.0D);
                        float aimSpeed = (float)(-(var10 + diff / (101.0D - (double)((float)ThreadLocalRandom.current().nextDouble(pitchSpeed1.getValue() - 4.723847D, pitchSpeed1.getValue())))));
                        float newPitch = mc.player.getPitch() + aimSpeed;
					mc.player.setPitch(newPitch);
			}
		}
	}
	}
	
	private boolean canAssist()
	{
		if(mc.currentScreen != null)
		{
			return false;
		}
		if(ignoreBreaking.isEnabled() && mc.interactionManager.isBreakingBlock()){
			return false;
		}
		
		if(mc.player.isUsingItem())
		{
			return false;
		}
		return true;
	}
	
	private List<LivingEntity> initTargets()
	{
		List<LivingEntity> targets = new ArrayList<>();
		targets.clear();
		for(Entity entity : mc.world.getEntities())
		{
			if(entity instanceof Entity && entity != mc.player)
			{
				if(!entity.isAlive() || entity.age < 10)
				{
					continue;
				}
				
				if(PlayerHelper.isInFov(entity, fov.value))
					continue;
				double focusRange =
					mc.player.canSee(entity) ? rangeSetting.value : 3.5;
				if(mc.player.distanceTo(entity) > focusRange)
					continue;
				if(entity instanceof PlayerEntity)
				{
					
					if(ignoreTeamsSetting.enabled
						&& ServerHelper.isTeammate((PlayerEntity)entity))
					{
						continue;
					}
					if(AntiBots.isBot((PlayerEntity)entity))
						continue;
					
					targets.add((LivingEntity)entity);
				}else if(entity instanceof AnimalEntity
					&& targetAnimalsSetting.enabled)
				{
					targets.add((LivingEntity)entity);
				}else if(entity instanceof MobEntity
					&& targetMonstersSetting.enabled)
				{
					targets.add((LivingEntity)entity);
				}
			}
		}

		targets.sort(Comparator.comparingDouble(this::calculateYawChangeToDst));
		targets.sort(Comparator.comparingInt(o -> o.hurtTime));
		 return sortTargets(targets);
	}

	private List<LivingEntity> sortTargets(List<LivingEntity> targets) {
		float yaw = Objects.requireNonNull(mc.player).getYaw();
		String Sort = sortmode.getMode();
		switch (Sort) {
			case "Distance":
				targets.sort((o1, o2) -> {
					double dist1 = Objects.requireNonNull(mc.player).squaredDistanceTo((Entity) o1);
					double dist2 = Objects.requireNonNull(mc.player).squaredDistanceTo((Entity) o2);
					return Double.compare(dist1, dist2);
				});
				break;
			case "Angle":
				targets.sort(Comparator.comparingDouble(RotationUtils::calculateYawChangeToDst));
				break;
			case "Cycle":
				targets.sort(Comparator.comparingDouble(player -> yawDistCycle(player, yaw)));
				break;
			case"HurtTime":
				targets.sort(Comparator.comparingInt(o -> o.hurtTime));
				break;
		}
		return targets;
	}

	private double yawDistCycle(LivingEntity e, float yaw) {
		Vec3d difference = e.getPos().add(0.0D, (e.getEyeHeight(e.getPose()) / 2.0F), 0.0D).subtract(Objects.requireNonNull(mc.player).getEyePos());
		return Math.abs(yaw - Math.atan2(difference.getZ(), difference.getX())) % 90.0D;
	}
	
	public float calculateYawChangeToDst(Entity entity)
	{
		double diffX = entity.getX() - Objects.requireNonNull(mc.player).getX();
		double diffZ = entity.getZ() - mc.player.getZ();
		double deg = Math.toDegrees(Math.atan(diffZ / diffX));
		if(diffZ < 0.0 && diffX < 0.0)
		{
			return (float)MathHelper
				.wrapDegrees(-(mc.player.getYaw() - (90 + deg)));
		}else if(diffZ < 0.0 && diffX > 0.0)
		{
			return (float)MathHelper
				.wrapDegrees(-(mc.player.getYaw() - (-90 + deg)));
		}else
		{
			return (float)MathHelper.wrapDegrees(-(mc.player.getYaw()
				- Math.toDegrees(-Math.atan(diffX / diffZ))));
		}
	}
}
