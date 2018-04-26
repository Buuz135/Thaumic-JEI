package com.buuz135.thaumicjei.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import thaumcraft.api.aspects.AspectList;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AspectListIngredientHelper implements IIngredientHelper<AspectList> {
    @Override
    public List<AspectList> expandSubtypes(List<AspectList> ingredients) {
        return ingredients;
    }

    @Nullable
    @Override
    public AspectList getMatch(Iterable<AspectList> ingredients, AspectList ingredientToMatch) {
        for (AspectList list : ingredients) {
            if (list.getAspects()[0].getName().equalsIgnoreCase(ingredientToMatch.getAspects()[0].getName()))
                return list;
        }
        return null;
    }

    @Override
    public String getDisplayName(AspectList ingredient) {
        return ingredient.getAspects()[0].getName();
    }

    @Override
    public String getUniqueId(AspectList ingredient) {
        return ingredient.getAspects()[0].getName();
    }

    @Override
    public String getWildcardId(AspectList ingredient) {
        return "/";
    }

    @Override
    public String getModId(AspectList ingredient) {
        return "thaumcraft";
    }

    @Override
    public Iterable<Color> getColors(AspectList ingredient) {
        return Arrays.asList(new Color(ingredient.getAspects()[0].getColor()));
    }

    @Override
    public String getResourceId(AspectList ingredient) {
        return ingredient.getAspects()[0].getName();
    }

    @Override
    public AspectList copyIngredient(AspectList ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(AspectList ingredient) {
        return "";
    }
}
