package com.buuz135.thaumicjei;

import com.buuz135.thaumicjei.category.ArcaneWorkbenchCategory;
import com.buuz135.thaumicjei.category.AspectCompoundCategory;
import com.buuz135.thaumicjei.category.CrucibleCategory;
import com.buuz135.thaumicjei.category.InfusionCategory;
import com.buuz135.thaumicjei.ingredient.AspectIngredientFactory;
import com.buuz135.thaumicjei.ingredient.AspectIngredientHelper;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.IThaumcraftRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.client.gui.GuiArcaneWorkbench;
import thaumcraft.common.container.ContainerArcaneWorkbench;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JEIPlugin
public class ThaumcraftJEIPlugin implements IModPlugin {

    public static ArcaneWorkbenchCategory arcaneWorkbenchCategory;
    public static CrucibleCategory crucibleCategory;
    public static InfusionCategory infusionCategory;
    //public static AspectFromItemStackCategory aspectFromItemStackCategory;
    public static AspectCompoundCategory aspectCompoundCategory;
    private IJeiRuntime runtime;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
        registry.register(Aspect.class, AspectIngredientFactory.create(), new AspectIngredientHelper(), new AspectIngredientRender());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        arcaneWorkbenchCategory = new ArcaneWorkbenchCategory(registry.getJeiHelpers().getGuiHelper());
        crucibleCategory = new CrucibleCategory(registry.getJeiHelpers().getGuiHelper());
        infusionCategory = new InfusionCategory();
        //aspectFromItemStackCategory = new AspectFromItemStackCategory();
        aspectCompoundCategory = new AspectCompoundCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(crucibleCategory, infusionCategory, /*aspectFromItemStackCategory,*/ aspectCompoundCategory, arcaneWorkbenchCategory);
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "arcane_workbench").toString())), arcaneWorkbenchCategory.getUid());
        registry.addRecipeClickArea(GuiArcaneWorkbench.class, 108, 56, 32, 32, arcaneWorkbenchCategory.getUid());
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerArcaneWorkbench.class, arcaneWorkbenchCategory.getUid(), 1, 9, 16, 36);

        registry.addRecipeCatalyst(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "crucible").toString())), crucibleCategory.getUid());

        registry.addRecipeCatalyst(new ItemStack(Block.getBlockFromName(new ResourceLocation("thaumcraft", "infusion_matrix").toString())), infusionCategory.getUid());

        List<CrucibleCategory.CrucibleWrapper> crucibleWrappers = new ArrayList<>();
        List<InfusionCategory.InfusionWrapper> infusionWrappers = new ArrayList<>();
        List<String> handledClasses = new ArrayList<>();
        for (ResourceLocation string : ThaumcraftApi.getCraftingRecipes().keySet()) {
            IThaumcraftRecipe recipe = ThaumcraftApi.getCraftingRecipes().get(string);
            if (recipe instanceof CrucibleRecipe) {
                crucibleWrappers.add(new CrucibleCategory.CrucibleWrapper((CrucibleRecipe) recipe));
                if (!handledClasses.contains(recipe.getClass().getName()))
                    handledClasses.add(recipe.getClass().getName());
            }
            if (recipe instanceof InfusionRecipe) {
                if (((InfusionRecipe) recipe).getRecipeInput() != null && ((InfusionRecipe) recipe).recipeOutput != null)
                    infusionWrappers.add(new InfusionCategory.InfusionWrapper((InfusionRecipe) recipe));
                if (!handledClasses.contains(recipe.getClass().getName()))
                    handledClasses.add(recipe.getClass().getName());
            }
        }
        registry.addRecipes(CraftingManager.REGISTRY.getKeys().stream().map(CraftingManager.REGISTRY::getObject).filter(iRecipe -> iRecipe instanceof IArcaneRecipe).map(iRecipe -> new ArcaneWorkbenchCategory.ArcaneWorkbenchWrapper((IArcaneRecipe) iRecipe)).collect(Collectors.toList()), arcaneWorkbenchCategory.getUid());
        registry.addRecipes(crucibleWrappers, crucibleCategory.getUid());
        registry.addRecipes(infusionWrappers, infusionCategory.getUid());

        //registry.addRecipeCatalyst(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "thaumonomicon").toString())), aspectFromItemStackCategory.getUid());

        //Aspect cache TODO IMPROVE
