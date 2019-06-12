package com.example.examplemod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Screens {

  private static class ColorWheelContainerProvider implements INamedContainerProvider
  {
    private final int slot;

    private ColorWheelContainerProvider(int slot)
    {
      this.slot = slot;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player)
    {
      ItemStack heldItem = playerInventory.getStackInSlot(slot);

      int blockedSlot = -1;
      if (player.getHeldItemMainhand() == heldItem)
        blockedSlot = playerInventory.currentItem;

      return new ColorWheelContainer(i, playerInventory, blockedSlot, heldItem);
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName()
    {
      return new TranslationTextComponent("text.simplecoloredblocks.color_wheel_container.title");
    }
  }

  private static class ColorWheelSlotContainerProvider implements INamedContainerProvider
  {
    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player)
    {
      return new ColorWheelContainer(i, playerInventory, playerInventory.currentItem, player.getHeldItemMainhand());
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName()
    {
      return new TranslationTextComponent("text.simplecoloredblocks.color_wheel_container.title");
    }
  }

  public static void openWheelScreen(ServerPlayerEntity player, int slot)
  {
    ItemStack heldItem = player.inventory.getStackInSlot(slot);
    if (heldItem.getCount() > 0 && heldItem.getItem() instanceof ItemColorWheel)
    {
      NetworkHooks.openGui(player, new ColorWheelContainerProvider(slot), (data) -> data.writeByte(slot));
    }
  }

  public static void openSlotScreen(ServerPlayerEntity player)
  {
    NetworkHooks.openGui(player, new ColorWheelSlotContainerProvider());
  }
}
