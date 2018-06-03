package com.buuz135.thaumicjei.gui;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import thaumcraft.client.gui.GuiFocalManipulator;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FocalManipulatorAdvancedGuiHandler implements IAdvancedGuiHandler<GuiFocalManipulator> {
    @Override
    public Class<GuiFocalManipulator> getGuiContainerClass() {
        return GuiFocalManipulator.class;
    }

    @Nullable
    @Override
    public List<Rectangle> getGuiExtraAreas(GuiFocalManipulator guiContainer) {
        List<Rectangle> rectangles = new ArrayList<>();
        rectangles.add(new Rectangle(guiContainer.getXSize() + guiContainer.getGuiLeft(), guiContainer.getGuiTop(), 130, guiContainer.getYSize()));
        return rectangles;
    }

    @Nullable
    @Override
    public Object getIngredientUnderMouse(GuiFocalManipulator guiContainer, int mouseX, int mouseY) {
        return null;
    }
}
