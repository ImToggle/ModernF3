package me.imtoggle.modernf3.mixin;

import me.imtoggle.modernf3.UtilKt;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(GuiNewChat.class)
public class GuiNewChatMixin {

    @Redirect(method = "clearChatMessages", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 2))
    private void keepSentMessages(List<String> instance) {
        if (UtilKt.keepSentMessage) {
            UtilKt.keepSentMessage = false;
            return;
        }
        instance.clear();
    }
}