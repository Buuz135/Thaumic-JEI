package com.buuz135.thaumicjei;

import com.google.gson.stream.*;
import com.google.gson.*;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import java.io.IOException;
   
   
public class AspectListAdapter extends TypeAdapter<AspectList> {
    public AspectList read(JsonReader reader) throws IOException {
        AspectList list = new AspectList();
        reader.beginObject();
        reader.nextName();
        reader.beginArray();
        while(reader.hasNext()){
            reader.beginObject();
            list.add(Aspect.getAspect(reader.nextName()), reader.nextInt());
            reader.endObject();
        }
        reader.endArray();
        reader.endObject();
        return list;
    }
    
    public void write(JsonWriter writer, AspectList list) throws IOException {
        writer.beginObject();
        writer.name("Aspects");
        writer.beginArray();
        for(Aspect a : list.getAspects()){
            writer.beginObject();
            writer.name(a.getTag());
            writer.value(list.getAmount(a));
            writer.endObject();
        }
        writer.endArray();
        writer.endObject();
    }
}