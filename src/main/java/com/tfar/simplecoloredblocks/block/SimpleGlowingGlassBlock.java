package com.tfar.simplecoloredblocks.block;

import javax.annotation.Nonnull;

public class SimpleGlowingGlassBlock extends SimpleGlassBlock {
  public SimpleGlowingGlassBlock(Properties properties, int r, int g, int b) {
    super(properties, r, g, b);
  }

  @Nonnull
  @Override
  public String getTranslationKey() {
    return "block.simplecoloredblocks.glowing_glass";
  }
}
