package com.tfar.simplecoloredblocks.recipe;

import com.tfar.simplecoloredblocks.ItemColorWheel;
import com.tfar.simplecoloredblocks.SimpleColoredBlocks;
import com.tfar.simplecoloredblocks.block.SimpleBlock;
import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SimpleRecipe extends SpecialRecipe {
  public SimpleRecipe(ResourceLocation idIn) {
    super(idIn);
  }


  public static final ItemStack DEBUG = new ItemStack(Items.NETHER_STAR);
  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv
   * @param world
   */
  @Override
  public boolean matches(@Nonnull CraftingInventory inv,@Nonnull World world) {
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

  public static int[] getColors(ItemStack wheel) {
    int[] colors = new int[3];
    colors[0] = wheel.getOrCreateTag().getInt("red");
    colors[1] = wheel.getOrCreateTag().getInt("green");
    colors[2] = wheel.getOrCreateTag().getInt("blue");
    return colors;
  }

  public static ItemStack setColors(int[] colors){
    String name = colors[0]+"r_"+colors[1]+"g_"+colors[2]+"b_";
    ResourceLocation resourceLocation = new ResourceLocation(SimpleColoredBlocks.MODID,name);
    return new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
  }

  private static ItemStack getColorWheel(ItemStack stack0, ItemStack stack1) {
    return stack0.getItem() instanceof ItemColorWheel ? stack0 : stack1.getItem() instanceof ItemColorWheel ? stack1 : ItemStack.EMPTY;

  }

  private static ItemStack getBlock(ItemStack stack0, ItemStack stack1) {
    return Block.getBlockFromItem(stack0.getItem()).getClass() == SimpleBlock.class ? stack0 : Block.getBlockFromItem(stack1.getItem()).getClass() == SimpleBlock.class ? stack1 : ItemStack.EMPTY;
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SimpleColoredBlocks.SIMPLE;
  }

  @Override
  public boolean canFit(int width, int height) {
    return width * height > 1;
  }
}
