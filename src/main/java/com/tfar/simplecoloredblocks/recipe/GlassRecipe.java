package com.tfar.simplecoloredblocks.recipe;

import com.tfar.simplecoloredblocks.SimpleColoredBlocks;
import com.tfar.simplecoloredblocks.block.SimpleGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class GlassRecipe extends AbstractRecipe {
  public GlassRecipe(ResourceLocation idIn) {
    super(idIn);
  }


  public ItemStack setColors(int[] colors){
    String name = colors[0] + "r_" + colors[1] + "g_" + colors[2] + "b_glass";
    ResourceLocation resourceLocation = new ResourceLocation(SimpleColoredBlocks.MODID,name);
    return new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
  }

  public ItemStack getBlock(ItemStack stack0, ItemStack stack1) {
    return Block.getBlockFromItem(stack0.getItem()).getClass() == SimpleGlassBlock.class ? stack0 : Block.getBlockFromItem(stack1.getItem()).getClass() == SimpleGlassBlock.class ? stack1 : ItemStack.EMPTY;
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SimpleColoredBlocks.GLASS;
  }

  @Override
  public boolean canFit(int width, int height) {
    return width * height > 1;
  }
}
