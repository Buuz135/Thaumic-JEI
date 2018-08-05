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
package com.buuz135.thaumicjei.gui;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import thaumcraft.client.gui.GuiResearchTable;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ResearchTableAdvancedGuiHandler implements IAdvancedGuiHandler<GuiResearchTable> {

    @Override
    public Class<GuiResearchTable> getGuiContainerClass() {
        return GuiResearchTable.class;
    }

    @Nullable
    @Override
    public List<Rectangle> getGuiExtraAreas(GuiResearchTable guiContainer) {
        List<Rectangle> rectangles = new ArrayList<>();
        if (guiContainer.tempCatTotals.size() > 0)
            rectangles.add(new Rectangle(guiContainer.getXSize() + guiContainer.getGuiLeft(), guiContainer.getGuiTop(), 16 * 4, 30 + 16 * guiContainer.tempCatTotals.size()));
        if (guiContainer.cardChoices.size() == 3)
            rectangles.add(new Rectangle(guiContainer.getXSize() + guiContainer.getGuiLeft(), guiContainer.getGuiTop(), 16 * 4, guiContainer.getYSize() - 75));
        return rectangles;
    }

    @Nullable
    @Override
    public Object getIngredientUnderMouse(GuiResearchTable guiContainer, int mouseX, int mouseY) {
        return null;
    }
}
