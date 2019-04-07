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
package com.buuz135.thaumicjei.category;

import com.buuz135.thaumicjei.ThaumcraftJEIPlugin;
import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AspectCompoundCategory implements IRecipeCategory<AspectCompoundCategory.AspectCompoundWrapper> {

    public static final String UUID = "THAUMCRAFT_ASPECT_COMPOUND";

    private final IGuiHelper helper;

    public AspectCompoundCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public String getUid() {
        return UUID;
    }

    @Override
    public String getTitle() {
        return I18n.format("thaumicjei.category.aspect_compound.title");
    }

    @Override
    public String getModName() {
        return ThaumicJEI.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return helper.createBlankDrawable(108, 24);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.fontRenderer.drawString(TextFormatting.DARK_GRAY + "+", 32, 6, 0);
        minecraft.fontRenderer.drawString(TextFormatting.DARK_GRAY + "=", 68, 6, 0);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AspectCompoundWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).init(0, false, new AspectIngredientRender(), 82, 2, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).set(0, ingredients.getOutputs(ThaumcraftJEIPlugin.ASPECT_LIST).get(0));
        recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).init(1, true, new AspectIngredientRender(), 8, 2, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).init(2, true, new AspectIngredientRender(), 46, 2, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).set(1, ingredients.getInputs(ThaumcraftJEIPlugin.ASPECT_LIST).get(0));
        recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).set(2, ingredients.getInputs(ThaumcraftJEIPlugin.ASPECT_LIST).get(1));
    }


    public static class AspectCompoundWrapper implements IRecipeWrapper {

        private final Aspect aspect;

        public AspectCompoundWrapper(Aspect aspect) {
            this.aspect = aspect;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setOutput(ThaumcraftJEIPlugin.ASPECT_LIST, new AspectList().add(aspect, 1));
            ingredients.setInputs(ThaumcraftJEIPlugin.ASPECT_LIST, Arrays.stream(aspect.getComponents()).map(aspect1 -> new AspectList().add(aspect1, 1)).collect(Collectors.toList()));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glScaled(0.5, 0.5, 0.5);
            String name = TextFormatting.DARK_GRAY + "" + aspect.getName();
            minecraft.fontRenderer.drawString(name, 181 - minecraft.fontRenderer.getStringWidth(name) / 2, 36, 0);
            name = TextFormatting.DARK_GRAY + "" + aspect.getComponents()[0].getName();
            minecraft.fontRenderer.drawString(name, 32 - minecraft.fontRenderer.getStringWidth(name) / 2, 36, 0);
            name = TextFormatting.DARK_GRAY + "" + aspect.getComponents()[1].getName();
            minecraft.fontRenderer.drawString(name, 109 - minecraft.fontRenderer.getStringWidth(name) / 2, 36, 0);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
    }
}
