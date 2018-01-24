package com.buuz135.thaumicjei.category;

import com.buuz135.thaumicjei.ThaumicJEI;
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
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.CrucibleRecipe;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrucibleCategory extends BlankRecipeCategory<CrucibleCategory.CrucibleWrapper> {

    public static final String UUID = "THAUMCRAFT_CRUCIBLE";

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
        return helper.createBlankDrawable(108, 138);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrucibleWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, false, 51 -6, 8);
        recipeLayout.getItemStacks().set(0, ingredients.getOutputs(ItemStack.class).get(0));

        recipeLayout.getItemStacks().init(1, true, -8,2);
        recipeLayout.getItemStacks().set(1, ingredients.getInputs(ItemStack.class).get(0));
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(new ResourceLocation(ThaumicJEI.MOD_ID, "textures/gui/gui_researchbook_overlay.png"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 2, 5, 109,129, 512, 512);
        Gui.drawModalRectWithCustomSizedTexture(6, 6, 199, 168, 26,26, 512, 512);
    }

    public static class CrucibleWrapper extends BlankRecipeWrapper{

        private final CrucibleRecipe recipe;
        private static final int ASPECT_Y = 66;
        private static final int ASPECT_X = 56;

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
            ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int space = 22;
            int center = (recipe.aspects.size()*space)/2;
            int x = 0;
            for (Aspect aspect : recipe.aspects.getAspectsSortedByAmount()){
                minecraft.renderEngine.bindTexture(aspect.getImage());
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                Color c = new Color(aspect.getColor());
                GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
                Gui.drawModalRectWithCustomSizedTexture(ASPECT_X-center+x*space, ASPECT_Y, 0,0,16,16,16,16);
                GL11.glColor4f(1,1,1,1);
                GL11.glScaled(0.5,0.5,0.5);
                minecraft.currentScreen.drawString(minecraft.fontRendererObj, TextFormatting.WHITE+""+recipe.aspects.getAmount(aspect), 28+(ASPECT_X-center+x*space)*2, ASPECT_Y*2+26, 0);
                GL11.glPopMatrix();
                ++x;
            }
        }

        @Nullable
        @Override
        public List<String> getTooltipStrings(int mouseX, int mouseY) {
            if (mouseY > ASPECT_Y && mouseY < ASPECT_Y + 16){
                int space = 22;
                int center = (recipe.aspects.size()*space)/2;
                int x = 0;
                for (Aspect aspect : recipe.aspects.getAspectsSortedByAmount()){
                    if (mouseX > ASPECT_X-center+x*space && mouseX < ASPECT_X-center+x*space +16){
                        return Arrays.asList(TextFormatting.AQUA+aspect.getName(), TextFormatting.GRAY+aspect.getLocalizedDescription());
                    }
                    ++x;
                }
            }
            return Arrays.asList();
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
