package com.buuz135.thaumicjei.category;

import com.buuz135.thaumicjei.AlphaDrawable;
import com.buuz135.thaumicjei.ItemStackDrawable;
import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.CrucibleRecipe;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CrucibleCategory implements IRecipeCategory<CrucibleCategory.CrucibleWrapper> {

    public static final String UUID = "THAUMCRAFT_CRUCIBLE";
    public static final int ASPECT_Y = 66;
    public static final int ASPECT_X = 66;
    public static final int SPACE = 22;

    private final IGuiHelper helper;

    public CrucibleCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public String getUid() {
        return UUID;
    }

    @Override
    public String getTitle() {
        return new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "crucible").toString())).getDisplayName();
    }

    @Override
    public String getModName() {
        return ThaumicJEI.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return new AlphaDrawable(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"), 2, 5, 109, 129, 0, 0, 10, 10);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return new ItemStackDrawable(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "crucible").toString())));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrucibleWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, false, 61 - 6, 8);
        recipeLayout.getItemStacks().set(0, ingredients.getOutputs(ItemStack.class).get(0));

        recipeLayout.getItemStacks().init(1, true, 2, 2);
        recipeLayout.getItemStacks().set(1, ingredients.getInputs(ItemStack.class).get(0));

        int center = (ingredients.getInputs(Aspect.class).size() * SPACE) / 2;
        int x = 0;
        for (List<Aspect> aspectList : ingredients.getInputs(Aspect.class)) {
            recipeLayout.getIngredientsGroup(Aspect.class).init(x + 1, true, new AspectIngredientRender(), ASPECT_X - center + x * SPACE, ASPECT_Y, 16, 16, 0, 0);
            recipeLayout.getIngredientsGroup(Aspect.class).set(x + 1, aspectList);
            ++x;
        }
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"));
        GL11.glEnable(3042);
        Gui.drawModalRectWithCustomSizedTexture(16, 6, 199, 168, 26, 26, 512, 512);
        GL11.glDisable(3042);
    }

    public static class CrucibleWrapper implements IHasResearch, IRecipeWrapper {

        private final CrucibleRecipe recipe;


        public CrucibleWrapper(CrucibleRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            ingredients.setInput(ItemStack.class, recipe.getCatalyst().getMatchingStacks()[0]);
            ingredients.setInputs(Aspect.class, Arrays.asList(recipe.getAspects().getAspectsSortedByAmount()));
            ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int center = (recipe.getAspects().size() * SPACE) / 2;
            int x = 0;
            for (Aspect aspect : recipe.getAspects().getAspectsSortedByAmount()) {
                minecraft.renderEngine.bindTexture(aspect.getImage());
                GL11.glPushMatrix();
                GL11.glScaled(0.5, 0.5, 0.5);
                minecraft.currentScreen.drawString(minecraft.fontRenderer, TextFormatting.WHITE + "" + recipe.getAspects().getAmount(aspect), 28 + (ASPECT_X - center + x * SPACE) * 2, ASPECT_Y * 2 + 26, 0);
                GL11.glPopMatrix();
                ++x;
            }
        }

        @Override
        public String[] getResearch() {
            return new String[]{recipe.getResearch()};
        }
    }

}
