package com.buuz135.thaumicjei.category;

import com.buuz135.thaumicjei.AlphaDrawable;
import com.buuz135.thaumicjei.ItemStackDrawable;
import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
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
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.AspectList;
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
        return "Infusion Crafting";
    }

    @Override
    public String getModName() {
        return ThaumicJEI.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return new AlphaDrawable(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"), 413, 154, 86, 86, 40, 40, 0, 0);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"));
        GL11.glEnable(3042);
        Gui.drawModalRectWithCustomSizedTexture(27, -7, 40, 6, 32, 32, 512, 512);
        GL11.glDisable(3042);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return new ItemStackDrawable(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "infusion_matrix").toString())));
    }


    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InfusionWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, false, 34, 0);
        recipeLayout.getItemStacks().set(0, ingredients.getOutputs(ItemStack.class).get(0));
        int slot = 1;
        float currentRotation = -90.0F;
        for (List<ItemStack> stacks : ingredients.getInputs(ItemStack.class)) {
            if (slot == 1) recipeLayout.getItemStacks().init(slot, true, 34, 75);
            else
                recipeLayout.getItemStacks().init(slot, true, (int) (MathHelper.cos((float) (currentRotation / 180.0F * Math.PI)) * 40.0F) + 34, (int) (MathHelper.sin(currentRotation / 180.0F * 3.1415927F) * 40.0F) + 75);
            recipeLayout.getItemStacks().set(slot, stacks);
            currentRotation += (360f / recipeWrapper.recipe.getComponents().size());
            ++slot;
        }

        int center = (ingredients.getInputs(AspectList.class).size() * SPACE) / 2;
        int x = 0;
        for (List<AspectList> aspectList : ingredients.getInputs(AspectList.class)) {
            recipeLayout.getIngredientsGroup(AspectList.class).init(x + slot, true, new AspectIngredientRender(), ASPECT_X - center + x * SPACE, ASPECT_Y, 16, 16, 0, 0);
            recipeLayout.getIngredientsGroup(AspectList.class).set(x + slot, aspectList);
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
                ingredients.setOutput(ItemStack.class, recipe.recipeOutput);
            } else if (recipe.recipeOutput != null) {
                for (ItemStack stack : inputs.get(0)) {
                    if (stack != null) {
                        Object[] objects = (Object[]) recipe.recipeOutput;
                        stack.setTagInfo((String) objects[0], (NBTBase) objects[1]);
                        ingredients.setOutput(ItemStack.class, stack);
                    }
                }
            }
            for (Ingredient comp : recipe.getComponents()) {
                inputs.add(Arrays.asList(comp.getMatchingStacks()));
            }
            ingredients.setInputLists(ItemStack.class, inputs);
            ingredients.setInputs(AspectList.class, Arrays.stream(recipe.aspects.getAspectsSortedByAmount()).map(aspect -> new AspectList().add(aspect, recipe.aspects.getAmount(aspect))).collect(Collectors.toList()));
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int instability = Math.min(5, recipe.instability / 2);
            String inst = new TextComponentTranslation("tc.inst").getFormattedText() + new TextComponentTranslation("tc.inst." + instability).getUnformattedText();
            minecraft.fontRenderer.drawString(TextFormatting.DARK_GRAY + inst, -minecraft.fontRenderer.getStringWidth(String.valueOf(instability)) / 2, 165, 0);
        }

        @Override
        public String[] getResearch() {
            return new String[]{recipe.research};
        }
    }

}
