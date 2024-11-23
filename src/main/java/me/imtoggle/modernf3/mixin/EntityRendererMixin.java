package me.imtoggle.modernf3.mixin;

import me.imtoggle.modernf3.renderer.ChunkBorderRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V", ordinal = 2, shift = At.Shift.AFTER))
    private void renderChunkBorder(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        ChunkBorderRenderer.INSTANCE.render(partialTicks);
    }
}