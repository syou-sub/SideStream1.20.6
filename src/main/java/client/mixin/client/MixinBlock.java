/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package client.mixin.client;

import client.features.modules.ModuleManager;
import client.features.modules.movement.NoSlowdown;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;

@Mixin(Block.class)
public abstract class MixinBlock implements ItemConvertible
{
    @Inject(at = @At("HEAD"),
            method = "getVelocityMultiplier()F",
            cancellable = true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir)
    {
        if(ModuleManager.getModulebyClass(NoSlowdown.class).isEnabled())
            return;

        if(cir.getReturnValueF() < 1)
            cir.setReturnValue(1F);
    }
}