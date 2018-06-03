package com.buuz135.thaumicjei.gui;

import com.buuz135.thaumicjei.ThaumicJEI;
import com.buuz135.thaumicjei.config.ThaumicConfig;
import com.buuz135.thaumicjei.util.AspectUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

/**
 * Modified Thaumcraft code to make the tooltip showing work on non container GUI
 */
@Mod.EventBusSubscriber(modid = ThaumicJEI.MOD_ID, value = Side.CLIENT)
public class AspectTooltipHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tooltipEvent(ItemTooltipEvent event) {
        if (!ThaumicConfig.forceAspectTooltipInAllGUI) return;
        Minecraft mc = FMLClientHandler.instance().getClient();
        GuiScreen gui = mc.currentScreen;
        if (!(gui instanceof GuiContainer) && GuiScreen.isShiftKeyDown() != ModConfig.CONFIG_GRAPHICS.showTags) {
            AspectList tags = ThaumcraftCraftingManager.getObjectTags(event.getItemStack());
            if (tags == null) return;
            int index = 0;
            if (tags.size() > 0) {
                Aspect[] var5 = tags.getAspects();
                int var6 = var5.length;
                for (int var7 = 0; var7 < var6; ++var7) {
                    Aspect tag = var5[var7];
                    if (tag != null) {
                        ++index;
                    }
                }
            }
            int width = index * 18;
            if (width > 0) {
                double sw = (double) mc.fontRenderer.getStringWidth(" ");
                int t = MathHelper.ceil((double) width / sw);
                int l = MathHelper.ceil(18.0D / (double) mc.fontRenderer.FONT_HEIGHT);
                for (int a = 0; a < l; ++a) {
                    event.getToolTip().add("                                                                                                                                            ".substring(0, Math.min(120, t)));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tooltipEvent(RenderTooltipEvent.PostBackground event) {
        if (!ThaumicConfig.forceAspectTooltipInAllGUI) return;
        Minecraft mc = FMLClientHandler.instance().getClient();
        GuiScreen gui = mc.currentScreen;
        if (!(gui instanceof GuiContainer) && GuiScreen.isShiftKeyDown() != ModConfig.CONFIG_GRAPHICS.showTags && !Mouse.isGrabbed()) {
            int bot = event.getHeight();
            if (!event.getLines().isEmpty()) {
                for (int a = event.getLines().size() - 1; a >= 0; --a) {
                    if (event.getLines().get(a) != null && !((String) event.getLines().get(a)).contains("    ")) {
                        bot -= 10;
                    } else if (a > 0 && event.getLines().get(a - 1) != null && ((String) event.getLines().get(a - 1)).contains("    ")) {
                        renderAspectsInGui(gui, mc.player, event.getStack(), bot, event.getX(), event.getY());
                        break;
                    }
                }
            }
        }
    }

    public static void renderAspectsInGui(GuiScreen screen, EntityPlayer player, ItemStack stack, int sd, int sx, int sy) {
        AspectList tags = ThaumcraftCraftingManager.getObjectTags(stack);
        if (tags != null) {
            GL11.glPushMatrix();
            int x = 0;
            int y = 0;
            int index = 0;
            if (tags.size() > 0) {
                Aspect[] var11 = tags.getAspectsSortedByAmount();
                int var12 = var11.length;

                for (int var13 = 0; var13 < var12; ++var13) {
                    Aspect tag = var11[var13];
                    if (tag != null) {
                        x = sx + index * 18;
                        y = sy + sd - 16;
                        AspectUtils.renderAspectList(new AspectList().add(tag, tags.getAmount(tag)), x, y, Minecraft.getMinecraft());
                        ++index;
                    }
                }
            }
            GL11.glPopMatrix();
        }
    }
}
