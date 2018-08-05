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
