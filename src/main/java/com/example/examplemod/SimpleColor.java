package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;

public class SimpleColor {

  public static void registerBlockColors(ColorHandlerEvent.Block event) {
    BlockColors colors = event.getBlockColors();
    final IBlockColor compressedColor = (state, blockAccess, pos, tintIndex) -> getColor(state);
    for (Block block : SimpleColoredBlocks.MOD_BLOCKS)
      colors.register(compressedColor, block);
  }

  public static void registerItemColors(final ColorHandlerEvent.Item event) {
    final ItemColors itemColors = event.getItemColors();
    final BlockColors blockColors = event.getBlockColors();


    final IItemColor itemBlockColor = (stack, tintIndex) -> {
      final BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
      return blockColors.getColor(state, null, null,0);
    };
    for (Block block : SimpleColoredBlocks.MOD_BLOCKS)
      itemColors.register(itemBlockColor, block);

    final IItemColor itemColor = SimpleColor::getColor;

    itemColors.register(itemColor,SimpleColoredBlocks.color_wheel);

  }

  private static int getColorWheel(ItemStack stack){
    return stack.hasTag() ? ((stack.getOrCreateTag().getInt("red") << 20) + (stack.getOrCreateTag().getInt("green") << 12) + (stack.getOrCreateTag().getInt("blue") << 4)) : 0xFFFFFF;
  }

  private static int getColor(BlockState state){
    return (((SimpleBlock)state.getBlock()).r << 20) + (((SimpleBlock)state.getBlock()).g << 12) + (((SimpleBlock)state.getBlock()).b << 4
    );
  }

  private static int getColor(ItemStack stack, int tintIndex) {
    return getColorWheel(stack);
  }
}
