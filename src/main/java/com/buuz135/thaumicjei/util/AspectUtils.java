package com.buuz135.thaumicjei.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.AspectList;

import java.awt.*;

public class AspectUtils {

    public static void renderAspectList(AspectList ingredient, int xPosition, int yPosition, Minecraft minecraft) {
        GL11.glPushMatrix();
        minecraft.renderEngine.bindTexture(ingredient.getAspects()[0].getImage());
        GL11.glEnable(3042);
        Color c = new Color(ingredient.getAspects()[0].getColor());
        GL11.glColor4f((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 0, 0, 16, 16, 16, 16);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glScaled(0.5, 0.5, 0.5);
        if (ingredient.getAmount(ingredient.getAspects()[0]) > 1)
            minecraft.currentScreen.drawCenteredString(minecraft.fontRenderer, TextFormatting.WHITE + "" + ingredient.getAmount(ingredient.getAspects()[0]), (xPosition + 16) * 2, (yPosition + 12) * 2, 0);
        GL11.glPopMatrix();
    }
}
