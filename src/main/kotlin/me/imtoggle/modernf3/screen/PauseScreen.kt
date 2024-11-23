package me.imtoggle.modernf3.screen

import cc.polyfrost.oneconfig.libs.universal.UResolution
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n

class PauseScreen : GuiScreen() {

    override fun doesGuiPauseGame(): Boolean {
        return true
    }

    override fun drawDefaultBackground() {
    }

    override fun drawBackground(tint: Int) {
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawCenteredString(mc.fontRendererObj, I18n.format("menu.paused"), width / 2, (40 / UResolution.scaleFactor).toInt(), -1)
    }
}