package me.imtoggle.modernf3.mixin;

import me.imtoggle.modernf3.UtilKt;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {

    @Inject(method = "setGameType", at = @At("HEAD"))
    private void gameModeChange(WorldSettings.GameType type, CallbackInfo ci) {
        UtilKt.gameModeChange(type);
    }
}