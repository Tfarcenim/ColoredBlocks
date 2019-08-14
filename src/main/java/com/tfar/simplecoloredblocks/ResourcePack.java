package com.tfar.simplecoloredblocks;

import com.google.gson.JsonObject;
import com.tfar.simplecoloredblocks.block.SimpleBlock;
import com.tfar.simplecoloredblocks.block.SimpleGlassBlock;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.tfar.simplecoloredblocks.SimpleColoredBlocks.MODID;


public class ResourcePack {
  public static void makeResourcePack() {
    String dir = "scb_resources/";
    Path path = Paths.get(dir);
    try {
      Files.createDirectories(path);
      Path assets = Paths.get(dir);
      Files.createDirectories(assets);
      File mcmeta = new File("scb_resources/pack.mcmeta");
      if (!mcmeta.exists()) {
        String str = "{\n" +
                "    \"pack\": {\n" +
                "        \"description\": \"Assets for Simple Colored Blocks, DO NOT REMOVE!\",\n" +
                "        \"pack_format\": 4,\n" +
                "        \"_comment\": \"A pack_format of 4 requires json lang files. Note: we require v4 pack meta for all mods.\"\n" +
                "    }\n" +
                "}";
        FileWriter writer = new FileWriter(mcmeta);
        writer.write(str);
        writer.flush();
      }
      dir += "/"+MODID;
      Path main = Paths.get(dir);
      Files.createDirectories(main);
      String blockstates = dir + "/blockstates";
      String items = dir + "/models/item";
      Path main1 = Paths.get(blockstates);
      Files.createDirectories(main1);
      Path main3 = Paths.get(items);
      Files.createDirectories(main3);

      for (SimpleBlock block : SimpleColoredBlocks.MOD_BLOCKS) {
        String path2 = block.getRegistryName().getPath();
        String path3 = ":block/cube_all_tinted";
        path3 += block instanceof SimpleGlassBlock ? "_glass":"";
        File blockstatefile = new File(blockstates + "/" + path2 + ".json");
        if (!blockstatefile.exists()) {
          String model = MODID + path3;
          JsonObject obj = new JsonObject();
          obj.addProperty("model", model);
          JsonObject variants = new JsonObject();
          variants.add("", obj);

          JsonObject blockstate = new JsonObject();
          blockstate.add("variants", variants);

          FileWriter writer = new FileWriter(blockstatefile);
          writer.write(Configs.g.toJson(blockstate));
          writer.flush();
        }

        File itemfile = new File(items + "/" + path2 + ".json");
        if (!itemfile.exists()) {
          JsonObject itemmodel = new JsonObject();
          itemmodel.addProperty("parent", MODID + path3);

          FileWriter writer = new FileWriter(itemfile);
          writer.write(Configs.g.toJson(itemmodel));
          writer.flush();
        }
      }
    } catch (IOException e) {
      //fail to create directory
      //throw new RuntimeException("Unable to create Resource Pack!",e);
    }
  }
}
