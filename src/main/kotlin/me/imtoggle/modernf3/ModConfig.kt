package me.imtoggle.modernf3

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.DualOption
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.*

object ModConfig : Config(Mod(ModernF3.NAME, ModType.UTIL_QOL, "/gray.svg"), "${ModernF3.MODID}.json") {

    @Switch(
        name = "Keep Input History (Sent Messages)",
        subcategory = "Clear Chat"
    )
    var keepInputHistory = true

    @Switch(
        name = "Use Modern Color",
        subcategory = "Chunk Border"
    )
    var useModernColor = true

    @DualOption(
        name = "Grid",
        left = "1x1",
        right = "2x2",
        subcategory = "Chunk Border"
    )
    var chunkBorderGrid = true

    init {
        initialize()
    }
}