package com.buuz135.thaumicjei;

import com.buuz135.thaumicjei.category.ArcaneWorkbenchCategory;
import com.buuz135.thaumicjei.category.AspectFromItemStackCategory;
import com.buuz135.thaumicjei.category.CrucibleCategory;
import com.buuz135.thaumicjei.category.InfusionCategory;
import com.buuz135.thaumicjei.ingredient.AspectIngredientFactory;
import com.buuz135.thaumicjei.ingredient.AspectIngredientHelper;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import com.google.common.collect.ArrayListMultimap;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class ThaumcraftJEIPlugin implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
        registry.register(Aspect.class, AspectIngredientFactory.create(), new AspectIngredientHelper(), new AspectIngredientRender());
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
        registry.addRecipeCategoryCraftingItem(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "crucible").toString())), crucibleCategory.getUid());

        InfusionCategory infusionCategory = new InfusionCategory();
        registry.addRecipeCategories(infusionCategory);
        registry.addRecipeHandlers(new InfusionCategory.InfusionHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "infusion_matrix").toString())), infusionCategory.getUid());

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
                    if (recipe instanceof InfusionRecipe) {
                        if (((InfusionRecipe) recipe).recipeInput != null && ((InfusionRecipe) recipe).recipeOutput != null)
                            objects.add(new InfusionCategory.InfusionWrapper((InfusionRecipe) recipe));
                        if (!handledClasses.contains(recipe.getClass().getName()))
                            handledClasses.add(recipe.getClass().getName());
                    }
                }
            }
        }
//        for (String s : unhandledClasses){
//            if (!handledClasses.contains(s)){
//                //System.out.println(s);
//            }
//        }
        registry.addRecipes(objects);


        AspectFromItemStackCategory aspectFromItemStackCategory = new AspectFromItemStackCategory();
        registry.addRecipeCategories(aspectFromItemStackCategory);
        registry.addRecipeHandlers(new AspectFromItemStackCategory.AspectFromItemStackHandler());

        //Aspect cache
        ArrayListMultimap<Aspect, ItemStack> aspectCache = ArrayListMultimap.create();
        for (ItemStack stack : registry.getIngredientRegistry().getIngredients(ItemStack.class)) {
            for (Aspect aspect : AspectHelper.getObjectAspects(stack).getAspectsSortedByAmount()) {
                aspectCache.put(aspect, stack);
            }
        }

        for (Aspect aspect : aspectCache.keySet()) {
            List<ItemStack> itemStacks = aspectCache.get(aspect);
            List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = new ArrayList<>();
            int start = 0;
            while (start < itemStacks.size()) {
                wrappers.add(new AspectFromItemStackCategory.AspectFromItemStackWrapper(aspect, itemStacks.subList(start, Math.min(start + 36, itemStacks.size()))));
                start += 36;
            }
            registry.addRecipes(wrappers);
        }

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }
}
