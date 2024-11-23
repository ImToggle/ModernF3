package me.imtoggle.modernf3

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import net.minecraft.client.settings.KeyBinding
import net.minecraft.crash.CrashReport
import net.minecraft.util.ReportedException
import org.lwjgl.input.Keyboard
import kotlin.math.ceil

private var pauseTime = Minecraft.getSystemTime()

private var functionKeys = listOf(20, 25, 30, 31, 32, 33, 34, 35, 48)

private var shouldPause = false

private var hasAction = false

private var actionKey = -1

private var crashStart = -1L

private var reportTime = Minecraft.getSystemTime()

@JvmField
var isPausing = false

fun onTick() {
    val currentTime = Minecraft.getSystemTime()

    if (crashStart > 0L) {
        if (Minecraft.getSystemTime() - crashStart >= 10000L) {
            crashStart = -1
            throw ReportedException(CrashReport("Manually triggered debug crash", Throwable()))
        }

        if (currentTime >= reportTime) {
            reportTime = currentTime + 1000
            val time = ceil((10000L - currentTime + crashStart) / 1000f).toInt()
            UChat.chat("§c§l${I18n.format("debug.prefix")}§r ${I18n.format("debug.crash.warning", "$time")}")
        }

        if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
            crashStart = -1L
        }
    } else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
        crashStart = currentTime
        reportTime = currentTime
    }

    if (Keyboard.isKeyDown(61)) {
        pauseTime = currentTime + 400
    }

    val pause = pauseTime > currentTime

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

fun keyPress(key: Int, state: Boolean): Boolean {
    if (!state && key == 61) {
        if (hasAction) {
            hasAction = false
        } else {
            mc.gameSettings.showDebugInfo = !mc.gameSettings.showDebugInfo
            mc.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown()
            mc.gameSettings.showLagometer = GuiScreen.isAltKeyDown()
        }
    } else if (state && functionKeys.contains(key) && Keyboard.isKeyDown(61)) {
        actionKey = key
        hasAction = true
        when(key) {
            34 -> sendDebugMessage(I18n.format(if (ChunkBorderRenderer.toggle()) "debug.chunk_boundaries.on" else "debug.chunk_boundaries.off"))
        }
    }
    isPausing = shouldPause && functionKeys.contains(key)
    return isPausing && state
}

fun handleAction() {
    if (actionKey == -1) return
    val keyCode = actionKey
    actionKey = -1
    if (!functionKeys.contains(keyCode)) return
    val string = when(keyCode) {
        20, 31 -> I18n.format("debug.reload_resourcepacks.message")
        25 -> I18n.format(if (mc.gameSettings.pauseOnLostFocus) "debug.pause_focus.on" else "debug.pause_focus.off")
        30 -> I18n.format("debug.reload_chunks.message")
        33 -> "${I18n.format("options.renderDistance")}: ${mc.gameSettings.renderDistanceChunks}"
        35 -> I18n.format(if (mc.gameSettings.advancedItemTooltips) "debug.advanced_tooltips.on" else "debug.advanced_tooltips.off")
        48 -> I18n.format(if (mc.renderManager.isDebugBoundingBox) "debug.show_hitboxes.on" else "debug.show_hitboxes.off")
        else -> ""
    }
    sendDebugMessage(string)
}

fun sendDebugMessage(string: String) {
    if (string.isEmpty()) return
    UChat.chat("§e§l${I18n.format("debug.prefix")}§r $string")
}