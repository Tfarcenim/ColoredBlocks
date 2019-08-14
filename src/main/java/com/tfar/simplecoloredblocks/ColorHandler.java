package com.tfar.simplecoloredblocks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tfar.simplecoloredblocks.block.SimpleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.ClientResourcePackInfo;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.PackCompatibility;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
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
public class ColorHandler {

  private static SimpleBlockResourcePack resourcePack = new SimpleBlockResourcePack();

  @SubscribeEvent
  public static void setupResourcePack(FMLClientSetupEvent event) {
    //  if (config_loaded_blocks != -1)return;
    handle();
    ScreenManager.registerFactory(SimpleColoredBlocks.TYPE, ColorWheelScreen::new);

    ResourcePack.makeResourcePack();

    ResourcePackList<ClientResourcePackInfo> rps = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getInstance(), "field_110448_aq");
    rps.addPackFinder(new IPackFinder() {

      @Override
      public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> nameToPackMap, ResourcePackInfo.IFactory<T> packInfoFactory) {
        NativeImage img = null;
        try {
          img = NativeImage.read(resourcePack.getRootResourceStream("pack.png"));
        } catch (IOException e) {
          LogManager.getLogger().error("Could not load simplecxoloredblocks's pack.png", e);
        }
        @SuppressWarnings("unchecked")
        T var3 = (T) new ClientResourcePackInfo(SimpleColoredBlocks.MODID, true, () -> resourcePack, new StringTextComponent(resourcePack.getName()), new StringTextComponent("Assets for Compressed"),
                PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.BOTTOM, true, img,true);
        nameToPackMap.put(SimpleColoredBlocks.MODID, var3);
      }
    });

    Minecraft.getInstance().getResourceManager().addResourcePack(resourcePack);

    //ForgeHooksClient.refreshResources(Minecraft.getInstance(), VanillaResourceType.MODELS);
  }

  private static void handle() {

    try {
      FileReader reader = new FileReader(configFile);
      JsonElement element = new JsonParser().parse(reader);
      FileWriter writer = new FileWriter(configFile);
      JsonObject config = element.getAsJsonObject();
      config.remove("loaded_blocks");
      config.addProperty("loaded_blocks", SimpleColoredBlocks.MOD_BLOCKS.size());
      writer.write(Configs.g.toJson(config,JsonObject.class));
      writer.flush();
    } catch (IOException ugh) {
      throw new RuntimeException("Impossible, the file existed moments ago", ugh);
    }
  }


  @SubscribeEvent
  public static void registerBlockColors(ColorHandlerEvent.Block event) {
    BlockColors colors = event.getBlockColors();
    final IBlockColor compressedColor = (state, blockAccess, pos, tintIndex) -> getColor(state);
    for (Block block : SimpleColoredBlocks.MOD_BLOCKS)
      colors.register(compressedColor, block);
  }
  @SubscribeEvent
  public static void registerItemColors(final ColorHandlerEvent.Item event) {
    final ItemColors itemColors = event.getItemColors();
    final BlockColors blockColors = event.getBlockColors();


    final IItemColor itemBlockColor = (stack, tintIndex) -> {
      final BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
      return blockColors.getColor(state, null, null,0);
    };
    for (Block block : SimpleColoredBlocks.MOD_BLOCKS)
      itemColors.register(itemBlockColor, block);

    final IItemColor itemColor = ColorHandler::getColor;
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
