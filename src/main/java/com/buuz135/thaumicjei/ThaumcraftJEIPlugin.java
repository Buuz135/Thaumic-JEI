package com.buuz135.thaumicjei;

import com.buuz135.thaumicjei.category.ArcaneWorkbenchCategory;
import com.buuz135.thaumicjei.category.CrucibleCategory;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class ThaumcraftJEIPlugin implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
    }

    @Override
    public void register(IModRegistry registry) {
        ArcaneWorkbenchCategory arcaneWorkbenchCategory = new ArcaneWorkbenchCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(arcaneWorkbenchCategory);
        registry.addRecipeHandlers(new ArcaneWorkbenchCategory.ArcaneWorkbenchHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "arcane_workbench").toString())), arcaneWorkbenchCategory.getUid());

        CrucibleCategory crucibleCategory = new CrucibleCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(crucibleCategory);
        registry.addRecipeHandlers(new CrucibleCategory.CrucibleHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(Blocks.STONE));
        registry.addRecipeCategoryCraftingItem(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "crucible").toString())), crucibleCategory.getUid());

        List<Object> objects = new ArrayList<>();
        List<String> handledClasses = new ArrayList<>();
        List<String> unhandledClasses = new ArrayList<>();
        for (String string : ThaumcraftApi.getCraftingRecipes().keySet()) {
            Object object = ThaumcraftApi.getCraftingRecipes().get(string);
            if (object instanceof Object[]) {
                for (Object recipe : (Object[]) object) {
                    if (!unhandledClasses.contains(recipe.getClass().getName())) unhandledClasses.add(recipe.getClass().getName());
                    if (recipe instanceof IArcaneRecipe) {
                        objects.add(new ArcaneWorkbenchCategory.ArcaneWorkbenchWrapper((IArcaneRecipe) recipe));
                        if (!handledClasses.contains(recipe.getClass().getName())) handledClasses.add(recipe.getClass().getName());
                    }
                    if (recipe instanceof CrucibleRecipe){
                        objects.add(new CrucibleCategory.CrucibleWrapper((CrucibleRecipe) recipe));
                        if (!handledClasses.contains(recipe.getClass().getName())) handledClasses.add(recipe.getClass().getName());
                    }
                }
            }
        }
        for (String s : unhandledClasses){
            if (!handledClasses.contains(s)){
                System.out.println(s);
            }
        }
        registry.addRecipes(objects);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }
}
