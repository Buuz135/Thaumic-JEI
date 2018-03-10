package com.buuz135.thaumicjei.config;

import com.buuz135.thaumicjei.ThaumicJEI;
import net.minecraftforge.common.config.Config;

@Config(modid = ThaumicJEI.MOD_ID)
public class ThaumicConfig {

    @Config.Comment("Allow the scanning of all items in game to show what items can make aspects. This can be performance heavy.")
    public static boolean enableAspectFromItemStacks = true;
    @Config.Comment("Enabling this switch adds load time to the pack by diabling threading for AspectFromItemStacks but makes sure everything is loaded from the beginning and prevents a (theoretically) possible bug. If you leave this switch disabled everything will be done in a new thread and might take a while to appear in game.")
    public static boolean disableThreading = false;
}
