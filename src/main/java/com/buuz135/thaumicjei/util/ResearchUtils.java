package com.buuz135.thaumicjei.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchEntry;

import java.util.ArrayList;
import java.util.List;

public class ResearchUtils {

    public static List<String> generateMissingResearchList(String... research) {
        List<String> list = new ArrayList<>();
        list.add(TextFormatting.GOLD + "Missing research:");
        for (String string : research) {
            for (String s : string.split("&&")) {
                if (!ThaumcraftCapabilities.knowsResearch(Minecraft.getMinecraft().player, s)) {
                    ResearchEntry entry = ResearchCategories.getResearch(s.contains("@") ? s.split("@")[0] : s);
                    if (entry != null) list.add("- " + TextFormatting.RED + entry.getLocalizedName());
                    else list.add("- " + TextFormatting.RED + s);
                }
            }

        }
        return list;
    }
}
