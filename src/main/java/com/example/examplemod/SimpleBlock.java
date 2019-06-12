package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SimpleBlock extends Block {

  public final int r,g,b;
  public SimpleBlock(Properties properties, int r, int g, int b) {
    super(properties);

    this.r = r;
    this.g = g;
    this.b = b;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    String hexcolor = "#"+Integer.toString((((SimpleBlock)((BlockItem)stack.getItem()).getBlock()).r<<20) + (((SimpleBlock)((BlockItem)stack.getItem()).getBlock()).g<<12) + (((SimpleBlock)((BlockItem)stack.getItem()).getBlock()).b<<4),16);
    tooltip.add(new StringTextComponent(hexcolor));
  }
}
