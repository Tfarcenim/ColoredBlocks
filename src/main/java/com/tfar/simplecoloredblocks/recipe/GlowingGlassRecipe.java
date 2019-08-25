package com.tfar.simplecoloredblocks.recipe;

import com.tfar.simplecoloredblocks.SimpleColoredBlocks;
import com.tfar.simplecoloredblocks.block.SimpleGlowingGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class GlowingGlassRecipe extends AbstractRecipe {
  public GlowingGlassRecipe(ResourceLocation p_i48169_1_) {
    super(p_i48169_1_);
  }

  @Override
  protected ItemStack setColors(int[] colors) {
    String name = colors[0] + "r_" + colors[1] + "g_" + colors[2] + "b_glowing_glass";
    ResourceLocation resourceLocation = new ResourceLocation(SimpleColoredBlocks.MODID,name);
    return new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
  }

  @Override
  protected ItemStack getBlock(ItemStack stack0, ItemStack stack1) {
    return Block.getBlockFromItem(stack0.getItem()).getClass() == SimpleGlowingGlassBlock.class ? stack0 : Block.getBlockFromItem(stack1.getItem()).getClass() == SimpleGlowingGlassBlock.class ? stack1 : ItemStack.EMPTY;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SimpleColoredBlocks.GLOWING_GLASS;
  }
}
