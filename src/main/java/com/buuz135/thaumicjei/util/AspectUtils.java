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
