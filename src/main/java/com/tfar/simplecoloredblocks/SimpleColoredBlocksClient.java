package com.tfar.simplecoloredblocks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tfar.simplecoloredblocks.block.SimpleBlock;
import com.tfar.simplecoloredblocks.block.SimpleGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static com.tfar.simplecoloredblocks.Configs.configFile;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
@SuppressWarnings("unused")
public class SimpleColoredBlocksClient {

  private static final Minecraft mc = Minecraft.getInstance();

  @SubscribeEvent
  public static void setupResourcePack(FMLClientSetupEvent event) {
    ScreenManager.registerFactory(SimpleColoredBlocks.TYPE, ColorWheelScreen::new);
  }

  @SubscribeEvent
  public static void registerBlockColors(final FMLClientSetupEvent event) {
    final BlockColors colors = mc.getBlockColors();
    final IBlockColor compressedColor = (state, blockAccess, pos, tintIndex) -> getColor(state);
    for (final Block block : SimpleColoredBlocks.MOD_BLOCKS)
      colors.register(compressedColor, block);

    SimpleColoredBlocks.MOD_BLOCKS.stream().filter(SimpleGlassBlock.class::isInstance)
            .forEach(simpleBlock -> RenderTypeLookup.setRenderLayer(simpleBlock, RenderType.getTranslucent()));
  }

  @SubscribeEvent
  public static void registerItemColors(final FMLClientSetupEvent event) {

    final ItemColors itemColors = mc.getItemColors();
    final BlockColors blockColors = mc.getBlockColors();

    final IItemColor itemBlockColor = (stack, tintIndex) -> {
      final BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
      return blockColors.getColor(state, null, null,0);
    };
    for (final Block block : SimpleColoredBlocks.MOD_BLOCKS)
      itemColors.register(itemBlockColor, block);

    final IItemColor itemColor = SimpleColoredBlocksClient::getColor;
    itemColors.register(itemColor,SimpleColoredBlocks.color_wheel);
  }

  private static int getColorFromWheel(ItemStack stack){
    return (stack.getOrCreateTag().getInt("red") * (0x100/Configs.GRANULARITY) << 16)
            + (stack.getOrCreateTag().getInt("green") * (0x100/Configs.GRANULARITY) << 8)
            + stack.getOrCreateTag().getInt("blue") * (0x100/Configs.GRANULARITY);
  }

  private static int getColor(BlockState state){
    return (((SimpleBlock) state.getBlock()).r * (0x100 / Configs.GRANULARITY) << 16)
            + (((SimpleBlock) state.getBlock()).g * (0x100 / Configs.GRANULARITY) << 8)
            + ((SimpleBlock) state.getBlock()).b * (0x100 / Configs.GRANULARITY);
  }

  private static int getColor(ItemStack stack, int tintIndex) {
    return getColorFromWheel(stack);
  }
}
