package me.imtoggle.modernf3.renderer

import cc.polyfrost.oneconfig.utils.dsl.mc
import me.imtoggle.modernf3.ModConfig
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color


object ChunkBorderRenderer {

    private var shouldRender = false

    private val CELL_BORDER = Color(0, 155, 155, 255)

    private val YELLOW = Color(255, 255, 0, 255)

    private val GRID
        get() = if (ModConfig.chunkBorderGrid) 2 else 1

    fun toggle(): Boolean {
        shouldRender = !shouldRender
        return shouldRender
    }

    fun render(partialTicks: Float) {
        if (mc.gameSettings.reducedDebugInfo) return
        if (!shouldRender) return
        val entity = mc.renderViewEntity ?: return
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer
        val d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks.toDouble()
        val d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks.toDouble()
        val d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks.toDouble()
        val d3 = 0.0 - d1
        val d4 = 256.0 - d1
        GlStateManager.disableTexture2D()
        GlStateManager.disableBlend()
        val d5 = (entity.chunkCoordX shl 4).toDouble() - d0
        val d6 = (entity.chunkCoordZ shl 4).toDouble() - d2
        GL11.glLineWidth(1.0f)
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR)

        for (i in -16..32 step 16) {
            for (j in -16..32 step 16) {
                worldRenderer.pos(d5 + i.toDouble(), d3, d6 + j.toDouble()).color(1.0f, 0.0f, 0.0f, 0.0f).endVertex()
                worldRenderer.pos(d5 + i.toDouble(), d3, d6 + j.toDouble()).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex()
                worldRenderer.pos(d5 + i.toDouble(), d4, d6 + j.toDouble()).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex()
                worldRenderer.pos(d5 + i.toDouble(), d4, d6 + j.toDouble()).color(1.0f, 0.0f, 0.0f, 0.0f).endVertex()
            }
        }

        val gird = GRID

        for (k in gird..<16 step gird) {
            val color = if (k % 4 == 0 && ModConfig.useModernColor) CELL_BORDER else YELLOW
            worldRenderer.pos(d5 + k.toDouble(), d3, d6).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
            worldRenderer.pos(d5 + k.toDouble(), d3, d6).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5 + k.toDouble(), d4, d6).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5 + k.toDouble(), d4, d6).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
            worldRenderer.pos(d5 + k.toDouble(), d3, d6 + 16.0).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
            worldRenderer.pos(d5 + k.toDouble(), d3, d6 + 16.0).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5 + k.toDouble(), d4, d6 + 16.0).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5 + k.toDouble(), d4, d6 + 16.0).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
        }

        for (l in gird..<16 step gird) {
            val color = if (l % 4 == 0 && ModConfig.useModernColor) CELL_BORDER else YELLOW
            worldRenderer.pos(d5, d3, d6 + l.toDouble()).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
            worldRenderer.pos(d5, d3, d6 + l.toDouble()).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5, d4, d6 + l.toDouble()).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5, d4, d6 + l.toDouble()).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
            worldRenderer.pos(d5 + 16.0, d3, d6 + l.toDouble()).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
            worldRenderer.pos(d5 + 16.0, d3, d6 + l.toDouble()).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5 + 16.0, d4, d6 + l.toDouble()).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5 + 16.0, d4, d6 + l.toDouble()).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
        }

        for (i1 in 0..256 step gird) {
            val d7 = i1.toDouble() - d1
            val color = if (i1 % 8 == 0 && ModConfig.useModernColor) CELL_BORDER else YELLOW
            worldRenderer.pos(d5, d7, d6).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
            worldRenderer.pos(d5, d7, d6).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5, d7, d6 + 16.0).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5 + 16.0, d7, d6 + 16.0).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5 + 16.0, d7, d6).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(d5, d7, d6).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex()
            worldRenderer.pos(d5, d7, d6).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex()
        }

        tessellator.draw()
        GL11.glLineWidth(2.0f)
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR)

        for (j1 in 0..16 step 16) {
            for (l1 in 0..16 step 16) {
                worldRenderer.pos(d5 + j1.toDouble(), d3, d6 + l1.toDouble()).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex()
                worldRenderer.pos(d5 + j1.toDouble(), d3, d6 + l1.toDouble()).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex()
                worldRenderer.pos(d5 + j1.toDouble(), d4, d6 + l1.toDouble()).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex()
                worldRenderer.pos(d5 + j1.toDouble(), d4, d6 + l1.toDouble()).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex()
            }
        }

        for (k1 in 0..256 step 16) {
            val d8 = k1.toDouble() - d1
            worldRenderer.pos(d5, d8, d6).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex()
            worldRenderer.pos(d5, d8, d6).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex()
            worldRenderer.pos(d5, d8, d6 + 16.0).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex()
            worldRenderer.pos(d5 + 16.0, d8, d6 + 16.0).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex()
            worldRenderer.pos(d5 + 16.0, d8, d6).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex()
            worldRenderer.pos(d5, d8, d6).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex()
            worldRenderer.pos(d5, d8, d6).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex()
        }

        tessellator.draw()
        GL11.glLineWidth(1.0f)
        GlStateManager.enableBlend()
        GlStateManager.enableTexture2D()
    }
}