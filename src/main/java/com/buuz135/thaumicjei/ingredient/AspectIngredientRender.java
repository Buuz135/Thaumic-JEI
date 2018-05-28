package com.buuz135.thaumicjei.ingredient;

import com.buuz135.thaumicjei.util.AspectUtils;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class AspectIngredientRender implements IIngredientRenderer<AspectList> {
    @Override
    public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable AspectList ingredient) {
        if (ingredient != null && ingredient.size() > 0) {
            AspectUtils.renderAspectList(ingredient, xPosition, yPosition, minecraft);
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
