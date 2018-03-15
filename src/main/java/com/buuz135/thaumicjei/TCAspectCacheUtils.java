package com.buuz135.thaumicjei;

import java.util.concurrent.ConcurrentHashMap;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.internal.CommonInternals;

class TCAspectCacheUtils{
    public static ConcurrentHashMap<String,AspectList> createDeepCopy(ConcurrentHashMap<String,AspectList> TCAspectCache){
        ConcurrentHashMap<String,AspectList> copy = new ConcurrentHashMap<String,AspectList>();
    
        for(String item : TCAspectCache.keySet()){
            AspectList aspectListCopy = new AspectList();
            aspectListCopy.add(TCAspectCache.get(item));
            copy.put(item, aspectListCopy);
        }
        return copy;
    }
    public static boolean areEqual(ConcurrentHashMap<String,AspectList> mapA,ConcurrentHashMap<String,AspectList> mapB){
        AspectList aspectListA;
        AspectList aspectListB;
        if (mapA.size()!=mapB.size()){
            ThaumicJEI.LOGGER.info("One of the TCAspectCaches has a different size than the other one");
            return false;
        }
        if (mapA.isEmpty()){
            return true;
        }
        for(String item : mapA.keySet()){
            aspectListA = mapA.get(item);
            aspectListB = mapB.get(item);
            if(aspectListB==null){
                ThaumicJEI.LOGGER.info("Item: " + item + "is not registered in mapB");
                return false;
            }
            if(aspectListA.aspects.size()!=aspectListA.aspects.size()) {
                ThaumicJEI.LOGGER.info("Item: " + item + "the AspectLists differs in size");
            }
            for (Aspect aspect : aspectListA.aspects.keySet()){
                if((int)aspectListA.aspects.get(aspect)!=(int)aspectListB.aspects.get(aspect)){
                    ThaumicJEI.LOGGER.info("Item: " + item + "has Aspect:" + aspect.getTag());
                    ThaumicJEI.LOGGER.info("In aspectListA = " + aspectListA.aspects.get(aspect));
                    ThaumicJEI.LOGGER.info("In aspectListB = " + aspectListB.aspects.get(aspect));
                    return false;
                }
            }
        }
        return true;
    }
}