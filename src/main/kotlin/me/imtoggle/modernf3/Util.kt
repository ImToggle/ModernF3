package me.imtoggle.modernf3

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

private var pauseTime = Minecraft.getSystemTime()

private var functionKeys = listOf(20, 25, 30, 31, 32, 33, 35, 48)

private var shouldPause = false

private var hasAction = false

private var currentKey = -1

private var currentState = false

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
    currentKey = key
    currentState = state
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

fun sendMessage() {
    if (!currentState) return
    if (!Keyboard.isKeyDown(61)) return
    if (!functionKeys.contains(currentKey)) return
    val string = when(currentKey) {
        20, 31 -> I18n.format("debug.reload_resourcepacks.message")
        25 -> I18n.format(if (mc.gameSettings.pauseOnLostFocus) "debug.pause_focus.on" else "debug.pause_focus.off")
        30 -> I18n.format("debug.reload_chunks.message")
        33 -> "${I18n.format("options.renderDistance")}: ${mc.gameSettings.renderDistanceChunks}"
        35 -> I18n.format(if (mc.gameSettings.advancedItemTooltips) "debug.advanced_tooltips.on" else "debug.advanced_tooltips.off")
        48 -> I18n.format(if (mc.renderManager.isDebugBoundingBox) "debug.show_hitboxes.on" else "debug.show_hitboxes.off")
        else -> ""
    }
    if (string.isEmpty()) return
    UChat.chat("§l§e${I18n.format("debug.prefix")}§r $string")
}