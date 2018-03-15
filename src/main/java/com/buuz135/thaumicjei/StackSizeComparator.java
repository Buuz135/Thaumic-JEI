package com.buuz135.thaumicjei;

import java.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

class StackSizeComparator implements Comparator{  
    public int compare(Object o1,Object o2){  
        ItemStack s1=(ItemStack)o1;  
        ItemStack s2=(ItemStack)o2;  
  
        if(s1.stackSize==s2.stackSize) {
            if (Item.getIdFromItem(s1.getItem())==Item.getIdFromItem(s2.getItem())){
                if (s1.getItemDamage()==s2.getItemDamage()){
                    return 0;
                } else if(s1.getItemDamage()<s2.getItemDamage()) {
                    return -1;  
                } else {
                    return 1; 
                }                  
            } else if(Item.getIdFromItem(s1.getItem())<Item.getIdFromItem(s2.getItem())) {
                return -1;  
            } else {
                return 1; 
            }  
        } else if(s1.stackSize>s2.stackSize) {
            return -1;  
        } else {
            return 1; 
        } 
    }  
}  