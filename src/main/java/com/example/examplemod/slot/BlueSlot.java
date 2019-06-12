package com.example.examplemod.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;

public class BlueSlot extends Slot {

    public BlueSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
      super(inventoryIn, index, xPosition, yPosition);
    }

  @Override
  public boolean isItemValid(ItemStack p_75214_1_) {
    return p_75214_1_.getItem().isIn(Tags.Items.DYES_BLUE);
  }

  @Override
  public int getSlotStackLimit() {
    return 15;
  }
}
