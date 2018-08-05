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
