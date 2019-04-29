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
import com.buuz135.thaumicjei.drawable.AlphaDrawable;
import com.buuz135.thaumicjei.drawable.ItemStackDrawable;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import com.buuz135.thaumicjei.util.ResearchUtils;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.InfusionRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InfusionCategory implements IRecipeCategory<InfusionCategory.InfusionWrapper> {

    public static final String UUID = "THAUMCRAFT_INFUSION";
    public static final int ASPECT_Y = 135;
    public static final int ASPECT_X = 46;
    public static final int SPACE = 22;

    @Override
    public String getUid() {
        return UUID;
    }

    @Override
    public String getTitle() {
        return I18n.format("thaumicjei.category.infusion.title");
    }

    @Override
    public String getModName() {
        return ThaumicJEI.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return new AlphaDrawable(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"), 413, 154, 86, 86, 40, 44, 30, 30);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"));
        GL11.glEnable(3042);
        Gui.drawModalRectWithCustomSizedTexture(27 + 30, 0, 40, 6, 32, 32, 512, 512);
        GL11.glDisable(3042);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return new ItemStackDrawable(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "infusion_matrix").toString())));
    }


    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InfusionWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, false, 34 + 30, 7);
        recipeLayout.getItemStacks().set(0, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        int slot = 1;
        float currentRotation = -90.0F;
        for (List<ItemStack> stacks : ingredients.getInputs(VanillaTypes.ITEM)) {
            if (slot == 1) recipeLayout.getItemStacks().init(slot, true, 34 + 30, 75);
            else
                recipeLayout.getItemStacks().init(slot, true, 30 + (int) (MathHelper.cos((float) (currentRotation / 180.0F * Math.PI)) * 40.0F) + 34, (int) (MathHelper.sin(currentRotation / 180.0F * 3.1415927F) * 40.0F) + 75);
            recipeLayout.getItemStacks().set(slot, stacks);
            currentRotation += (360f / recipeWrapper.recipe.getComponents().size());
            ++slot;
        }
        int center = (ingredients.getInputs(ThaumcraftJEIPlugin.ASPECT_LIST).size() * SPACE) / 2;
        int x = 0;
        for (List<AspectList> aspectList : ingredients.getInputs(ThaumcraftJEIPlugin.ASPECT_LIST)) {
            recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).init(x + slot, true, new AspectIngredientRender(), 30 + ASPECT_X - center + x * SPACE, ASPECT_Y, 16, 16, 0, 0);
            recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).set(x + slot, aspectList);
            ++x;
        }
    }

    public static class InfusionWrapper implements IHasResearch, IRecipeWrapper {

        private final InfusionRecipe recipe;

        public InfusionWrapper(InfusionRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            List<List<ItemStack>> inputs = new ArrayList<>();
            inputs.add(Arrays.asList(recipe.getRecipeInput().getMatchingStacks()));
            if (recipe.recipeOutput instanceof ItemStack) {
                ingredients.setOutput(VanillaTypes.ITEM, ((ItemStack) recipe.recipeOutput).copy());
            } else if (recipe.recipeOutput != null) {
                for (ItemStack stack : inputs.get(0)) {
                    if (stack != null) {
                        Object[] objects = (Object[]) recipe.recipeOutput;
                        ItemStack copied = stack.copy();
                        copied.setTagInfo((String) objects[0], (NBTBase) objects[1]);
                        ingredients.setOutput(VanillaTypes.ITEM, copied);
                    }
                }
            }
            for (Ingredient comp : recipe.getComponents()) {
                inputs.add(Arrays.asList(comp.getMatchingStacks()));
            }
            ingredients.setInputLists(VanillaTypes.ITEM, inputs);
            ingredients.setInputs(ThaumcraftJEIPlugin.ASPECT_LIST, Arrays.stream(recipe.aspects.getAspectsSortedByAmount()).map(aspect -> new AspectList().add(aspect, recipe.aspects.getAmount(aspect))).collect(Collectors.toList()));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int instability = Math.min(5, recipe.instability / 2);
            String inst = TextFormatting.DARK_GRAY + new TextComponentTranslation("tc.inst").getFormattedText() + new TextComponentTranslation("tc.inst." + instability).getUnformattedText();
            minecraft.fontRenderer.drawString(inst, (recipeWidth / 2) - (minecraft.fontRenderer.getStringWidth(inst) / 2), 158, 0);
            if (!ThaumcraftCapabilities.knowsResearch(Minecraft.getMinecraft().player, getResearch()))
                minecraft.getRenderItem().renderItemIntoGUI(new ItemStack(Blocks.BARRIER), 92, 9);
        }

        @Override
        public String[] getResearch() {
            return new String[]{recipe.research};
        }

        @Override
        public List<String> getTooltipStrings(int mouseX, int mouseY) {
            if (!ThaumcraftCapabilities.knowsResearch(Minecraft.getMinecraft().player, getResearch()) && mouseX > 92 && mouseX < 108 && mouseY > 9 && mouseY < 25) {
                return ResearchUtils.generateMissingResearchList(getResearch());
            }
            return null;
        }
    }

}
