package com.tfar.simplecoloredblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class SimpleRedstoneLampBlock extends SimpleBlock {

    public static final BooleanProperty LIT;

    public SimpleRedstoneLampBlock(Properties p_i48343_1_,int r, int g, int b) {
      super(p_i48343_1_,r,g,b);
      this.setDefaultState(this.getDefaultState().with(LIT, false));
    }

    public int getLightValue(BlockState p_149750_1_) {
      return p_149750_1_.get(LIT) ? super.getLightValue(p_149750_1_) : 0;
    }

    public void onBlockAdded(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_, boolean p_220082_5_) {
      super.onBlockAdded(p_220082_1_, p_220082_2_, p_220082_3_, p_220082_4_, p_220082_5_);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
      return this.getDefaultState().with(LIT, p_196258_1_.getWorld().isBlockPowered(p_196258_1_.getPos()));
    }

    public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
      if (!p_220069_2_.isRemote) {
        boolean lvt_7_1_ = p_220069_1_.get(LIT);
        if (lvt_7_1_ != p_220069_2_.isBlockPowered(p_220069_3_)) {
          if (lvt_7_1_) {
            p_220069_2_.getPendingBlockTicks().scheduleTick(p_220069_3_, this, 4);
          } else {
            p_220069_2_.setBlockState(p_220069_3_, p_220069_1_.cycle(LIT), 2);
          }
        }

      }
    }

    public void tick(BlockState p_196267_1_, World p_196267_2_, BlockPos p_196267_3_, Random p_196267_4_) {
      if (!p_196267_2_.isRemote) {
        if (p_196267_1_.get(LIT) && !p_196267_2_.isBlockPowered(p_196267_3_)) {
          p_196267_2_.setBlockState(p_196267_3_, p_196267_1_.cycle(LIT), 2);
        }

      }
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
      p_206840_1_.add(LIT);
    }

    static {
      LIT = RedstoneTorchBlock.LIT;
    }
  }