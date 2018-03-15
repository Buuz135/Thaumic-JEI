package com.buuz135.thaumicjei;


import com.buuz135.thaumicjei.AspectListAdapter;
import com.buuz135.thaumicjei.TCAspectCacheUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.lang.reflect.Type;
import java.text.Collator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.TreeSet;
import java.util.Collections;
import java.util.ArrayList;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.internal.CommonInternals;


class DiskCacheHandler{
    public static Gson gson;
    private static File modFile = new File("./config/ThaumicJEI/mods.txt");
    private static File startObjectTagFile = new File("./config/ThaumicJEI/startObjectTags.txt");
    private static File cacheFile = new File("./config/ThaumicJEI/cache.txt");

    public static void save(ConcurrentHashMap<String,AspectList> startObjectTags){
        gson = new GsonBuilder()
            .registerTypeAdapter(AspectList.class, new AspectListAdapter())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();
        try{
            new File("./config/ThaumicJEI").mkdir();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            FileWriter fw = new FileWriter(cacheFile);
            fw.write(gson.toJson(CommonInternals.objectTags));
            
            fw.close();
            
            fw = new FileWriter(modFile);
            ArrayList modlist = new ArrayList();
            for (ModContainer modContainer : Loader.instance().getActiveModList()) {
                String mod = modContainer.getModId() + "#" + modContainer.getVersion();
                modlist.add(mod);
                Collections.sort(modlist, Collator.getInstance());
            }
            fw.write(gson.toJson(modlist));
            fw.close();
            
            fw = new FileWriter(startObjectTagFile);
            fw.write(gson.toJson(startObjectTags));
            //fw.flush();
            fw.close();
            /*
            FileReader fr = new FileReader("./startObjectTags.txt");
            Type type = new TypeToken<ConcurrentHashMap<String,AspectList>>(){}.getType();
            if(TCAspectCacheUtils.areEqual(startObjectTags, gson.fromJson(fr, type))){
                ThaumicJEI.LOGGER.info("startObjectTags equal after creation");
            }
            fr.close();*/
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static boolean equal(ConcurrentHashMap<String,AspectList> startObjectTags){
        gson = new GsonBuilder()
            .registerTypeAdapter(AspectList.class, new AspectListAdapter())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();
        if(!modFile.isFile()||!startObjectTagFile.isFile()||!cacheFile.isFile()){
            return false;
        }
        try{            
            FileReader fr = new FileReader(modFile);
            ArrayList modlist = new ArrayList();
            for (ModContainer modContainer : Loader.instance().getActiveModList()) {
                String mod = modContainer.getModId() + "#" + modContainer.getVersion();
                modlist.add(mod);
                Collections.sort(modlist, Collator.getInstance());
            }
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> modlistNew = gson.fromJson(fr, type);
            Collections.sort(modlistNew, Collator.getInstance());
            if(!modlist.containsAll(modlistNew) || !modlistNew.containsAll(modlist)){
                ThaumicJEI.LOGGER.info(gson.toJson(modlist));
                ThaumicJEI.LOGGER.info(gson.toJson(modlistNew));
                ThaumicJEI.LOGGER.info("mods not equal");
                fr.close();
                return false;
            }
            fr.close();
            
            fr = new FileReader(startObjectTagFile);
            type = new TypeToken<ConcurrentHashMap<String,AspectList>>(){}.getType();
            if(!TCAspectCacheUtils.areEqual(startObjectTags, gson.fromJson(fr, type))){
                ThaumicJEI.LOGGER.info("startObjectTags not equal");
                fr.close();
                return false;
            }
            fr.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    
    public static void load(){
        gson = new GsonBuilder()
            .registerTypeAdapter(AspectList.class, new AspectListAdapter())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();
        try{ 
            
            FileReader fr = new FileReader(cacheFile);
            Type type = new TypeToken<ConcurrentHashMap<String,AspectList>>(){}.getType();
            CommonInternals.objectTags = gson.fromJson(fr, type);
            fr.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}