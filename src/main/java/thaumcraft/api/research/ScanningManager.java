package thaumcraft.api.research;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

public class ScanningManager {
	
	static ArrayList<IScanThing> things = new ArrayList<IScanThing>();
	
	/**
	 * Add things to scan
	 * @example 
	 * <i>ScanManager.addScannableThing(new ScanItem("HIPSTER",new ItemStack(Items.apple,1,OreDictionary.WILDCARD_VALUE)));</i><br>
	 * This will unlock the <b>HIPSTER</b> research if you scan any kind of apple.
	 */	
	public static void addScannableThing(IScanThing obj) {		
		things.add(obj);		
	}
	
	/**
	 * @param player
	 * @param object this could in theory be anything, but vanilla tc scanning tools only pass in Entity, BlockPos, Itemstack or null
	 */
	public static void scanTheThing(EntityPlayer player, Object object) {
		boolean found = false;
		for (IScanThing thing:things) {
			if (thing.checkThing(player, object)) {						
				if (ThaumcraftApi.internalMethods.progressResearch(player, thing.getResearchKey(player, object))) {					
					found=true;
					thing.onSuccess(player, object);
				}
			}			
		}
		
		if (!found) {
			//REPLACENOTIFICATION
			player.sendMessage(new TextComponentString("\u00a75\u00a7o"+I18n.translateToLocal("tc.unknownobject")));
		} else {
			//REPLACENOTIFICATION
			player.sendMessage(new TextComponentString("\u00a7a\u00a7o"+I18n.translateToLocal("tc.knownobject")));
		}
		
		// scan contents of inventories
		if (object instanceof BlockPos && player.getEntityWorld().getTileEntity((BlockPos) object) instanceof IInventory) {
			IInventory inv = (IInventory) player.getEntityWorld().getTileEntity((BlockPos) object);
			for (int slot=0;slot<inv.getSizeInventory();slot++) {
				ItemStack stack = inv.getStackInSlot(slot);
				if (stack!=null) scanTheThing(player,stack);
			}
			return;
		}
		
	}
	
	/**
	 * @param player
	 * @param object
	 * @return true if the object can be scanned for research the player has not yet discovered
	 */
	public static boolean isThingStillScannable(EntityPlayer player, Object object) {		
		for (IScanThing thing:things) {
			if (thing.checkThing(player, object)) {
				try {
					if (!ThaumcraftCapabilities.knowsResearch(player,thing.getResearchKey(player, object))) {					
						return true;
					}
				} catch (Exception e) {	}
			}			
		}
		return false;
	}
		
	
	public static ItemStack getItemFromParms(EntityPlayer player, Object obj) {
		ItemStack is = null;
		if (obj instanceof ItemStack) 
			is = (ItemStack) obj;
		if (obj instanceof EntityItem && ((EntityItem)obj).getEntityItem()!=null) 
			is = ((EntityItem)obj).getEntityItem();
		if (obj instanceof BlockPos) {
			IBlockState state = player.world.getBlockState((BlockPos) obj);
			is = state.getBlock().getItem(player.world, (BlockPos) obj, state);
			try {
				if (is==null) is = state.getBlock().getPickBlock(state, rayTrace(player), player.world, (BlockPos) obj, player);
			} catch (Exception e) {	}
			// Water and Lava blocks cant be registered as itemstacks anymore, Oh Minecraft!
			try {
				if (is==null && state.getMaterial()==Material.WATER) {
					is = new ItemStack(Items.WATER_BUCKET);
				}
				if (is==null && state.getMaterial()==Material.LAVA) {
					is = new ItemStack(Items.LAVA_BUCKET);
				}
			} catch (Exception e) {e.printStackTrace();	}			
		}
		return is;
	}
	
	private static RayTraceResult rayTrace(EntityPlayer player)
    {
        Vec3d vec3d = player.getPositionEyes(0);
        Vec3d vec3d1 = player.getLook(0);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * 4, vec3d1.yCoord * 4, vec3d1.zCoord * 4);
        return player.world.rayTraceBlocks(vec3d, vec3d2, true, false, true);
    }
}
