package com.buuz135.thaumicjei.category;

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
        recipeLayout.getIngredientsGroup(AspectList.class).init(0, false, new AspectIngredientRender(), 82, 2, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(AspectList.class).set(0, ingredients.getOutputs(AspectList.class).get(0));
        recipeLayout.getIngredientsGroup(AspectList.class).init(1, true, new AspectIngredientRender(), 8, 2, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(AspectList.class).init(2, true, new AspectIngredientRender(), 46, 2, 16, 16, 0, 0);
        recipeLayout.getIngredientsGroup(AspectList.class).set(1, ingredients.getInputs(AspectList.class).get(0));
        recipeLayout.getIngredientsGroup(AspectList.class).set(2, ingredients.getInputs(AspectList.class).get(1));
    }


    public static class AspectCompoundWrapper implements IRecipeWrapper {

        private final Aspect aspect;

        public AspectCompoundWrapper(Aspect aspect) {
            this.aspect = aspect;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setOutput(AspectList.class, new AspectList().add(aspect, 1));
            ingredients.setInputs(AspectList.class, Arrays.stream(aspect.getComponents()).map(aspect1 -> new AspectList().add(aspect1, 1)).collect(Collectors.toList()));
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
