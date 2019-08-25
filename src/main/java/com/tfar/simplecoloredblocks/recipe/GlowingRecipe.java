package com.tfar.simplecoloredblocks.recipe;

import com.tfar.simplecoloredblocks.SimpleColoredBlocks;
import com.tfar.simplecoloredblocks.block.SimpleGlowingBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class GlowingRecipe extends AbstractRecipe {
  public GlowingRecipe(ResourceLocation idIn) {
    super(idIn);
  }

  public ItemStack setColors(int[] colors){
    String name = colors[0] + "r_" + colors[1] + "g_" + colors[2] + "b_glowing";
    ResourceLocation resourceLocation = new ResourceLocation(SimpleColoredBlocks.MODID,name);
    return new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
  }

  public ItemStack getBlock(ItemStack stack0, ItemStack stack1) {
    return Block.getBlockFromItem(stack0.getItem()).getClass() == SimpleGlowingBlock.class ? stack0 : Block.getBlockFromItem(stack1.getItem()).getClass() == SimpleGlowingBlock.class ? stack1 : ItemStack.EMPTY;
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SimpleColoredBlocks.GLOWING;
  }

}
