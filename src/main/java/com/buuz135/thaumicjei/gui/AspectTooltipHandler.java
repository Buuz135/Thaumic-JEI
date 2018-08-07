/*
 * This file is part of Hot or Not.
 *
 * Copyright 2018, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.thaumicjei.gui;

import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.config.ThaumicConfig;
import com.buuz135.thaumicjei.util.AspectUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

/**
 * Modified Thaumcraft code to make the tooltip showing work on non container GUI
 */
@Mod.EventBusSubscriber(modid = ThaumicJEI.MOD_ID, value = Side.CLIENT)
public class AspectTooltipHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tooltipEvent(ItemTooltipEvent event) {
        if (!ThaumicConfig.forceAspectTooltipInAllGUI) return;
        Minecraft mc = FMLClientHandler.instance().getClient();
        GuiScreen gui = mc.currentScreen;
        if (!(gui instanceof GuiContainer) && GuiScreen.isShiftKeyDown() != ModConfig.CONFIG_GRAPHICS.showTags) {
            AspectList tags = ThaumcraftCraftingManager.getObjectTags(event.getItemStack());
            if (tags == null) return;
            int index = 0;
            if (tags.size() > 0) {
                for (Aspect aspect : tags.getAspects()) {
                    if (aspect != null) ++index;
                }
            }
            int width = index * 18;
            if (width > 0) {
                double spaceWidth = (double) mc.fontRenderer.getStringWidth(" ");
                int spaceAmount = MathHelper.ceil((double) width / spaceWidth);
                int heightAmount = MathHelper.ceil(18.0D / (double) mc.fontRenderer.FONT_HEIGHT);
                for (int a = 0; a < heightAmount; ++a) {
                    event.getToolTip().add("                                                                                                                                            ".substring(0, Math.min(120, spaceAmount)));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tooltipEvent(RenderTooltipEvent.PostBackground event) {
        if (!ThaumicConfig.forceAspectTooltipInAllGUI) return;
        Minecraft mc = FMLClientHandler.instance().getClient();
        GuiScreen gui = mc.currentScreen;
        if (!(gui instanceof GuiContainer) && GuiScreen.isShiftKeyDown() != ModConfig.CONFIG_GRAPHICS.showTags && !Mouse.isGrabbed()) {
            int bot = event.getHeight();
            if (!event.getLines().isEmpty()) {
                for (int a = event.getLines().size() - 1; a >= 0; --a) {
                    if (event.getLines().get(a) != null && !((String) event.getLines().get(a)).contains("    ")) {
                        bot -= 10;
                    } else if (a > 0 && event.getLines().get(a - 1) != null && ((String) event.getLines().get(a - 1)).contains("    ")) {
                        renderAspectsInGui(event.getStack(), bot, event.getX(), event.getY());
                        break;
                    }
                }
            }
        }
    }

    public static void renderAspectsInGui(ItemStack stack, int botY, int posX, int posY) {
        AspectList tags = ThaumcraftCraftingManager.getObjectTags(stack);
        if (tags != null) {
            GL11.glPushMatrix();
            int pos = 0;
            if (tags.size() > 0) {
                Aspect[] sortedTags = tags.getAspectsSortedByAmount();
                for (Aspect sortedTag : sortedTags) {
                    AspectUtils.renderAspectList(new AspectList().add(sortedTag, tags.getAmount(sortedTag)), posX + pos * 19, posY + botY - 16, Minecraft.getMinecraft());
                    ++pos;
                }
            }
            GL11.glPopMatrix();
        }
    }
}
