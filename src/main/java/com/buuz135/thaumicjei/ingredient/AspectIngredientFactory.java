package com.buuz135.thaumicjei.ingredient;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AspectIngredientFactory {

    public static List<AspectList> create() {
        List<AspectList> aspects = new ArrayList<>();
        aspects.addAll(Aspect.getPrimalAspects().stream().map(aspect -> new AspectList().add(aspect, 1)).collect(Collectors.toList()));
        aspects.addAll(Aspect.getCompoundAspects().stream().map(aspect -> new AspectList().add(aspect, 1)).collect(Collectors.toList()));
        return aspects;
    }

}
