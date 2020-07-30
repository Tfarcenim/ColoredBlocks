package com.tfar.simplecoloredblocks;

import com.tfar.simplecoloredblocks.block.SimpleBlock;
import com.tfar.simplecoloredblocks.block.SimpleGlassBlock;
import com.tfar.simplecoloredblocks.block.SimpleGlowingBlock;
import com.tfar.simplecoloredblocks.block.SimpleGlowingGlassBlock;
import com.tfar.simplecoloredblocks.recipe.GlassRecipe;
import com.tfar.simplecoloredblocks.recipe.GlowingGlassRecipe;
import com.tfar.simplecoloredblocks.recipe.GlowingRecipe;
import com.tfar.simplecoloredblocks.recipe.SimpleRecipe;
import net.devtech.arrp.ARRP;
import net.devtech.arrp.api.RRPEvent;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.loot.JLootTable;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.tfar.simplecoloredblocks.Configs.GRANULARITY;
import static com.tfar.simplecoloredblocks.Configs.handleConfig;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;


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

  @ObjectHolder(MODID + ":glowing_glass")
  public static final IRecipeSerializer<?> GLOWING_GLASS = null;

  @ObjectHolder(MODID + ":" + "color_wheel")
  public static Item color_wheel = null;

  public static final Set<SimpleBlock> MOD_BLOCKS = new LinkedHashSet<>();
  public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(MODID+":builtin");

  public SimpleColoredBlocks() {
    EVENT_BUS.addListener(this::registerPack);
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    bus.addListener(this::common);
  }

  private void registerPack(RRPEvent e) {
    e.packs.add(RESOURCE_PACK);
  }

  private void common(FMLCommonSetupEvent e) {
    for (Block block : MOD_BLOCKS) {
      RESOURCE_PACK.addLootTable(blockLootTable(block), JLootTable.loot("minecraft:block")
              .pool(JLootTable.pool()
                      .rolls(1)
                      .entry(JLootTable.entry()
                              .type("minecraft:item")
                              .name("minecraft:diamond"))
                      .condition(JLootTable.condition("minecraft:survives_explosion"))));
    }
  }

  public static ResourceLocation blockLootTable(Block block) {
    ResourceLocation identifier = block.getRegistryName();
    return new ResourceLocation(identifier.getNamespace(),"blocks/"+ identifier.getPath());
  }

  public static final ItemGroup tab = new ItemGroup(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(Blocks.CLAY);
    }
  };

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

  handleConfig();
  IForgeRegistry<Block> registry = blockRegistryEvent.getRegistry();

  Block.Properties properties = Block.Properties.create(Material.ROCK, MaterialColor.DIRT).hardnessAndResistance(1.5F, 6.0F);

  Block.Properties glowing = Block.Properties.create(Material.ROCK, MaterialColor.DIRT).hardnessAndResistance(1.5F, 6.0F).setLightLevel(state -> 15);

      for (int r = 0; r < GRANULARITY; r++) {
        for (int g = 0; g < GRANULARITY; g++) {
          for (int b = 0; b < GRANULARITY; b++) {
            registerBlock(new SimpleBlock(properties, r, g, b), r + "r_" + g + "g_" + b + "b_", registry);
            registerBlock(new SimpleGlassBlock(properties.notSolid(), r, g, b), r + "r_" + g + "g_" + b + "b_glass", registry);
            registerBlock(new SimpleGlowingBlock(glowing, r, g, b), r + "r_" + g + "g_" + b + "b_glowing", registry);
            registerBlock(new SimpleGlowingGlassBlock(glowing.notSolid(), r, g, b), r + "r_" + g + "g_" + b + "b_glowing_glass", registry);
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
      SpecialRecipeSerializer<GlowingGlassRecipe> glowing_glass = new SpecialRecipeSerializer<>(GlowingGlassRecipe::new);
      registry.register(glowing_glass.setRegistryName("glowing_glass"));
    }
  }
}
