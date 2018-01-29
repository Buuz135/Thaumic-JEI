package com.buuz135.thaumicjei.category;

import com.buuz135.thaumicjei.AlphaDrawable;
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
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.CrucibleRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrucibleCategory extends BlankRecipeCategory<CrucibleCategory.CrucibleWrapper> {

    public static final String UUID = "THAUMCRAFT_CRUCIBLE";
    public static final int ASPECT_Y = 66;
    public static final int ASPECT_X = 56;
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
        return "Crucible";
    }

    @Override
    public IDrawable getBackground() {
        return new AlphaDrawable(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"), 2, 5, 109, 129);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrucibleWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, false, 51 -6, 8);
        recipeLayout.getItemStacks().set(0, ingredients.getOutputs(ItemStack.class).get(0));

        recipeLayout.getItemStacks().init(1, true, -8,2);
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
        Gui.drawModalRectWithCustomSizedTexture(6, 6, 199, 168, 26,26, 512, 512);
        GL11.glDisable(3042);
    }

    public static class CrucibleWrapper extends BlankRecipeWrapper implements IHasResearch {

        private final CrucibleRecipe recipe;


        public CrucibleWrapper(CrucibleRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            if (recipe.catalyst instanceof ItemStack){
                ingredients.setInput(ItemStack.class, (ItemStack) recipe.catalyst);
            }else{
                List<List<ItemStack>> in = new ArrayList<>();
                in.add((List<ItemStack>) recipe.catalyst);
                ingredients.setInputLists(ItemStack.class, in);
            }
            ingredients.setInputs(Aspect.class, Arrays.asList(recipe.aspects.getAspectsSortedByAmount()));
            ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int center = (recipe.aspects.size() * SPACE) / 2;
            int x = 0;
            for (Aspect aspect : recipe.aspects.getAspectsSortedByAmount()){
                minecraft.renderEngine.bindTexture(aspect.getImage());
                GL11.glPushMatrix();
                GL11.glScaled(0.5,0.5,0.5);
                minecraft.currentScreen.drawString(minecraft.fontRendererObj, TextFormatting.WHITE + "" + recipe.aspects.getAmount(aspect), 28 + (ASPECT_X - center + x * SPACE) * 2, ASPECT_Y * 2 + 26, 0);
                GL11.glPopMatrix();
                ++x;
            }
        }

        @Nullable
        @Override
        public List<String> getTooltipStrings(int mouseX, int mouseY) {
            return Arrays.asList();
        }

        @Override
        public String[] getResearch() {
            return recipe.research;
        }
    }

    public static class CrucibleHandler implements IRecipeHandler<CrucibleWrapper> {

        @Override
        public Class<CrucibleWrapper> getRecipeClass() {
            return CrucibleWrapper.class;
        }

        @Override
        public String getRecipeCategoryUid() {
            return CrucibleCategory.UUID;
        }

        @Override
        public String getRecipeCategoryUid(CrucibleWrapper recipe) {
            return CrucibleCategory.UUID;
        }

        @Override
        public IRecipeWrapper getRecipeWrapper(CrucibleWrapper recipe) {
            return recipe;
        }

        @Override
        public boolean isRecipeValid(CrucibleWrapper recipe) {
            return true;
        }
    }
}
