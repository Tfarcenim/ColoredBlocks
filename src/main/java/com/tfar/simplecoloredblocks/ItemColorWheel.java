package com.tfar.simplecoloredblocks;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemColorWheel extends Item {


  public ItemColorWheel(Properties properties) {
    super(properties);
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {

    if (!world.isRemote) {
      NetworkHooks.openGui((ServerPlayerEntity) player, new Handler(), data -> data.writeBlockPos(player.getPosition()));
    }
    return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

    if (!stack.hasTag())return;

    tooltip.add(new StringTextComponent("Red: "+ stack.getOrCreateTag().getInt("red")));
    tooltip.add(new StringTextComponent("Green: "+ stack.getOrCreateTag().getInt("green")));
    tooltip.add(new StringTextComponent("Blue: "+ stack.getOrCreateTag().getInt("blue")));
  }

  @Override
  public boolean hasContainerItem(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getContainerItem(ItemStack itemStack) {
    return itemStack.copy();
  }
}


