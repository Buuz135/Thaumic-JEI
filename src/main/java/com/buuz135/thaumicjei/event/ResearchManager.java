/*
 * This file is part of Hot or Not.
 *
 * Copyright 2018, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.thaumicjei.event;

import com.buuz135.thaumicjei.ThaumcraftJEIPlugin;
import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.category.IHasResearch;
import com.buuz135.thaumicjei.config.ThaumicConfig;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchEvent;

@Mod.EventBusSubscriber(modid = ThaumicJEI.MOD_ID, value = Side.CLIENT)
public class ResearchManager {

    private static int timeToSync = -1;

    @SubscribeEvent
    public static void onResearch(ResearchEvent.Research event) {
        Minecraft.getMinecraft().addScheduledTask(() -> sync(event.getResearchKey()));
    }

    @SubscribeEvent
    public static void onJoin(WorldEvent.Load event) {
        if (event.getWorld() instanceof WorldClient) {
            timeToSync = 20;
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (timeToSync >= 0 && event.phase == TickEvent.Phase.END) {
            if (timeToSync == 0) sync("");
            timeToSync--;
        }
    }

    private static void sync(String current) {
        if (!ThaumicConfig.hideRecipesIfMissingResearch) return;
        if (Minecraft.getMinecraft().player == null) {
            timeToSync = 20;
            return;
        }
        if (Minecraft.getMinecraft().player.hasCapability(ThaumcraftCapabilities.KNOWLEDGE, null)) {
            for (String uuid : new String[]{ThaumcraftJEIPlugin.arcaneWorkbenchCategory.getUid(), ThaumcraftJEIPlugin.crucibleCategory.getUid(), ThaumcraftJEIPlugin.infusionCategory.getUid()}) {
                if (ThaumcraftJEIPlugin.runtime.getRecipeRegistry().getRecipeCategory(uuid) != null) {
                    for (Object o : ThaumcraftJEIPlugin.runtime.getRecipeRegistry().getRecipeWrappers(ThaumcraftJEIPlugin.runtime.getRecipeRegistry().getRecipeCategory(uuid))) {
                        if (o != null)
                            ThaumcraftJEIPlugin.runtime.getRecipeRegistry().hideRecipe((IRecipeWrapper) o, uuid);
                    }
                }
            }
            IPlayerKnowledge knowledge = Minecraft.getMinecraft().player.getCapability(ThaumcraftCapabilities.KNOWLEDGE, null);
            if (knowledge == null) {
                timeToSync = 100;
                return;
            }
            for (IRecipeWrapper recipeWrapper : ThaumcraftJEIPlugin.recipes.keySet()) {
                if (recipeWrapper instanceof IHasResearch) {
                    boolean hasAll = true;
                    for (String research : ((IHasResearch) recipeWrapper).getResearch()) {
                        for (String subResearch : research.split("&&")) {
                            if (current.equalsIgnoreCase(subResearch)) continue;
                            if (!knowledge.isResearchComplete(subResearch)) {
                                hasAll = false;
                                break;
                            }
                        }
                    }
                    if (hasAll) {
                        ThaumcraftJEIPlugin.runtime.getRecipeRegistry().unhideRecipe(recipeWrapper, ThaumcraftJEIPlugin.recipes.get(recipeWrapper));
                    }
                }
            }
        }
    }
}
