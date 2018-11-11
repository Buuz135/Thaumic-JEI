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
package com.buuz135.thaumicjei.config;

import com.buuz135.thaumicjei.ThaumicJEI;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ThaumicJEI.MOD_ID)
public class ThaumicConfig {

    @Config.RequiresMcRestart
    @Config.Comment("Allow the scanning of all items in game to show what items can make aspects. WARNING: This can be performance heavy but it won't add load time to the pack, everything is done in a new thread and it might take a while to appear in game!")
    public static boolean enableAspectFromItemStacks = true;

    @Config.RequiresMcRestart
    @Config.Comment("Items blacklisted from the checking in the Aspect For ItemStack. Format: 'minecraft:stone'")
    public static String[] blacklistedFromAspectChecking = new String[]{};

    @Config.Comment("If true the Aspect from ItemStack json will be created always on startup, if false if will be only be created if it doesn't exist")
    public static boolean alwaysRecreateAspectFromItemStackFile = true;

    @Config.Comment("Allow to render the ItemStacks aspects in all GUI")
    public static boolean forceAspectTooltipInAllGUI = true;

    @Config.Comment("Hide recipes from JEI if you don't have the research for it")
    public static boolean hideRecipesIfMissingResearch = false;

    @Mod.EventBusSubscriber(modid = ThaumicJEI.MOD_ID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ThaumicJEI.MOD_ID)) {
                ConfigManager.sync(ThaumicJEI.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }

}
