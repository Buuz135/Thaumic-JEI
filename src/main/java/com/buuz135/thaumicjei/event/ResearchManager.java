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

    private static int needSync = -1;

    @SubscribeEvent
    public static void onResearch(ResearchEvent.Research event) {
        Minecraft.getMinecraft().addScheduledTask(() -> sync(event.getResearchKey()));
    }

    @SubscribeEvent
    public static void onJoin(WorldEvent.Load event) {
        if (event.getWorld() instanceof WorldClient) {
            needSync = 20;
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (needSync >= 0 && event.phase == TickEvent.Phase.END) {
            if (needSync == 0) sync("");
            needSync--;
        }
    }

    private static void sync(String current) {
        if (!ThaumicConfig.hideRecipesIfMissingResearch) return;
        if (Minecraft.getMinecraft().player.hasCapability(ThaumcraftCapabilities.KNOWLEDGE, null)) {
            for (String uuid : new String[]{ThaumcraftJEIPlugin.arcaneWorkbenchCategory.getUid(), ThaumcraftJEIPlugin.crucibleCategory.getUid(), ThaumcraftJEIPlugin.infusionCategory.getUid()}) {
                ThaumcraftJEIPlugin.runtime.getRecipeRegistry().getRecipeWrappers(ThaumcraftJEIPlugin.runtime.getRecipeRegistry().getRecipeCategory(uuid)).forEach(o -> ThaumcraftJEIPlugin.runtime.getRecipeRegistry().hideRecipe((IRecipeWrapper) o, uuid));
            }
            IPlayerKnowledge knowledge = Minecraft.getMinecraft().player.getCapability(ThaumcraftCapabilities.KNOWLEDGE, null);
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
