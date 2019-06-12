package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Scripts {

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

  private static final String[] recipe = {
          "###",
          "###",
          "###"
  };

  public static void jsonStuff() throws IOException {

    for (Block block : SimpleColoredBlocks.MOD_BLOCKS) {
    //  blockstates(block.getRegistryName().getPath());
    //  block(block.getRegistryName().getPath());
    //  item(block.getRegistryName().getPath());
     // loot_tables(block.getRegistryName().getPath());
    }
    lang();

    //  "text.simplecoloredblocks.color_wheel_container.preview" : "Preview",
    //  "text.simplecoloredblocks.color_wheel_container.title": "Color Wheel"
  }

  private static void loot_tables(String path) throws IOException {
    //if (blockstatefile.exists()) return;
    JsonObject loot_table = new JsonObject();
    loot_table.addProperty("type","minecraft:block");
    String block = "simplecoloredblocks:" + path;
    JsonObject pool = new JsonObject();
    pool.addProperty("name","simple");
    pool.addProperty("rolls",1);
    JsonObject entry = new JsonObject();
    entry.addProperty(          "type", "minecraft:item");
    entry.addProperty("name", block);
    JsonArray entries = new JsonArray();
    entries.add(entry);
    pool.add("entries",entries);
    JsonObject condition = new JsonObject();
    condition.addProperty("condition","minecraft:survives_explosion");
    JsonArray conditions = new JsonArray();
    conditions.add(condition);
    pool.add("conditions",conditions);
    JsonArray pools = new JsonArray();
    pools.add(pool);
    loot_table.add("pools", pools);
    File loot_tablefile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\ColoredBlocks\\src\\main\\resources\\data\\simplecoloredblocks\\loot_tables\\blocks\\" + path + ".json");
    FileWriter writer = new FileWriter(loot_tablefile);
    writer.write(g.toJson(loot_table));
    writer.flush();
  }

  private static void blockstates(String registryname) throws IOException {

    //if (blockstatefile.exists()) return;
    JsonObject variants = new JsonObject();
    String model = "simplecoloredblocks:block/" + registryname;
    JsonObject obj = new JsonObject();
    obj.addProperty("model", model);
    variants.add("", obj);

    JsonObject blockstates = new JsonObject();
    blockstates.add("variants", variants);

    File blockstatefile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\ColoredBlocks\\src\\main\\resources\\assets\\simplecoloredblocks\\blockstates\\" + registryname + ".json");
    FileWriter writer = new FileWriter(blockstatefile);
    writer.write(g.toJson(blockstates));
    writer.flush();
  }

  private static void block(String material) throws IOException {
    // if (blockfile.exists()) return;
    JsonObject all = new JsonObject();
    all.addProperty("all", "block/" + material);

    JsonObject block = new JsonObject();
    block.addProperty("parent", "simplecoloredblocks:block/cube_all_tinted");
    block.add("textures", all);

    File blockfile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\ColoredBlocks\\src\\main\\resources\\assets\\simplecoloredblocks\\models\\block\\" + material + ".json");
    FileWriter writer = new FileWriter(blockfile);
    writer.write(g.toJson(block));
    writer.flush();
  }

  private static void item(String material) throws IOException {
    File itemfile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\ColoredBlocks\\src\\main\\resources\\assets\\simplecoloredblocks\\models\\item\\" + material +  ".json");
    // if (blockfile.exists()) return;

    JsonObject item = new JsonObject();
    item.addProperty("parent", "simplecoloredblocks:block/" + material);

    FileWriter writer = new FileWriter(itemfile);
    writer.write(g.toJson(item));
    writer.flush();
  }

  private static void lang() throws IOException {
    JsonObject lang = new JsonObject();
    for (Block block : SimpleColoredBlocks.MOD_BLOCKS){
      lang.addProperty("block.simplecoloredblocks."+block.getRegistryName().getPath(),"Red "+((SimpleBlock)block).r + ", Green "+((SimpleBlock)block).g + ", Blue "+((SimpleBlock)block).b+" Block");
    }
    File langfile = new File("C:\\Users\\xluser\\Documents\\MinecraftMods\\mods\\ColoredBlocks\\src\\main\\resources\\assets\\simplecoloredblocks\\lang\\en_us.json");
    FileWriter writer = new FileWriter(langfile);
    writer.write(g.toJson(lang));
    writer.flush();
  }
}
