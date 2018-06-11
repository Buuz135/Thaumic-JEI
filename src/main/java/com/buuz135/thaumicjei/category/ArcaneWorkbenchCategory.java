package com.buuz135.thaumicjei.category;

import com.buuz135.thaumicjei.AlphaDrawable;
import com.buuz135.thaumicjei.ItemStackDrawable;
import com.buuz135.thaumicjei.ThaumicJEI;
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
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.resources.ItemCrystalEssence;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArcaneWorkbenchCategory implements IRecipeCategory<ArcaneWorkbenchCategory.ArcaneWorkbenchWrapper> {

    public static final String UUID = "THAUMCRAFT_ARCANE_WORKBENCH";

    private IGuiHelper helper;

    public ArcaneWorkbenchCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public String getUid() {
        return UUID;
    }

    @Override
    public String getTitle() {
        return new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "arcane_workbench").toString())).getDisplayName();
    }

    @Override
    public String getModName() {
        return ThaumicJEI.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return new AlphaDrawable(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"), 225, 31, 102, 102, 36, 0, 30, 30);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return new ItemStackDrawable(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "arcane_workbench").toString())));
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"));
        GL11.glEnable(3042);
        Gui.drawModalRectWithCustomSizedTexture(51 - 16 + 30, 0, 40, 6, 32, 32, 512, 512);
        Gui.drawModalRectWithCustomSizedTexture(0 - 18 + 30, 4, 135, 152, 23, 23, 512, 512);
        GL11.glDisable(3042);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ArcaneWorkbenchWrapper recipeWrapper, IIngredients ingredients) {
        if (recipeWrapper.getRecipe() instanceof ShapelessArcaneRecipe) {
            recipeLayout.setShapeless();
        }
        recipeLayout.getItemStacks().init(0, false, 51 - 9 + 30, 7);
        int sizeX = 3;
        int sizeY = 3;
        if (recipeWrapper.getRecipe() instanceof ShapedArcaneRecipe) {
            sizeX = ((ShapedArcaneRecipe) recipeWrapper.getRecipe()).getRecipeWidth();
            sizeY = ((ShapedArcaneRecipe) recipeWrapper.getRecipe()).getRecipeHeight();
        }
        int slot = 1;
        for (int y = 0; y < sizeY; ++y) {
            for (int x = 0; x < sizeX; ++x) {
                if (ingredients.getInputs(ItemStack.class).size() >= slot) {
                    recipeLayout.getItemStacks().init(slot, true, 12 + (x * 30) + 30, 36 + 12 + (y) * 30);
                    if (ingredients.getInputs(ItemStack.class).get(slot - 1) != null) {
                        recipeLayout.getItemStacks().set(slot, ingredients.getInputs(ItemStack.class).get(slot - 1));
                    }
                    ++slot;
                }
            }
        }
        int crystalAmount = 0;
        if (recipeWrapper.getRecipe().getCrystals() != null) {
            for (Aspect aspect : recipeWrapper.getRecipe().getCrystals().getAspectsSortedByAmount()) {
                ItemStack crystal = new ItemStack(ItemsTC.crystalEssence);
                ((ItemCrystalEssence) ItemsTC.crystalEssence).setAspects(crystal, new AspectList().add(aspect, 1));
                crystal.setCount(recipeWrapper.getRecipe().getCrystals().getAmount(aspect));
                recipeLayout.getItemStacks().init(slot + crystalAmount, false, 118 + 23, 6 + 22 * crystalAmount);
                recipeLayout.getItemStacks().set(slot + crystalAmount, crystal);
                ++crystalAmount;
            }
        }

        recipeLayout.getItemStacks().set(0, ingredients.getOutputs(ItemStack.class).get(0));
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Arrays.asList();
    }

    public static class ArcaneWorkbenchWrapper implements IHasResearch, IRecipeWrapper {

        private final IArcaneRecipe recipe;

        public ArcaneWorkbenchWrapper(IArcaneRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            List<List<ItemStack>> lists = new ArrayList<>();
            List<Ingredient> input = new ArrayList<>();
            ItemStack output = null;
            if (recipe instanceof ShapedArcaneRecipe || recipe instanceof ShapelessArcaneRecipe) {
                input = recipe.getIngredients();
                output = recipe.getRecipeOutput();
            }
            for (Ingredient ingredient : input) {
                lists.add(Arrays.asList(ingredient.getMatchingStacks()));
            }
            ingredients.setInputLists(ItemStack.class, lists);
            ingredients.setOutput(ItemStack.class, output);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            minecraft.fontRenderer.drawString(TextFormatting.DARK_GRAY + String.valueOf(recipe.getVis()), 50 - minecraft.fontRenderer.getStringWidth(String.valueOf(recipe.getVis())) / 2, 12, 0);
        }

        @Nullable
        @Override
        public List<String> getTooltipStrings(int mouseX, int mouseY) {
            if (mouseX > -18 && mouseX < 34 && mouseY > 4 && mouseY < 28) {
                return Arrays.asList("Vis Cost");
            }
            return null;
        }

        @Override
        public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
            return false;
        }

        public IArcaneRecipe getRecipe() {
            return recipe;
        }

        @Override
        public String[] getResearch() {
            return new String[]{recipe.getResearch()};
        }
    }
}