//        if (ThaumicConfig.enableAspectFromItemStacks) {
//            new Thread(() -> {
//                long time = System.currentTimeMillis();
//                ArrayListMultimap<Aspect, ItemStack> aspectCache = ArrayListMultimap.create();
//                Set<String> checkedItemStacksEmpty = new HashSet<>();
//                ThaumicJEI.LOGGER.info("Adding " + registry.getIngredientRegistry().getIngredients(ItemStack.class).size() + " to the aspect cache");
//                for (ItemStack stack : registry.getIngredientRegistry().getIngredients(ItemStack.class)) {
//                    if (!checkedItemStacksEmpty.contains(stack.getItem().getRegistryName().toString())) {
//                        AspectList list = new AspectList(stack);
//                        if (list.size() > 0) {
//                            for (Aspect aspect : list.getAspects()) {
//                                ItemStack clone = stack.copy();
//                                clone.setCount( list.getAmount(aspect));
//                                aspectCache.put(aspect, clone);
//                            }
//                        } else {
//                            checkedItemStacksEmpty.add(stack.getItem().getRegistryName().toString());
//                        }
//                    }
//                }
//                ThaumicJEI.LOGGER.info("Cached ItemStack aspects in " + (System.currentTimeMillis() - time) + "ms");
//                if (System.currentTimeMillis() - time < 15000){
//                    ThaumicJEI.LOGGER.info("Waiting "+(15000 - (System.currentTimeMillis() - time))+"ms as the itemstack checking was too fast.");
//                    try {
//                        Thread.sleep(15000 - (System.currentTimeMillis() - time));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                time = System.currentTimeMillis();
//                List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = new ArrayList<>();
//                for (Aspect aspect : aspectCache.keySet()) {
//                    List<ItemStack> itemStacks = aspectCache.get(aspect);
//                    int start = 0;
//                    while (start < itemStacks.size()) {
//                        wrappers.add(new AspectFromItemStackCategory.AspectFromItemStackWrapper(aspect, itemStacks.subList(start, Math.min(start + 36, itemStacks.size()))));
//                        start += 36;
//                    }
//                }
//                if (runtime != null) {
//                    //runtime.getRecipeRegistry().addRecipe(wrappers,aspectFromItemStackCategory.getUid()); TODO
//                } else {
//                    registry.addRecipes(wrappers,aspectFromItemStackCategory.getUid());
//                }
//                ThaumicJEI.LOGGER.info("Created recipes " + (System.currentTimeMillis() - time) + "ms");
//            }, "ThaumicJEI Aspect Cache").start();
//        }

        registry.addRecipeCatalyst(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "thaumonomicon").toString())), aspectCompoundCategory.getUid());
        List<AspectCompoundCategory.AspectCompoundWrapper> compoundWrappers = new ArrayList<>();
        for (Aspect aspect : Aspect.getCompoundAspects()) {
            compoundWrappers.add(new AspectCompoundCategory.AspectCompoundWrapper(aspect));
        }
        registry.addRecipes(compoundWrappers, aspectCompoundCategory.getUid());

        registry.addIngredientInfo(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "salis_mundus").toString())), ItemStack.class, "To create Salis Mundis, take 3 Vis Crystals of differing types and combine them with Redstone Dust by crafting them with a flint and a bowl.");
        registry.addIngredientInfo(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "triple_meat_treat").toString())), ItemStack.class, "To create the Triple Meat Treat, take 3 different kinds of meat nuggets (produced by cooking meat in the Infernal Furnace) and mix them with sugar.");

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        this.runtime = jeiRuntime;

        CraftingManager.REGISTRY.getKeys().stream().map(CraftingManager.REGISTRY::getObject).filter(iRecipe -> iRecipe instanceof IArcaneRecipe && jeiRuntime.getRecipeRegistry().getRecipeWrapper(iRecipe, VanillaRecipeCategoryUid.CRAFTING) != null).forEach(iRecipe -> jeiRuntime.getRecipeRegistry().hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(iRecipe, VanillaRecipeCategoryUid.CRAFTING)));
    }

//    public void createAspectsFile(List<ItemStack> items){
//        ArrayListMultimap<Aspect, ItemStack> aspectCache = ArrayListMultimap.create();
//        for (ItemStack stack : items){
//            AspectList list = new AspectList(stack);
//            if (list.size() > 0){
//                for (Aspect aspect : list.getAspects()) {
//                    ItemStack clone = stack.copy();
//                    clone.setCount( list.getAmount(aspect));
//                    aspectCache.put(aspect, clone);
//                }
//            }
//        }
//
//    }
}
