package com.buuz135.thaumicjei;

import com.buuz135.thaumicjei.category.*;
import com.buuz135.thaumicjei.config.ThaumicConfig;
import com.buuz135.thaumicjei.gui.ResearchTableAdvancedGuiHandler;
import com.buuz135.thaumicjei.ingredient.AspectIngredientFactory;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import com.buuz135.thaumicjei.ingredient.AspectListIngredientHelper;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.IThaumcraftRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.client.gui.GuiArcaneWorkbench;
import thaumcraft.common.container.ContainerArcaneWorkbench;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@JEIPlugin
public class ThaumcraftJEIPlugin implements IModPlugin {

    private static final String ASPECT_PATH = "." + File.separator + "config" + File.separator + "thaumicjei_itemstack_aspects.json";

    public static ArcaneWorkbenchCategory arcaneWorkbenchCategory;
    public static CrucibleCategory crucibleCategory;
    public static InfusionCategory infusionCategory;
    public static AspectFromItemStackCategory aspectFromItemStackCategory;
    public static AspectCompoundCategory aspectCompoundCategory;
    private IJeiRuntime runtime;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
        registry.register(AspectList.class, AspectIngredientFactory.create(), new AspectListIngredientHelper(), new AspectIngredientRender());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        arcaneWorkbenchCategory = new ArcaneWorkbenchCategory(registry.getJeiHelpers().getGuiHelper());
        crucibleCategory = new CrucibleCategory(registry.getJeiHelpers().getGuiHelper());
        infusionCategory = new InfusionCategory();
        aspectFromItemStackCategory = new AspectFromItemStackCategory();
        aspectCompoundCategory = new AspectCompoundCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(arcaneWorkbenchCategory, crucibleCategory, infusionCategory, aspectCompoundCategory, aspectFromItemStackCategory);
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

        registry.addRecipeCatalyst(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "thaumonomicon").toString())), aspectFromItemStackCategory.getUid());

        if (ThaumicConfig.enableAspectFromItemStacks) {
            File aspectFile = new File(ASPECT_PATH);
            if (!aspectFile.exists() || ThaumicConfig.alwaysRecreateAspectFromItemStackFile) {
                new Thread(() -> {
                    ThaumicJEI.LOGGER.info("Starting Aspect ItemStack Thread.");
                    ThaumicJEI.LOGGER.info("Trying to cache " + registry.getIngredientRegistry().getAllIngredients(ItemStack.class).size() + " aspects.");
                    createAspectsFile(new ArrayList<>(registry.getIngredientRegistry().getAllIngredients(ItemStack.class)));
                    ThaumicJEI.LOGGER.info("Finished Aspect ItemStack Thread.");
                }, "ThaumicJEI Aspect Cache").start();
            }
            if (aspectFile.exists()) {
                try {
                    long time = System.currentTimeMillis();
                    AspectCache[] aspectCaches = new Gson().fromJson(new FileReader(aspectFile), AspectCache[].class);
                    List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = new ArrayList<>();
                    for (AspectCache aspectCache : aspectCaches) {
                        Aspect aspect = Aspect.getAspect(aspectCache.aspect);
                        if (aspect == null) continue;
                        List<ItemStack> items = aspectCache.items.stream().map(s -> {
                            try {
                                return JsonToNBT.getTagFromJson(s);
                            } catch (NBTException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }).filter(Objects::nonNull).map(compound -> new ItemStack(compound)).filter(stack -> !stack.isEmpty()).sorted(Comparator.comparing(ItemStack::getCount).reversed()).collect(Collectors.toList());
                        ;
                        int start = 0;
                        while (start < items.size()) {
                            wrappers.add(new AspectFromItemStackCategory.AspectFromItemStackWrapper(new AspectList().add(aspect, 1), items.subList(start, Math.min(start + 36, items.size()))));
                            start += 36;
                        }
                    }
                    registry.addRecipes(wrappers, aspectFromItemStackCategory.getUid());
                    ThaumicJEI.LOGGER.info("Parsed aspect file in " + (System.currentTimeMillis() - time) + "ms");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        registry.addRecipeCatalyst(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "thaumonomicon").toString())), aspectCompoundCategory.getUid());
        List<AspectCompoundCategory.AspectCompoundWrapper> compoundWrappers = new ArrayList<>();
        for (Aspect aspect : Aspect.getCompoundAspects()) {
            compoundWrappers.add(new AspectCompoundCategory.AspectCompoundWrapper(aspect));
        }
        registry.addRecipes(compoundWrappers, aspectCompoundCategory.getUid());

        registry.addIngredientInfo(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "salis_mundus").toString())), ItemStack.class, "To create Salis Mundis, take 3 Vis Crystals of different types and combine them with Redstone Dust by crafting them with a flint and a bowl.");
        registry.addIngredientInfo(new ItemStack(Item.getByNameOrId(new ResourceLocation("thaumcraft", "triple_meat_treat").toString())), ItemStack.class, "To create the Triple Meat Treat, take 3 different kinds of meat nuggets (produced by cooking meat in the Infernal Furnace) and mix them with sugar.");

        registry.addAdvancedGuiHandlers(new ResearchTableAdvancedGuiHandler());
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        this.runtime = jeiRuntime;

        CraftingManager.REGISTRY.getKeys().stream().map(CraftingManager.REGISTRY::getObject).filter(iRecipe -> iRecipe instanceof IArcaneRecipe && jeiRuntime.getRecipeRegistry().getRecipeWrapper(iRecipe, VanillaRecipeCategoryUid.CRAFTING) != null).forEach(iRecipe -> jeiRuntime.getRecipeRegistry().hideRecipe(jeiRuntime.getRecipeRegistry().getRecipeWrapper(iRecipe, VanillaRecipeCategoryUid.CRAFTING)));
    }

    public void createAspectsFile(Collection<ItemStack> items) {
        List<String> blacklist = Arrays.asList(ThaumicConfig.blacklistedFromAspectChecking);
        int prevAmount = items.size();
        items.removeIf(stack -> blacklist.contains(stack.getItem().getRegistryName().toString()));
        ThaumicJEI.LOGGER.info("Removed " + (prevAmount - items.size()) + " items that are blacklisted");
        int cachedAmount = 0;
        long lastTimeChecked = System.currentTimeMillis();
        HashMap<Aspect, AspectCache> aspectCacheHashMap = Maps.newHashMap();
        for (ItemStack stack : items) {
            AspectList list = new AspectList(stack);
            if (list.size() > 0) {
                for (Aspect aspect : list.getAspects()) {
                    ItemStack clone = stack.copy();
                    clone.setCount(list.getAmount(aspect));
                    AspectCache cache = aspectCacheHashMap.getOrDefault(aspect, new AspectCache(aspect.getTag()));
                    cache.items.add(clone.serializeNBT().toString());
                    aspectCacheHashMap.put(aspect, cache);
                }
            }
            ++cachedAmount;
            if (lastTimeChecked + 5000 < System.currentTimeMillis()) {
                lastTimeChecked = System.currentTimeMillis();
                ThaumicJEI.LOGGER.info("ItemStack Aspect checking at " + (100 * cachedAmount / items.size()) + "%");
            }
        }
        try {
            File output = new File(ASPECT_PATH);
            output.createNewFile();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(output);
            gson.toJson(aspectCacheHashMap.values(), writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AspectCache {

        private String aspect;
        private List<String> items;

        public AspectCache() {
            this.items = new ArrayList<>();
        }

        public AspectCache(String aspect) {
            this();
            this.aspect = aspect;
        }
    }
}
