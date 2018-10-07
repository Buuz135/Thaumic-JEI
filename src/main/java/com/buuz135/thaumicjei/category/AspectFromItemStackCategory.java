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

import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.drawable.AlphaDrawable;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.AspectList;

import java.util.List;

public class AspectFromItemStackCategory implements IRecipeCategory<AspectFromItemStackCategory.AspectFromItemStackWrapper> {

    public static final String UUID = "THAUMCRAFT_ASPECT_FROM_ITEMSTACK";

    @Override
    public String getUid() {
        return UUID;
    }

    @Override
    public String getTitle() {
        return I18n.format("thaumicjei.category.aspect_from_itemstack.title");
    }

    @Override
    public String getModName() {
        return ThaumicJEI.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return new AlphaDrawable(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"), 40, 6, 32, 32, 0, 18 * 4 + 5, 72, 72);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(new ResourceLocation(ThaumicJEI.MOD_ID, "textures/gui/gui.png"));
        GL11.glEnable(3042);
        Gui.drawModalRectWithCustomSizedTexture(-66 + 81 - 9, 31, 0, 0, 163, 74, 256, 256);
        GL11.glDisable(3042);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AspectFromItemStackWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getIngredientsGroup(AspectList.class).init(0, false, new AspectIngredientRender(), 8 + 81 - 9, 8, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(AspectList.class).set(0, ingredients.getOutputs(AspectList.class).get(0));
        int slot = 0;
        int row = 9;
        for (List<ItemStack> stacks : ingredients.getInputs(ItemStack.class)) {
            recipeLayout.getItemStacks().init(slot + 1, true, (slot % row) * 18 - 18 * 3 - 21 + 81, (slot / row) * 18 + 32);
            recipeLayout.getItemStacks().set(slot + 1, stacks);
            ++slot;
        }
    }

    public static class AspectFromItemStackWrapper implements IRecipeWrapper {

        private final AspectList aspect;
        private final List<ItemStack> stacks;

        public AspectFromItemStackWrapper(AspectList aspect, List<ItemStack> stacks) {
            this.aspect = aspect;
            this.stacks = stacks;
        }


        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setOutput(AspectList.class, aspect);
            ingredients.setInputs(ItemStack.class, stacks);
        }
    }

}
