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
