package com.tfar.simplecoloredblocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class Handler implements INamedContainerProvider {
  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent("text.simplecoloredblocks.color_wheel_container.title");
  }

  @Nullable
  @Override
  public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
    return new ColorWheelContainer(p_createMenu_1_, p_createMenu_2_,p_createMenu_3_.inventory.currentItem, p_createMenu_3_.getHeldItemMainhand());
  }
}
