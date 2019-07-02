package com.tfar.simplecoloredblocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;

import static com.tfar.simplecoloredblocks.SimpleColoredBlocks.TYPE;

public class ColorWheelContainer extends Container {

  private final ItemStack heldItem;


  public ColorWheelContainer(int id, PlayerInventory playerInventory) {
    this(id, playerInventory, playerInventory.currentItem, playerInventory.getCurrentItem());
  }

  public ColorWheelContainer(int id, PlayerInventory playerInventory, int blockedSlot, ItemStack heldItem) {
    super(TYPE, id);

    this.heldItem = heldItem;

    this.addSlot(new Slot(this.inputSlots, 0, 26, 20) {
      @Override
      public int getSlotStackLimit() {
        return Configs.GRANULARITY - 1;
      }

      @Override
      public boolean isItemValid(ItemStack stack) {
        return stack.getItem().isIn(Tags.Items.DYES_RED);
      }
    });
    this.addSlot(new Slot(this.inputSlots, 1, 36 + 26, 20) {
      @Override
      public int getSlotStackLimit() {
        return Configs.GRANULARITY - 1;
      }
      @Override
      public boolean isItemValid(ItemStack stack) {
        return stack.getItem().isIn(Tags.Items.DYES_GREEN);
      }
    });
    this.addSlot(new Slot(this.inputSlots, 2, 36 * 2 + 26, 20) {
      @Override
      public int getSlotStackLimit() {
        return Configs.GRANULARITY - 1;
      }
      @Override
      public boolean isItemValid(ItemStack stack) {
        return stack.getItem().isIn(Tags.Items.DYES_BLUE);
      }
    });
    bindPlayerInventory(playerInventory, blockedSlot);
  }

  private void bindPlayerInventory(IInventory playerInventory, int blockedSlot) {
    for (int l = 0; l < 3; ++l) {
      for (int j1 = 0; j1 < 9; ++j1) {
        int index = j1 + l * 9 + 9;
        this.addSlot(
                blockedSlot == index
                        ? new Slot(playerInventory, index, 8 + j1 * 18, l * 18 + 51) {
                  @Override
                  public boolean canTakeStack(PlayerEntity playerIn) {
                    return false;
                  }
                }
                        : new Slot(playerInventory, index, 8 + j1 * 18, l * 18 + 51)
        );
      }
    }

    for (int i1 = 0; i1 < 9; ++i1) {
      this.addSlot(
              blockedSlot == i1
                      ? new Slot(playerInventory, i1, 8 + i1 * 18, 109) {
                @Override
                public boolean canTakeStack(PlayerEntity playerIn) {
                  return false;
                }
              }
                      : new Slot(playerInventory, i1, 8 + i1 * 18, 109)
      );
    }
  }

  private final IInventory inputSlots = new Inventory(3) {
    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
     * it hasn't changed and skip it.
     */
    public void markDirty() {
      super.markDirty();
      ColorWheelContainer.this.onCraftMatrixChanged(this);
    }
  };

  /**
   * Called when the container is closed.
   */
  @Override
  public void onContainerClosed(PlayerEntity playerIn) {
    super.onContainerClosed(playerIn);
    if (!playerIn.world.isRemote) {
      ItemStack stack = playerIn.getHeldItemMainhand();
      stack.getOrCreateTag().putInt("red", Math.min(this.inputSlots.getStackInSlot(0).getCount(), 15));
      stack.getOrCreateTag().putInt("green", Math.min(this.inputSlots.getStackInSlot(1).getCount(), 15));
      stack.getOrCreateTag().putInt("blue", Math.min(this.inputSlots.getStackInSlot(2).getCount(), 15));
      this.clearContainer(playerIn, playerIn.world, this.inputSlots);
    }
  }

  /**
   * Take a stack from the specified inventory slot. (Shift-clicking)
   */
  @Override
  @Nonnull
  public ItemStack transferStackInSlot(PlayerEntity player, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (index != 0 && index != 1 && index != 2) {
        if (index < 39 && !this.mergeItemStack(itemstack1, 0, 3, false)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }
      if (itemstack1.getCount() == itemstack.getCount()) {
        return ItemStack.EMPTY;
      }
      slot.onTake(player, itemstack1);
    }

    return itemstack;
  }


  /**
   * Determines whether supplied player can use this container
   *
   * @param playerIn
   */
  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return true;
  }
}
