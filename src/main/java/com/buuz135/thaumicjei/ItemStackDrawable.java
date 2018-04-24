package com.buuz135.thaumicjei;

import mezz.jei.api.gui.IDrawableStatic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ItemStackDrawable implements IDrawableStatic {

    private final ItemStack stack;

    public ItemStackDrawable(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void draw(Minecraft minecraft, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();
        minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, stack, xOffset, yOffset);
        GlStateManager.disableDepth();
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void draw(Minecraft minecraft) {
        draw(minecraft, 0, 0);
    }

    @Override
    public void draw(Minecraft minecraft, int xOffset, int yOffset) {
        draw(minecraft, xOffset, yOffset, 0, 0, 0, 0);
    }
}
