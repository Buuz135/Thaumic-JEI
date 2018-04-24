package com.buuz135.thaumicjei.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import thaumcraft.api.aspects.Aspect;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AspectIngredientHelper implements IIngredientHelper<Aspect> {
    @Override
    public List<Aspect> expandSubtypes(List<Aspect> ingredients) {
        return ingredients;
    }

    @Nullable
    @Override
    public Aspect getMatch(Iterable<Aspect> ingredients, Aspect ingredientToMatch) {
        for (Aspect aspect : ingredients) {
            if (aspect.getName().equalsIgnoreCase(ingredientToMatch.getName())) return aspect;
        }
        return null;
    }

    @Override
    public String getDisplayName(Aspect ingredient) {
        return ingredient.getName();
    }

    @Override
    public String getUniqueId(Aspect ingredient) {
        return ingredient.getName();
    }

    @Override
    public String getWildcardId(Aspect ingredient) {
        return "/";
    }

    @Override
    public String getModId(Aspect ingredient) {
        return "thaumcraft";
    }

    @Override
    public Iterable<Color> getColors(Aspect ingredient) {
        return Arrays.asList(new Color(ingredient.getColor()));
    }

    @Override
    public String getResourceId(Aspect ingredient) {
        return ingredient.getName();
    }

    @Override
    public Aspect copyIngredient(Aspect ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(Aspect ingredient) {
        return "";
    }
}
