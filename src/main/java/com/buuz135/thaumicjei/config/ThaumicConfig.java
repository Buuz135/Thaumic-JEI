package com.buuz135.thaumicjei.config;

import com.buuz135.thaumicjei.ThaumicJEI;
import net.minecraftforge.common.config.Config;

@Config(modid = ThaumicJEI.MOD_ID)
public class ThaumicConfig {

    @Config.Comment("Allow the scanning of all items in game to show what items can make aspects. WARNING: This can be performance heavy, everything is done in a new thread and it might take a while to appear in game!")
    public static boolean enableAspectFromItemStacks = true;
}
