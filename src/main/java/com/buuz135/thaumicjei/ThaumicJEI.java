package com.buuz135.thaumicjei;

import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = ThaumicJEI.MOD_ID,
        name = ThaumicJEI.MOD_NAME,
        version = ThaumicJEI.VERSION,
        dependencies = "required-after:JEI@[1.10.2-3.14.7.420,);required-after:thaumcraft@[6.0BETA3,);"
)
public class ThaumicJEI {

    public static final String MOD_ID = "ThaumicJEI";
    public static final String MOD_NAME = "ThaumicJEI";
    public static final String VERSION = "1.0-SNAPSHOT";

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static ThaumicJEI INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */

}
