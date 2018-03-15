package com.buuz135.thaumicjei;

import com.buuz135.thaumicjei.AspectFromItemStacksHelper;
import com.buuz135.thaumicjei.category.*;
import com.buuz135.thaumicjei.config.ThaumicConfig;
import com.buuz135.thaumicjei.ingredient.AspectIngredientFactory;
import com.buuz135.thaumicjei.ingredient.AspectIngredientHelper;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import com.buuz135.thaumicjei.StackSizeComparator;
import com.google.common.collect.ArrayListMultimap;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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

class AspectFromItemStacksHelper{
    public static long TCCalculation(IModRegistry registry){
        long time = System.currentTimeMillis();
        ThaumicJEI.LOGGER.info("Thaumcraft calculating aspects from ItemStacks in the IngredientRegistry");
        for (ItemStack stack : registry.getIngredientRegistry().getIngredients(ItemStack.class).asList()) {
            thaumcraft.common.lib.crafting.ThaumcraftCraftingManager.generateTags(stack);
        }
        ThaumicJEI.LOGGER.info("Thaumcraft calculated aspects in " + (System.currentTimeMillis() - time) + "ms");
        return (System.currentTimeMillis() - time);
    }
    
    
    public static ArrayListMultimap<Aspect, ItemStack> GenerateAspectCache(){
        ArrayListMultimap<Aspect, ItemStack> aspectCache = ArrayListMultimap.create();
        long time = System.currentTimeMillis();
        int isCounter = 0;
        ThaumicJEI.LOGGER.info("Adding " + CommonInternals.objectTags.mappingCount() + " potential items to the aspect cache");
        try{
            //reflection to prevent one unneccsary .serializeNBT().toString() call
            Method cA = ThaumcraftCraftingManager.class.getDeclaredMethod("capAspects",new Class[]{AspectList.class,int.class});
            cA.setAccessible(true);
            Method gBT = ThaumcraftCraftingManager.class.getDeclaredMethod("getBonusTags",new Class[]{ItemStack.class,AspectList.class});
            gBT.setAccessible(true);
            for(String s : CommonInternals.objectTags.keySet()){
                try{
                    ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound)JsonToNBT.getTagFromJson(s));
                    isCounter++;
                    AspectList list = CommonInternals.objectTags.get(s); 
                    list = (AspectList)cA.invoke(null, new Object[] {gBT.invoke(null, new Object[] {stack, list}), 500});
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
        } catch (Exception e){
            e.printStackTrace();
        }
        ThaumicJEI.LOGGER.info("Cached " + isCounter + " real ItemStack aspects in " + (System.currentTimeMillis() - time) + "ms");
        return aspectCache;
    }
    
    
    public static List<AspectFromItemStackCategory.AspectFromItemStackWrapper> GenerateWrappers(ArrayListMultimap<Aspect,ItemStack> aspectCache){
        long time = System.currentTimeMillis();
        List<AspectFromItemStackCategory.AspectFromItemStackWrapper> wrappers = new ArrayList<>();
        for (Aspect aspect : aspectCache.keySet()) {
            List<ItemStack> itemStacks = aspectCache.get(aspect);
            Collections.sort(itemStacks, new StackSizeComparator());
            int start = 0;
            while (start < itemStacks.size()) {
                wrappers.add(new AspectFromItemStackCategory.AspectFromItemStackWrapper(aspect, itemStacks.subList(start, Math.min(start + 36, itemStacks.size()))));
                start += 36;
            }
        }
        ThaumicJEI.LOGGER.info("Created recipes " + (System.currentTimeMillis() - time) + "ms");
        return wrappers;
    }
    
    
    public static void waitTill15000(long duration){
        if (15000>duration){
            ThaumicJEI.LOGGER.info("That took less than 15s, waiting " + (15000-duration) + "ms");
            try {
               Thread.sleep(15000-duration);
            }catch(InterruptedException e){}
        }
    }
}