package com.tfar.simplecoloredblocks.recipe;

import com.tfar.simplecoloredblocks.ItemColorWheel;
import com.tfar.simplecoloredblocks.SimpleColoredBlocks;
import com.tfar.simplecoloredblocks.block.SimpleBlock;
import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRecipe extends SpecialRecipe {

  public AbstractRecipe(ResourceLocation p_i48169_1_) {
    super(p_i48169_1_);
  }

  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv
   * @param world
   */
  @Override
  public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
    List<ItemStack> notemptyitemstacks = new ArrayList<>();

    for(int j = 0; j < inv.getSizeInventory(); ++j) {
      ItemStack stack = inv.getStackInSlot(j);
      if (!stack.isEmpty())
        notemptyitemstacks.add(stack);
    }
    if (notemptyitemstacks.size() != 2)
      return false;

    final ItemStack stack0 = notemptyitemstacks.get(0);
    final ItemStack stack1 = notemptyitemstacks.get(1);

    final ItemStack colorWheel = getColorWheel(stack0,stack1);
    final ItemStack block = getBlock(stack0,stack1);

    return !block.isEmpty() && !colorWheel.isEmpty();
  }

  /**
   * Returns an Item that is the result of this recipe
   *
   * @param inv
   */
  @Nonnull
  @Override
  public ItemStack getCraftingResult(CraftingInventory inv) {

    List<ItemStack> notemptyitemstacks = new ArrayList<>();

    for(int j = 0; j < inv.getSizeInventory(); ++j) {
      ItemStack stack = inv.getStackInSlot(j);
      if (!stack.isEmpty())
        notemptyitemstacks.add(stack);
    }

    final ItemStack stack0 = notemptyitemstacks.get(0);
    final ItemStack stack1 = notemptyitemstacks.get(1);

    ItemStack colorWheel = getColorWheel(stack0,stack1);

    int[] colors = getColors(colorWheel);
    return setColors(colors);
  }

  /**
   * Used to determine if this recipe can fit in a grid of the given width/height
   *
   * @param width
   * @param height
   */
  @Override
  public boolean canFit(int width, int height) {
    return width * height > 1;
  }

  public static int[] getColors(ItemStack wheel) {
    int[] colors = new int[3];
    colors[0] = wheel.getOrCreateTag().getInt("red");
    colors[1] = wheel.getOrCreateTag().getInt("green");
    colors[2] = wheel.getOrCreateTag().getInt("blue");
    return colors;
  }

  protected abstract ItemStack setColors(int[] colors);
  protected ItemStack getColorWheel(ItemStack stack0, ItemStack stack1){
    return stack0.getItem() instanceof ItemColorWheel ? stack0
            : stack1.getItem() instanceof ItemColorWheel ? stack1 : ItemStack.EMPTY;
  }
  protected abstract ItemStack getBlock(ItemStack stack0, ItemStack stack1);
}
