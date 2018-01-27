package com.buuz135.thaumicjei.ingredient;

import thaumcraft.api.aspects.Aspect;

import java.util.ArrayList;
import java.util.List;

public class AspectIngredientFactory {

    public static List<Aspect> create() {
        List<Aspect> aspects = new ArrayList<>();
        aspects.addAll(Aspect.getPrimalAspects());
        aspects.addAll(Aspect.getCompoundAspects());
        return aspects;
    }

}
