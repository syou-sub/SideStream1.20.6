package client.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TextVisitFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import client.Client;
import client.features.modules.ModuleManager;
import client.features.modules.misc.NameProtect;

@Mixin(TextVisitFactory.class)
public class MixinTextVisitFactory {
    @ModifyArg(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
            ordinal = 0),
            method = {
                    "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"},
            index = 0)
    private static String adjustText(String text) {
    if(ModuleManager.getModulebyClass(NameProtect.class).isEnabled()) {
        if(MinecraftClient.getInstance().player == null) {
            return text;
        }
        return text.replaceAll( String.valueOf(net.minecraft.client.MinecraftClient.getInstance().player.getName().getLiteralString()),"\247d" + Client.NAME + "User" + "\247r");
    }
    else return text;
      }
}
