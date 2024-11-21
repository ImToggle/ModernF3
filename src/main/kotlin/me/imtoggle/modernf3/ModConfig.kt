package me.imtoggle.modernf3

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.data.*

object ModConfig : Config(Mod(ModernF3.NAME, ModType.UTIL_QOL), "${ModernF3.MODID}.json") {

    init {
        initialize()
    }
}