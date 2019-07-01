package com.tfar.simplecoloredblocks.recipe;

import com.tfar.simplecoloredblocks.ItemColorWheel;
import com.tfar.simplecoloredblocks.SimpleBlock;
import com.tfar.simplecoloredblocks.SimpleColoredBlocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ColoredBlocksRecipe extends SpecialRecipe {
  public ColoredBlocksRecipe(ResourceLocation idIn) {
    super(idIn);
  }

  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv
   * @param worldIn
   */
  @Override
  public boolean matches(CraftingInventory inv, World worldIn) {
    List<ItemStack> notemptyitemstacks = new ArrayList<>();

    for(int j = 0; j < inv.getSizeInventory(); ++j) {
      ItemStack stack = inv.getStackInSlot(j);
      if (stack.getItem() instanceof ItemColorWheel ||
              stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBlock)
        notemptyitemstacks.add(stack);
    }
    return notemptyitemstacks.size() == 2 && !bothEqual(notemptyitemstacks.get(0), notemptyitemstacks.get(1));

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
      if (stack.getItem() instanceof ItemColorWheel ||
              stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBlock)
        notemptyitemstacks.add(stack);
    }
    if (notemptyitemstacks.size() != 2)return ItemStack.EMPTY;

    ItemStack stack0 = notemptyitemstacks.get(0);
    ItemStack stack1 = notemptyitemstacks.get(1);

    if (bothEqual(stack0,stack1)) return ItemStack.EMPTY;

    stack0 = getColorWheel(stack0,stack1);

    int type = 0;

    if (stack1.getItem().getRegistryName().getPath().endsWith("glass")) type = 1;

    int[] colors = getcolors(stack0);
    return setColors(colors,type);
  }

  public static int[] getcolors(ItemStack wheel) {
    int[] colors = new int[3];
    colors[0] = wheel.getOrCreateTag().getInt("red");
    colors[1] = wheel.getOrCreateTag().getInt("green");
    colors[2] = wheel.getOrCreateTag().getInt("blue");
    return colors;
  }

  public static ItemStack setColors(int[] colors,int type){
    String name = colors[0]+"r_"+colors[1]+"g_"+colors[2]+"b_";
    if (type == 1)name = name + "glass";
    ResourceLocation resourceLocation = new ResourceLocation(SimpleColoredBlocks.MODID,name);
    return new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
  }

  private static ItemStack getColorWheel(ItemStack stack0, ItemStack stack1) {
    return isColorWheel(stack0) ? stack0 : stack1;
  }

  public static boolean bothEqual(ItemStack stack0, ItemStack stack1){
    return bothSimple(stack0, stack1) || bothColorWheels(stack0, stack1);
  }

  public static boolean bothColorWheels(ItemStack stack0, ItemStack stack1){
    return isColorWheel(stack0) && isColorWheel(stack1);
  }

  public static boolean bothSimple(ItemStack stack0, ItemStack stack1){
   return isSimple(stack0) && isSimple(stack1);
  }

  public static boolean isColorWheel(ItemStack stack){
    return stack.getItem() instanceof ItemColorWheel;
  }

  public static boolean isSimple(ItemStack stack){
    return stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof SimpleBlock;
  }

  @Nonnull
  @Override
  public IRecipeSerializer<?> getSerializer() {
    return SimpleColoredBlocks.RECIPE;
  }

  @Override
  public boolean canFit(int width, int height) {
    return width > 1 || height > 1;
  }
}
