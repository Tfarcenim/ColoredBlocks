package com.tfar.simplecoloredblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
@SuppressWarnings("unused")
public class ColorHandler {

  @SubscribeEvent
  public static void registerBlockColors(ColorHandlerEvent.Block event) {
    BlockColors colors = event.getBlockColors();
    final IBlockColor compressedColor = (state, blockAccess, pos, tintIndex) -> getColor(state);
    for (Block block : SimpleColoredBlocks.MOD_BLOCKS)
      colors.register(compressedColor, block);
  }
  @SubscribeEvent
  public static void registerItemColors(final ColorHandlerEvent.Item event) {
    final ItemColors itemColors = event.getItemColors();
    final BlockColors blockColors = event.getBlockColors();


    final IItemColor itemBlockColor = (stack, tintIndex) -> {
      final BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
      return blockColors.getColor(state, null, null,0);
    };
    for (Block block : SimpleColoredBlocks.MOD_BLOCKS)
      itemColors.register(itemBlockColor, block);

    final IItemColor itemColor = ColorHandler::getColor;
    itemColors.register(itemColor,SimpleColoredBlocks.color_wheel);
  }

  private static int getColorFromWheel(ItemStack stack){
    return (stack.getOrCreateTag().getInt("red") * (0x100/Configs.GRANULARITY) << 16)
            + (stack.getOrCreateTag().getInt("green") * (0x100/Configs.GRANULARITY) << 8)
            + stack.getOrCreateTag().getInt("blue") * (0x100/Configs.GRANULARITY);
  }

  private static int getColor(BlockState state){
    return (((SimpleBlock) state.getBlock()).r * (0x100 / Configs.GRANULARITY) << 16)
            + (((SimpleBlock) state.getBlock()).g * (0x100 / Configs.GRANULARITY) << 8)
            + ((SimpleBlock) state.getBlock()).b * (0x100 / Configs.GRANULARITY);
  }

  private static int getColor(ItemStack stack, int tintIndex) {
    return getColorFromWheel(stack);
  }
}
