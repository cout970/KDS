package net.fabricmc.example.mixin;

import kds.internal.ScriptingEngine;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {

    @Inject(at = @At("HEAD"), method = "render(Z)V")
    private void renderTick(CallbackInfo info) {
        ScriptingEngine.INSTANCE.tick();
    }
}
