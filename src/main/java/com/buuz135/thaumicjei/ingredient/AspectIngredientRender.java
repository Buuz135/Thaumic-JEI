package com.buuz135.thaumicjei.ingredient;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AspectIngredientRender implements IIngredientRenderer<AspectList> {
    @Override
    public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable AspectList ingredient) {
        if (ingredient != null && ingredient.size() > 0) {
            GL11.glPushMatrix();
            minecraft.renderEngine.bindTexture(ingredient.getAspects()[0].getImage());
            GL11.glEnable(3042);
            Color c = new Color(ingredient.getAspects()[0].getColor());
            GL11.glColor4f((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, 16, 16, 16, 16);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GL11.glScaled(0.5, 0.5, 0.5);
            if (ingredient.getAmount(ingredient.getAspects()[0]) > 1)
                minecraft.currentScreen.drawCenteredString(minecraft.fontRenderer, TextFormatting.WHITE + "" + ingredient.getAmount(ingredient.getAspects()[0]), (xPosition + 16) * 2, (yPosition + 12) * 2, 0);
            GL11.glPopMatrix();
        }
    }

    @Override
    public List<String> getTooltip(Minecraft minecraft, AspectList ingredient, ITooltipFlag tooltipFlag) {
        return ingredient.size() > 0 ? Arrays.asList(TextFormatting.AQUA + ingredient.getAspects()[0].getName(), TextFormatting.GRAY + ingredient.getAspects()[0].getLocalizedDescription()) : Arrays.asList();
    }

    @Override
    public FontRenderer getFontRenderer(Minecraft minecraft, AspectList ingredient) {
        return minecraft.fontRenderer;
    }
}
