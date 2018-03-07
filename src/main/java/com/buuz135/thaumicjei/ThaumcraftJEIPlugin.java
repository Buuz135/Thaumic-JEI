package com.buuz135.thaumicjei;

import com.buuz135.thaumicjei.category.*;
import com.buuz135.thaumicjei.config.ThaumicConfig;
import com.buuz135.thaumicjei.ingredient.AspectIngredientFactory;
import com.buuz135.thaumicjei.ingredient.AspectIngredientHelper;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import com.google.common.collect.ArrayListMultimap;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.internal.CommonInternals;
import thaumcraft.client.gui.GuiArcaneWorkbench;
import thaumcraft.common.container.ContainerArcaneWorkbench;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JEIPlugin
public class ThaumcraftJEIPlugin implements IModPlugin {

    private IJeiRuntime runtime;

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

        //Aspect cache
        if (ThaumicConfig.enableAspectFromItemStacks) {
            if (ThaumicConfig.disableThreading){
                long time = System.currentTimeMillis();
                for (ItemStack stack : registry.getIngredientRegistry().getIngredients(ItemStack.class).asList()) {
                    thaumcraft.common.lib.crafting.ThaumcraftCraftingManager.generateTags(stack);
                }
                ThaumicJEI.LOGGER.info("Thaumcraft calculated remaining aspects in " + (System.currentTimeMillis() - time) + "ms");
                ArrayListMultimap<Aspect, ItemStack> aspectCache = ArrayListMultimap.create();
                time = System.currentTimeMillis();
                int isCounter = 0;
                ThaumicJEI.LOGGER.info("Adding " + CommonInternals.objectTags.mappingCount() + " potential items to the aspect cache");        
        
                for(String s : CommonInternals.objectTags.keySet()){
                    //ThaumicJEI.LOGGER.info("s = " + s);
                    try{
                    ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound)JsonToNBT.getTagFromJson(s));
                    isCounter++;
                    AspectList list = CommonInternals.objectTags.get(s);                            
                        if (list.size() > 0) {
                            for (Aspect aspect : list.getAspects()) {
                                ItemStack clone = stack.copy();
                                clone.stackSize = list.getAmount(aspect);
                                aspectCache.put(aspect, clone);
                            }
                        }
                    }catch(Exception e){
                        isCounter--;
                    }
                }
        
                ThaumicJEI.LOGGER.info("Cached " + isCounter + " real ItemStack aspects in " + (System.currentTimeMillis() - time) + "ms");
                time = System.currentTimeMillis();
                List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = new ArrayList<>();
                for (Aspect aspect : aspectCache.keySet()) {
                    List<ItemStack> itemStacks = aspectCache.get(aspect);
                    int start = 0;
                    while (start < itemStacks.size()) {
                        wrappers.add(new AspectFromItemStackCategory.AspectFromItemStackWrapper(aspect, itemStacks.subList(start, Math.min(start + 36, itemStacks.size()))));
                        start += 36;
                    }
                }
                ThaumicJEI.LOGGER.info("Created recipes " + (System.currentTimeMillis() - time) + "ms");
                registry.addRecipes(wrappers);
                
            } else {
            //Threading
                new Thread(() -> {
                    long time = System.currentTimeMillis();
                    for (ItemStack stack : registry.getIngredientRegistry().getIngredients(ItemStack.class).asList()) {
                        thaumcraft.common.lib.crafting.ThaumcraftCraftingManager.generateTags(stack);
                    }
                    ThaumicJEI.LOGGER.info("Thaumcraft calculated remaining aspects in " + (System.currentTimeMillis() - time) + "ms");
                    long waittime = (15000-(System.currentTimeMillis() - time));
                    time = System.currentTimeMillis();
                    if (waittime>0){
                        try {
                           Thread.sleep(waittime);
                        }catch(InterruptedException e){}
                    }
                    ThaumicJEI.LOGGER.info("Waited for " + (System.currentTimeMillis() - time) + "ms");
                    ArrayListMultimap<Aspect, ItemStack> aspectCache = ArrayListMultimap.create();
                    time = System.currentTimeMillis();
                    int isCounter = 0;
                    ThaumicJEI.LOGGER.info("Adding " + CommonInternals.objectTags.mappingCount() + " potential items to the aspect cache");        
        
                    for(String s : CommonInternals.objectTags.keySet()){
                        //ThaumicJEI.LOGGER.info("s = " + s);
                        try{
                        ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound)JsonToNBT.getTagFromJson(s));
                        isCounter++;
                        AspectList list = CommonInternals.objectTags.get(s);                            
                            if (list.size() > 0) {
                                for (Aspect aspect : list.getAspects()) {
                                    ItemStack clone = stack.copy();
                                    clone.stackSize = list.getAmount(aspect);
                                    aspectCache.put(aspect, clone);
                                }
                            }
                        }catch(Exception e){
                            isCounter--;
                        }
                    }
        
                    ThaumicJEI.LOGGER.info("Cached " + isCounter + " real ItemStack aspects in " + (System.currentTimeMillis() - time) + "ms");
                    time = System.currentTimeMillis();
                    List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = new ArrayList<>();
                    for (Aspect aspect : aspectCache.keySet()) {
                        List<ItemStack> itemStacks = aspectCache.get(aspect);
                        int start = 0;
                        while (start < itemStacks.size()) {
                            wrappers.add(new AspectFromItemStackCategory.AspectFromItemStackWrapper(aspect, itemStacks.subList(start, Math.min(start + 36, itemStacks.size()))));
                            start += 36;
                        }
                    }
                    ThaumicJEI.LOGGER.info("Created recipes " + (System.currentTimeMillis() - time) + "ms");
                    if (runtime != null) {
                        for (AspectFromItemStackCategory.AspectFromItemStackWrapper wrapper : wrappers) {
                            runtime.getRecipeRegistry().addRecipe(wrapper);
                        }
                    } else {
                        registry.addRecipes(wrappers);
                    }
                }).start();
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
