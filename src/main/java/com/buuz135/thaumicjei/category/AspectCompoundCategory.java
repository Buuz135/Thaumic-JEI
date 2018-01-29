package com.buuz135.thaumicjei.category;

import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;

import java.util.Arrays;

public class AspectCompoundCategory extends BlankRecipeCategory<AspectCompoundCategory.AspectCompoundWrapper> {

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
        return "Aspect compound";
    }

    @Override
    public IDrawable getBackground() {
        return helper.createBlankDrawable(108, 24);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.fontRendererObj.drawString(TextFormatting.DARK_GRAY + "+", 32, 4, 0);
        minecraft.fontRendererObj.drawString(TextFormatting.DARK_GRAY + "=", 68, 4, 0);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AspectCompoundWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getIngredientsGroup(Aspect.class).init(0, false, new AspectIngredientRender(), 82, 0, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(Aspect.class).set(0, ingredients.getOutputs(Aspect.class));
        recipeLayout.getIngredientsGroup(Aspect.class).init(1, true, new AspectIngredientRender(), 8, 0, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(Aspect.class).init(2, true, new AspectIngredientRender(), 46, 0, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(Aspect.class).set(1, ingredients.getInputs(Aspect.class).get(0));
        recipeLayout.getIngredientsGroup(Aspect.class).set(2, ingredients.getInputs(Aspect.class).get(1));
    }


    public static class AspectCompoundWrapper extends BlankRecipeWrapper {

        private final Aspect aspect;

        public AspectCompoundWrapper(Aspect aspect) {
            this.aspect = aspect;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setOutput(Aspect.class, aspect);
            ingredients.setInputs(Aspect.class, Arrays.asList(aspect.getComponents()));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glScaled(0.5, 0.5, 0.5);
            String name = TextFormatting.DARK_GRAY + "" + aspect.getName();
            minecraft.fontRendererObj.drawString(name, 181 - minecraft.fontRendererObj.getStringWidth(name) / 2, 34, 0);
            name = TextFormatting.DARK_GRAY + "" + aspect.getComponents()[0].getName();
            minecraft.fontRendererObj.drawString(name, 32 - minecraft.fontRendererObj.getStringWidth(name) / 2, 34, 0);
            name = TextFormatting.DARK_GRAY + "" + aspect.getComponents()[1].getName();
            minecraft.fontRendererObj.drawString(name, 109 - minecraft.fontRendererObj.getStringWidth(name) / 2, 34, 0);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
    }

    public static class AspectCompoundHandler implements IRecipeHandler<AspectCompoundWrapper> {

        @Override
        public Class<AspectCompoundWrapper> getRecipeClass() {
            return AspectCompoundWrapper.class;
        }

        @Override
        public String getRecipeCategoryUid() {
            return UUID;
        }

        @Override
        public String getRecipeCategoryUid(AspectCompoundWrapper recipe) {
            return UUID;
        }

        @Override
        public IRecipeWrapper getRecipeWrapper(AspectCompoundWrapper recipe) {
            return recipe;
        }

        @Override
        public boolean isRecipeValid(AspectCompoundWrapper recipe) {
            return true;
        }
    }
}
