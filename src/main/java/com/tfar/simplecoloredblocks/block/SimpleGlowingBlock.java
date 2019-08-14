package com.tfar.simplecoloredblocks.block;

import javax.annotation.Nonnull;

public class SimpleGlowingBlock extends SimpleBlock {

  public SimpleGlowingBlock(Properties properties, int r, int g, int b) {
    super(properties,r,g,b);
  }

  @Nonnull
  @Override
  public String getTranslationKey() {
    return "block.simplecoloredblocks.glowing";
  }

}
