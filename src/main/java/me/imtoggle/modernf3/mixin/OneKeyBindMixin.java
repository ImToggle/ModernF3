package me.imtoggle.modernf3.mixin;

import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import me.imtoggle.modernf3.UtilKt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(value = OneKeyBind.class, remap = false)
public class OneKeyBindMixin {

    @Shadow @Final protected ArrayList<Integer> keyBinds;

    @Inject(method = "isActive", at = @At("HEAD"), cancellable = true)
    private void cancel(CallbackInfoReturnable<Boolean> cir) {
        if (UtilKt.shouldCancel(keyBinds)) cir.setReturnValue(false);
    }
}