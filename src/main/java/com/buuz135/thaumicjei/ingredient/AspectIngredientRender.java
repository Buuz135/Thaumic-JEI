package com.buuz135.thaumicjei.ingredient;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AspectIngredientRender implements IIngredientRenderer<Aspect> {
    @Override
    public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable Aspect ingredient) {
        if (ingredient != null) {
            GL11.glPushMatrix();
            minecraft.renderEngine.bindTexture(ingredient.getImage());
            GL11.glEnable(3042);
            Color c = new Color(ingredient.getColor());
            GL11.glColor4f((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, 16, 16, 16, 16);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GL11.glPopMatrix();
        }
    }

    @Override
    public List<String> getTooltip(Minecraft minecraft, Aspect ingredient, ITooltipFlag tooltipFlag) {
        return Arrays.asList(TextFormatting.AQUA + ingredient.getName(), TextFormatting.GRAY + ingredient.getLocalizedDescription());
    }

    @Override
    public FontRenderer getFontRenderer(Minecraft minecraft, Aspect ingredient) {
        return minecraft.fontRenderer;
    }
}
