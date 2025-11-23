package client.features.modules.movement;

import client.event.listeners.EventUpdate;
import client.features.modules.Module;
import client.mixin.mixininterface.IVec3d;
import client.settings.NumberSetting;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;


public class Step extends Module {
    public NumberSetting maxHeight;
    private float prevStepHeight;
    public Step() {
        super("Step", 0, Category.MOVEMENT);
        maxHeight = new NumberSetting("Max Height",1.25,0,1.5,0.1);
        addSetting(maxHeight);
    }

    public void onEnabled() {
        super.onEnabled();
        prevStepHeight = mc.player.getStepHeight();
    }

    public void onUpdate(EventUpdate eventUpdate){
        double height = getMaxSafeHeight();
        if (height > 0) {
            mc.player.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT).setBaseValue(height);
        } else {
            mc.player.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT).setBaseValue(prevStepHeight);
        }
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        mc.player.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT).setBaseValue(prevStepHeight);
    }

    private float getHealth() {
        return mc.player.getHealth() + mc.player.getAbsorptionAmount();
    }

    private boolean isSaferThanWith() {
       return true;
    }

    private double getMaxSafeHeight() {
        double max = maxHeight.getValue();
        double h = 0;
        Box initial = mc.player.getBoundingBox();

        // all of this is to avoid running into crystals which are behind
        // one block when holding a movement key because standing on the
        // near edge of that block is technically safe

        Vec3d inputOffset = mc.player.getRotationVector();
        Vec2f input = mc.player.input.getMovementInput();
        ((IVec3d) inputOffset).meteor$setY(0);
        inputOffset = inputOffset.normalize().multiply(1.2);
        double zdot = inputOffset.z;
        double xdot = inputOffset.x;
        inputOffset = new Vec3d(input.y * xdot + input.x * zdot, 0, input.x * xdot + input.y * zdot);

        for (int i = 1; i < max; i++) {
            mc.player.setBoundingBox(initial.offset(0, i, 0));
            if (!isSaferThanWith()) {
                mc.player.setBoundingBox(initial);
                return h;
            }

            mc.player.setBoundingBox(mc.player.getBoundingBox().offset(inputOffset));
            if (!isSaferThanWith()) {
                mc.player.setBoundingBox(initial);
                return h;
            }
            h += 1;
        }
        mc.player.setBoundingBox(initial.offset(0, max, 0));

        if (isSaferThanWith()) h = max;

        mc.player.setBoundingBox(initial);
        return h;
    }
}
