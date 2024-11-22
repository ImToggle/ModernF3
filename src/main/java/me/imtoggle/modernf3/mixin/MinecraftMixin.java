package me.imtoggle.modernf3.mixin;

import me.imtoggle.modernf3.UtilKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow public GameSettings gameSettings;

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void onTick(CallbackInfo ci) {
        UtilKt.onTick();
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;setKeyBindState(IZ)V", ordinal = 1))
    private void cancel(int keyCode, boolean pressed) {
        if (UtilKt.shouldPause(keyCode, pressed)) return;
        KeyBinding.setKeyBindState(keyCode, pressed);
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;onTick(I)V", ordinal = 0))
    private void cancel(int keyCode) {
        if (UtilKt.isPausing) return;
        KeyBinding.onTick(keyCode);
    }

    @ModifyConstant(method = "runTick", constant = @Constant(intValue = 61, ordinal = 15))
    private int cancelVanillaCheck(int constant) {
        return 256;
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isPressed()Z", ordinal = 0))
    private void handleMessage(CallbackInfo ci) {
        UtilKt.sendMessage();
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;setOptionValue(Lnet/minecraft/client/settings/GameSettings$Options;I)V"))
    private void renderDistance(GameSettings instance, GameSettings.Options settingsOption, int value) {
        gameSettings.renderDistanceChunks += GuiScreen.isShiftKeyDown() ? -1 : 1;
        gameSettings.renderDistanceChunks = Math.max(Math.min(gameSettings.renderDistanceChunks, (int) GameSettings.Options.RENDER_DISTANCE.getValueMax()), 2);
        gameSettings.saveOptions();
    }

}
