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

    @Config.Comment("If true the Aspect from ItemStack json will be created always on startup, if false if will be only be created if it doesn't exist and ")
    public static boolean alwaysRecreateAspectFromItemStackFile = true;

    @Config.Comment("Allow to render the ItemStacks aspects in all GUI")
    public static boolean forceAspectTooltipInAllGUI = true;

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
