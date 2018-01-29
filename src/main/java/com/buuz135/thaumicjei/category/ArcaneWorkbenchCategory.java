package com.buuz135.thaumicjei.category;

import com.buuz135.thaumicjei.AlphaDrawable;
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
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArcaneWorkbenchCategory extends BlankRecipeCategory<ArcaneWorkbenchCategory.ArcaneWorkbenchWrapper> {

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
        return "Arcane workbench";
    }

    @Override
    public IDrawable getBackground() {
        return new AlphaDrawable(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"), 225, 31, 102, 102, 36, 0, 0, 0);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"));
        GL11.glEnable(3042);
        Gui.drawModalRectWithCustomSizedTexture(51 - 16, 0, 40, 6, 32, 32, 512, 512);
        Gui.drawModalRectWithCustomSizedTexture(0 - 18, 4, 135, 152, 23, 23, 512, 512);
        GL11.glDisable(3042);
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ArcaneWorkbenchWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, false, 51 - 9, 7);
        int sizeX = 3;
        int sizeY = 3;
        if (recipeWrapper.getRecipe() instanceof ShapedArcaneRecipe) {
            sizeX = ((ShapedArcaneRecipe) recipeWrapper.getRecipe()).width;
            sizeY = ((ShapedArcaneRecipe) recipeWrapper.getRecipe()).height;
        }
        int slot = 1;
        for (int y = 0; y < sizeY; ++y) {
            for (int x = 0; x < sizeX; ++x) {
                if (ingredients.getInputs(ItemStack.class).size() >= slot) {
                    recipeLayout.getItemStacks().init(slot, true, 12 + (x * 30), 36 + 12 + (y) * 30);
                    if (ingredients.getInputs(ItemStack.class).get(slot - 1) != null) {
                        recipeLayout.getItemStacks().set(slot, ingredients.getInputs(ItemStack.class).get(slot - 1));
                    }
                    ++slot;
                }
            }
        }
        int crystalAmount = 0;
        for (ItemStack crystal : recipeWrapper.getRecipe().getCrystals()) {
            recipeLayout.getItemStacks().init(slot + crystalAmount, false, 118, 6 + 22 * crystalAmount);
            recipeLayout.getItemStacks().set(slot + crystalAmount, crystal);
            ++crystalAmount;
        }
        recipeLayout.getItemStacks().set(0, ingredients.getOutputs(ItemStack.class).get(0));
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Arrays.asList();
    }

    public static class ArcaneWorkbenchHandler implements IRecipeHandler<ArcaneWorkbenchWrapper> {
        @Override
        public Class<ArcaneWorkbenchWrapper> getRecipeClass() {
            return ArcaneWorkbenchWrapper.class;
        }

        @Override
        public String getRecipeCategoryUid() {
            return ArcaneWorkbenchCategory.UUID;
        }

        @Override
        public String getRecipeCategoryUid(ArcaneWorkbenchWrapper recipe) {
            return ArcaneWorkbenchCategory.UUID;
        }

        @Override
        public IRecipeWrapper getRecipeWrapper(ArcaneWorkbenchWrapper recipe) {
            return recipe;
        }

        @Override
        public boolean isRecipeValid(ArcaneWorkbenchWrapper recipe) {
            return true;
        }
    }


    public static class ArcaneWorkbenchWrapper extends BlankRecipeWrapper implements IHasResearch {

        private final IArcaneRecipe recipe;

        public ArcaneWorkbenchWrapper(IArcaneRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void getIngredients(IIngredients ingredients) {
            List<List<ItemStack>> lists = new ArrayList<>();
            List<Object> input = new ArrayList<>();
            ItemStack output = null;
            if (recipe instanceof ShapedArcaneRecipe) {
                input = Arrays.asList(((ShapedArcaneRecipe) recipe).input);
                output = ((ShapedArcaneRecipe) recipe).output;
            } else if (recipe instanceof ShapelessArcaneRecipe) {
                input = ((ShapelessArcaneRecipe) recipe).getInput();
                output = recipe.getRecipeOutput();
            }
            for (Object o : input) {
                if (o == null) {
                    lists.add(new ArrayList<>());
                } else if (o instanceof ItemStack) {
                    lists.add(Arrays.asList((ItemStack) o));
                } else {
                    lists.add((List) o);
                }
            }
            ingredients.setInputLists(ItemStack.class, lists);
            ingredients.setOutput(ItemStack.class, output);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            minecraft.fontRendererObj.drawString(TextFormatting.DARK_GRAY + String.valueOf(recipe.getVis()), 20 - minecraft.fontRendererObj.getStringWidth(String.valueOf(recipe.getVis())) / 2, 12, 0);
        }

        @Override
        public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {

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
