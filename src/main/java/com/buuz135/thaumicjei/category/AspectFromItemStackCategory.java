package com.buuz135.thaumicjei.category;

import com.buuz135.thaumicjei.AlphaDrawable;
import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;

import java.util.List;

public class AspectFromItemStackCategory extends BlankRecipeCategory<AspectFromItemStackCategory.AspectFromItemStackWrapper> {

    public static final String UUID = "THAUMCRAFT_ASPECT_FROM_ITEMSTACK";

    @Override
    public String getUid() {
        return UUID;
    }

    @Override
    public String getTitle() {
        return "Aspect from ItemStack";
    }

    @Override
    public IDrawable getBackground() {
        return new AlphaDrawable(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"), 40, 6, 32, 32, 0, 18 * 4, 0, 0);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(new ResourceLocation(ThaumicJEI.MOD_ID, "textures/gui/gui.png"));
        GL11.glEnable(3042);
        Gui.drawModalRectWithCustomSizedTexture(-66, 31, 0, 0, 163, 74, 256, 256);
        GL11.glDisable(3042);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AspectFromItemStackWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getIngredientsGroup(Aspect.class).init(0, false, new AspectIngredientRender(), 8, 8, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(Aspect.class).set(0, ingredients.getOutputs(Aspect.class).get(0));
        int slot = 0;
        int row = 9;
        for (List<ItemStack> stacks : ingredients.getInputs(ItemStack.class)) {
            recipeLayout.getItemStacks().init(slot + 1, true, (slot % row) * 18 - 18 * 3 - 12, (slot / row) * 18 + 32);
            recipeLayout.getItemStacks().set(slot + 1, stacks);
            ++slot;
        }
    }

    public static class AspectFromItemStackWrapper extends BlankRecipeWrapper {

        private final Aspect aspect;
        private final List<ItemStack> stacks;

        public AspectFromItemStackWrapper(Aspect aspect, List<ItemStack> stacks) {
            this.aspect = aspect;
            this.stacks = stacks;
        }


        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setOutput(Aspect.class, aspect);
            ingredients.setInputs(ItemStack.class, stacks);
        }
    }

    public static class AspectFromItemStackHandler implements IRecipeHandler<AspectFromItemStackWrapper> {

        @Override
        public Class<AspectFromItemStackWrapper> getRecipeClass() {
            return AspectFromItemStackWrapper.class;
        }

        @Override
        public String getRecipeCategoryUid() {
            return UUID;
        }

        @Override
        public String getRecipeCategoryUid(AspectFromItemStackWrapper recipe) {
            return UUID;
        }

        @Override
        public IRecipeWrapper getRecipeWrapper(AspectFromItemStackWrapper recipe) {
            return recipe;
        }

        @Override
        public boolean isRecipeValid(AspectFromItemStackWrapper recipe) {
            return true;
        }
    }
}
