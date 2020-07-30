package com.tfar.simplecoloredblocks.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class SimpleGlassBlock extends SimpleBlock {

  public SimpleGlassBlock(Properties properties, int r, int g, int b) {
    super(properties,r,g,b);
  }


  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  @OnlyIn(Dist.CLIENT)
  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.getBlock() instanceof SimpleGlassBlock || super.isSideInvisible(state, adjacentBlockState, side);
  }

  @Nonnull
  @Override
  public String getTranslationKey() {
    return "block.simplecoloredblocks.glass";
  }

}
