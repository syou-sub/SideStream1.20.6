/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2015 - 2025 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */
package client.mixin.client;

import client.Client;
import client.features.modules.ModuleManager;
import client.features.modules.render.ItemRendererTweaker;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer {

    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow
    private ItemStack offHand;

    @Shadow
    @Final
    private static float EQUIP_OFFSET_TRANSLATE_Y;

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER))
    private void hookRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        boolean isEnabled = ModuleManager.getModulebyClass(ItemRendererTweaker.class).isEnabled();
        if (isEnabled) {
            var isInBothHands = Hand.MAIN_HAND == hand && item.contains(DataComponentTypes.MAP_ID) && offHand.isEmpty();
            if (isInBothHands) {
                matrices.translate(0f, 0f, ItemRendererTweaker.mainHandItemScale.getValue());
            } else if (Hand.MAIN_HAND == hand) {
                liquid_bounce$applyTransformations(matrices, (float) ItemRendererTweaker.mainHandX.getValue(), (float) ItemRendererTweaker.mainHandY.getValue(), (float) ItemRendererTweaker.mainHandItemScale.getValue(), (float) ItemRendererTweaker.mainHandPositiveX.getValue(), (float) ItemRendererTweaker.mainHandPositiveY.getValue(), (float) ItemRendererTweaker.mainHandPositiveZ.getValue());
            }
        }
    }

    @Unique
    private static void liquid_bounce$applyTransformations(MatrixStack matrices, float translateX, float translateY, float translateZ, float rotateX, float rotateY, float rotateZ) {
        matrices.translate(translateX, translateY, translateZ);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotateX));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateY));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotateZ));
    }
}