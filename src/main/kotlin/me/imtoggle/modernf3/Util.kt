package me.imtoggle.modernf3

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.utils.IOUtils
import cc.polyfrost.oneconfig.utils.dsl.mc
import me.imtoggle.modernf3.renderer.ChunkBorderRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import net.minecraft.client.settings.KeyBinding
import net.minecraft.crash.CrashReport
import net.minecraft.util.ReportedException
import net.minecraft.world.WorldSettings.GameType
import org.lwjgl.input.Keyboard
import kotlin.math.ceil

private var pauseTime = Minecraft.getSystemTime()

private var functionKeys = listOf(16, 20, 25, 30, 31, 32, 33, 34, 35, 46, 48, 49)

private var shouldPause = false

private var hasAction = false

private var actionKey = -1

private var crashStart = -1L

private var reportTime = Minecraft.getSystemTime()

private var reported = false

private var currentMode = -1

private var lastMode = -1

private val HELP_LIST = listOf(
    "debug.reload_chunks.help",
    "debug.show_hitboxes.help",
    "debug.copy_location.help",
    "debug.clear_chat.help",
    "debug.chunk_boundaries.help",
    "debug.advanced_tooltips.help",
    "debug.creative_spectator.help",
    "debug.pause_focus.help",
    "debug.help.help",
    "debug.reload_resourcepacks.help",
    "debug.pause.help"
)

@JvmField
var keepSentMessage = false

@JvmField
var isPausing = false

fun onTick() {
    val currentTime = Minecraft.getSystemTime()

    val f3Pressed = Keyboard.isKeyDown(61)

    if (crashStart > 0L) {
        if (Minecraft.getSystemTime() - crashStart >= 10000L) {
            crashStart = -1
            reported = false
            throw ReportedException(CrashReport("Manually triggered debug crash", Throwable()))
        }

        if (currentTime >= reportTime) {
            reportTime = currentTime + 1000
            val time = ceil((10000L - currentTime + crashStart) / 1000f).toInt()
            if (reported) {
                UChat.chat("§c§l${I18n.format("debug.prefix")}§r ${I18n.format("debug.crash.warning", "$time")}")
            } else {
                sendDebugMessage(I18n.format("debug.crash.message"))
                reported = true
            }
        }

        if (!Keyboard.isKeyDown(46) || !f3Pressed) {
            crashStart = -1L
            reported = false
        }
    } else if (Keyboard.isKeyDown(46) && f3Pressed) {
        crashStart = currentTime
        reportTime = currentTime + 1000
    }

    if (f3Pressed) {
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
    var shouldCancel = false
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
            16 -> {
                sendDebugMessage(I18n.format("debug.help.message"))
                HELP_LIST.forEach { UChat.chat(I18n.format(it)) }
            }
            32 -> if (mc.ingameGUI == null) {
                shouldCancel = true
            } else {
                keepSentMessage = ModConfig.keepInputHistory
            }
            34 -> sendDebugMessage(I18n.format(if (ChunkBorderRenderer.toggle()) "debug.chunk_boundaries.on" else "debug.chunk_boundaries.off"))
            46 -> {
                IOUtils.copyStringToClipboard(String.format("/tp ${mc.thePlayer.name} %.2f %.2f %.2f %.2f %.2f", mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch))
                sendDebugMessage(I18n.format("debug.copy_location.message"))
            }
            49 -> {
                UChat.say("/gamemode ${if (currentMode == 3) lastMode else 3}")
            }
        }
    }
    if (shouldCancel) {
        actionKey = -1
        hasAction = false
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

fun gameModeChange(gameMode: GameType) {
    currentMode = gameMode.id
    if (gameMode.id != 3) lastMode = gameMode.id
    if (lastMode == -1) lastMode = 1
}

fun shouldCancel(keys: ArrayList<Int>): Boolean {
    if (!Keyboard.isKeyDown(61)) return false
    for (i in keys) {
        if (i in functionKeys) return true
    }
    return false
}