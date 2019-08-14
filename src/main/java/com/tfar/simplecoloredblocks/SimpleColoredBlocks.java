package com.tfar.simplecoloredblocks;

import com.tfar.simplecoloredblocks.block.SimpleBlock;
import com.tfar.simplecoloredblocks.block.SimpleGlassBlock;
import com.tfar.simplecoloredblocks.block.SimpleGlowingBlock;
import com.tfar.simplecoloredblocks.recipe.GlassRecipe;
import com.tfar.simplecoloredblocks.recipe.GlowingRecipe;
import com.tfar.simplecoloredblocks.recipe.SimpleRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.tfar.simplecoloredblocks.Configs.*;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(SimpleColoredBlocks.MODID)
public class SimpleColoredBlocks {
  // Directly reference a log4j logger.
  public static final String MODID = "simplecoloredblocks";
  public static final Logger LOGGER = LogManager.getLogger();

  public static final String WHEEL = "color_wheel_container";

  @ObjectHolder(MODID + ":" + WHEEL)
  public static final ContainerType<ColorWheelContainer> TYPE = null;

  @ObjectHolder(MODID + ":simple")
  public static final IRecipeSerializer<?> SIMPLE = null;

  @ObjectHolder(MODID + ":glass")
  public static final IRecipeSerializer<?> GLASS = null;

  @ObjectHolder(MODID + ":glowing")
  public static final IRecipeSerializer<?> GLOWING = null;

  @ObjectHolder(MODID + ":" + "color_wheel")
  public static Item color_wheel = null;

  public static final Set<SimpleBlock> MOD_BLOCKS = new LinkedHashSet<>();

  public static final ItemGroup tab = new ItemGroup(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(Blocks.CLAY);
    }
  };

  public static SimpleColoredBlocks instance;

  public SimpleColoredBlocks() {
    instance = this;
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

    // Register ourselves for server and other game events we are interested in
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(FMLServerStartingEvent event) {
    // do something when the server starts
    try {
      //    Scripts.jsonStuff();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

      handleConfig();

      IForgeRegistry<Block> registry = blockRegistryEvent.getRegistry();

      Block.Properties properties = Block.Properties.create(Material.ROCK, MaterialColor.DIRT).hardnessAndResistance(1.5F, 6.0F);

      Block.Properties glowing = Block.Properties.create(Material.ROCK, MaterialColor.DIRT).hardnessAndResistance(1.5F, 6.0F).lightValue(15);

      for (int r = 0; r < GRANULARITY; r++) {
        for (int g = 0; g < GRANULARITY; g++) {
          for (int b = 0; b < GRANULARITY; b++) {
            registerBlock(new SimpleBlock(properties, r, g, b), r + "r_" + g + "g_" + b + "b_", registry);
            registerBlock(new SimpleGlassBlock(properties, r, g, b), r + "r_" + g + "g_" + b + "b_glass", registry);
            registerBlock(new SimpleGlowingBlock(glowing,r,g,b),r + "r_" + g + "g_" + b + "b_glowing",registry);
          }
        }
      }
    }

    private static void registerBlock(Block block, String name, IForgeRegistry<Block> registry) {
      block.setRegistryName(name);
      MOD_BLOCKS.add((SimpleBlock) block);
      registry.register(block);
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {

      IForgeRegistry<Item> registry = itemRegistryEvent.getRegistry();

      Item.Properties properties1 = new Item.Properties().group(tab);

      registerItem(new ItemColorWheel(properties1), "color_wheel", registry);
      for (Block block : MOD_BLOCKS) {
        registerItem(new BlockItem(block, properties1) {

          @Nonnull
          @Override
          public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
            return new TranslationTextComponent(getTranslationKey(),
                    ((SimpleBlock) ((BlockItem) stack.getItem()).getBlock()).r,
                    ((SimpleBlock) ((BlockItem) stack.getItem()).getBlock()).g,
                    ((SimpleBlock) ((BlockItem) stack.getItem()).getBlock()).b
            );
          }
          },
                block.getRegistryName().toString(), registry);
      }
    }

    private static void registerItem(Item item, String name, IForgeRegistry<Item> registry) {
      item.setRegistryName(name);
      registry.register(item);
    }

    @SubscribeEvent
    public static void registerContainer(RegistryEvent.Register<ContainerType<?>> containerTypeRegister) {

      ContainerType<ColorWheelContainer> obj = new ContainerType<>(ColorWheelContainer::new);
      obj.setRegistryName(WHEEL);

      containerTypeRegister.getRegistry().register(obj);
    }

    @SubscribeEvent
    public static void registerSerials(RegistryEvent.Register<IRecipeSerializer<?>> event) {

      IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
      SpecialRecipeSerializer<SimpleRecipe> simple = new SpecialRecipeSerializer<>(SimpleRecipe::new);
      registry.register(simple.setRegistryName("simple"));
      SpecialRecipeSerializer<GlassRecipe> glass = new SpecialRecipeSerializer<>(GlassRecipe::new);
      registry.register(glass.setRegistryName("glass"));
      SpecialRecipeSerializer<GlowingRecipe> glowing = new SpecialRecipeSerializer<>(GlowingRecipe::new);
      registry.register(glowing.setRegistryName("glowing"));
    }
  }
}
