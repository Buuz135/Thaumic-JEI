package com.buuz135.thaumicjei;


import com.buuz135.thaumicjei.AspectFromItemStacksHelper;
import com.buuz135.thaumicjei.category.*;
import com.buuz135.thaumicjei.config.ThaumicConfig;
import com.buuz135.thaumicjei.ingredient.AspectIngredientFactory;
import com.buuz135.thaumicjei.ingredient.AspectIngredientHelper;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import com.buuz135.thaumicjei.StackSizeComparator;
import com.buuz135.thaumicjei.TCAspectCacheUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.internal.CommonInternals;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.client.gui.GuiArcaneWorkbench;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

@JEIPlugin
public class ThaumcraftJEIPlugin implements IModPlugin {

    private IJeiRuntime runtime;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(Item.getByNameOrId("thaumcraft:crystal_essence"));
        subtypeRegistry.useNbtForSubtypes(Item.getByNameOrId("thaumcraft:phial"));
        subtypeRegistry.useNbtForSubtypes(Item.getByNameOrId("thaumcraft:label"));
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
        registry.addRecipeClickArea(GuiArcaneWorkbench.class, 108, 56, 32, 32, arcaneWorkbenchCategory.getUid());
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerArcaneWorkbench.class, arcaneWorkbenchCategory.getUid(), 1, 9, 16, 36);

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
                    if (!unhandledClasses.contains(recipe.getClass().getName()))
                        unhandledClasses.add(recipe.getClass().getName());
                    if (recipe instanceof IArcaneRecipe) {
                        objects.add(new ArcaneWorkbenchCategory.ArcaneWorkbenchWrapper((IArcaneRecipe) recipe));
                        if (!handledClasses.contains(recipe.getClass().getName()))
                            handledClasses.add(recipe.getClass().getName());
                    }
                    if (recipe instanceof CrucibleRecipe) {
                        objects.add(new CrucibleCategory.CrucibleWrapper((CrucibleRecipe) recipe));
                        if (!handledClasses.contains(recipe.getClass().getName()))
                            handledClasses.add(recipe.getClass().getName());
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
        registry.addRecipeCategoryCraftingItem(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "thaumonomicon").toString())), aspectFromItemStackCategory.getUid());
        ConcurrentHashMap<String,AspectList> startObjectTags = TCAspectCacheUtils.createDeepCopy(CommonInternals.objectTags);
        if (ThaumicConfig.enableAspectFromItemStacks) {
            ThaumicJEI.LOGGER.info("Start checking DiskCache");
            long time = System.currentTimeMillis();
            if(DiskCacheHandler.equal(startObjectTags)){
                ThaumicJEI.LOGGER.info("Check finished in "+ (System.currentTimeMillis() - time) + "ms");
                ThaumicJEI.LOGGER.info("DiskCache valid, loding aspects from DiskCache");
                time = System.currentTimeMillis();
                ThaumicJEI.LOGGER.info("Start loading DiskCache");
                DiskCacheHandler.load();
                ThaumicJEI.LOGGER.info("Loading finished in "+ (System.currentTimeMillis() - time) + "ms");
                ArrayListMultimap<Aspect, ItemStack> aspectCache = AspectFromItemStacksHelper.GenerateAspectCache();
                List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = AspectFromItemStacksHelper.GenerateWrappers(aspectCache);
                registry.addRecipes(wrappers);
            }else{
                ThaumicJEI.LOGGER.info("check finished in:"+ (System.currentTimeMillis() - time) + "ms");
                ThaumicJEI.LOGGER.info("DiskCache invalid");
                if (ThaumicConfig.disableThreading){
                    AspectFromItemStacksHelper.TCCalculation(registry);
                    ArrayListMultimap<Aspect, ItemStack> aspectCache = AspectFromItemStacksHelper.GenerateAspectCache();
                    List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = AspectFromItemStacksHelper.GenerateWrappers(aspectCache);
                    registry.addRecipes(wrappers);
                    DiskCacheHandler.save(startObjectTags);
                } else {
                    new Thread(() -> {
                        long duration;
                        duration=AspectFromItemStacksHelper.TCCalculation(registry);
                        AspectFromItemStacksHelper.waitTill15000(duration);
                        ArrayListMultimap<Aspect, ItemStack> aspectCache = AspectFromItemStacksHelper.GenerateAspectCache();
                        List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = AspectFromItemStacksHelper.GenerateWrappers(aspectCache);
                        if (runtime != null) {
                            for (AspectFromItemStackCategory.AspectFromItemStackWrapper wrapper : wrappers) {
                                runtime.getRecipeRegistry().addRecipe(wrapper);
                            }
                        } else {
                            registry.addRecipes(wrappers);
                        }
                        DiskCacheHandler.save(startObjectTags);
                    }, "ThaumicJEI Aspect Cache").start();
                }
                
            }
        }

        AspectCompoundCategory aspectCompoundCategory = new AspectCompoundCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(aspectCompoundCategory);
        registry.addRecipeHandlers(new AspectCompoundCategory.AspectCompoundHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "thaumonomicon").toString())), aspectCompoundCategory.getUid());
        List<AspectCompoundCategory.AspectCompoundWrapper> compoundWrappers = new ArrayList<>();
        for (Aspect aspect : Aspect.getCompoundAspects()) {
            compoundWrappers.add(new AspectCompoundCategory.AspectCompoundWrapper(aspect));
        }
        registry.addRecipes(compoundWrappers);

        registry.addDescription(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "salis_mundus").toString())), "To create Salis Mundis, take 3 Vis Crystals of differing types and combine them with Redstone Dust by crafting them with a flint and a bowl.");
        registry.addDescription(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "triple_meat_treat").toString())), "To create the Triple Meat Treat, take 3 different kinds of meat nuggets (produced by cooking meat in the Infernal Furnace) and mix them with sugar.");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        this.runtime = jeiRuntime;
    }
}