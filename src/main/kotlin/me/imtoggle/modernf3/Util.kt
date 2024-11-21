package me.imtoggle.modernf3

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

private var pauseTime = Minecraft.getSystemTime()

private var functionKeys = listOf(20, 25, 30, 31, 32, 33, 35, 48)

private var shouldPause = false

private var hasAction = false

@JvmField
var isPausing = false

fun onTick() {
    if (Keyboard.isKeyDown(61)) {
        pauseTime = Minecraft.getSystemTime() + 400
    }

    val pause = pauseTime > Minecraft.getSystemTime()

    if (pause != shouldPause) {
        shouldPause = pause
        if (!shouldPause) {
            refreshKeyBinds()
        }
    }
}

fun refreshKeyBinds() {
    for (keyCode in functionKeys) {
        KeyBinding.setKeyBindState(keyCode, keyCode < 256 && Keyboard.isKeyDown(keyCode))
    }
}

fun shouldPause(key: Int, state: Boolean): Boolean {
    if (!state && key == 61) {
        if (hasAction) {
            hasAction = false
        } else {
            mc.gameSettings.showDebugInfo = !mc.gameSettings.showDebugInfo
            mc.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown()
            mc.gameSettings.showLagometer = GuiScreen.isAltKeyDown()
        }
    } else if (!hasAction && state && functionKeys.contains(key) && Keyboard.isKeyDown(61)) {
        hasAction = true
    }
    isPausing = shouldPause && functionKeys.contains(key)
    return isPausing && state
}