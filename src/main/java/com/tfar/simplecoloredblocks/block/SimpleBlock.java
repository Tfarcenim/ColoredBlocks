package com.tfar.simplecoloredblocks.block;

import com.tfar.simplecoloredblocks.Configs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
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
    SimpleBlock simpleBlock = (SimpleBlock)Block.getBlockFromItem(stack.getItem());
    int hexcolor = (simpleBlock.r * (0x100/ Configs.GRANULARITY) << 16)
            + (simpleBlock.g * (0x100/ Configs.GRANULARITY) << 8)
            + (simpleBlock.b * (0x100 / Configs.GRANULARITY));

    String str = "#"+Integer.toHexString(hexcolor).toUpperCase();

    tooltip.add(new StringTextComponent(str));
  }@Nonnull
  @Override
  public ITextComponent getNameTextComponent() {
    return new TranslationTextComponent(this.getTranslationKey(),r,g,b);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    return new ArrayList<>(Collections.singletonList(new ItemStack(this)));
  }

  @Nonnull
  @Override
  public String getTranslationKey() {
    return "block.simplecoloredblocks.simple";
  }
}
