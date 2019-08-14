package com.tfar.simplecoloredblocks;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

public class Configs {
  public static int GRANULARITY;

    public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final File configFile = new File("config/"+SimpleColoredBlocks.MODID+".json");
    private static BufferedInputStream in = new BufferedInputStream(Configs.class.getResourceAsStream("/config.json"));
    public static int loaded;

  private static JsonObject colors;


  static {
      String s;
      try {
        s = IOUtils.toString(in, Charset.defaultCharset());
      } catch (IOException e) {
        throw new RuntimeException("The default config is broken, report to mod author asap!", e);
      }
      colors = g.fromJson(s, JsonObject.class);
    }

    public static void handleConfig() {

      writeConfig();
      readConfig();

    }

    private static void writeConfig() {

      if (configFile.exists())return;
      try {
        FileWriter writer = new FileWriter(configFile);
        writer.write(g.toJson(colors));
        writer.flush();
      } catch (IOException ugh) {
        //I expect this from a user, but you?!
        throw new RuntimeException("The default config is broken, report to mod author asap!", ugh);
      }
    }

    private static void readConfig() {
      try {
        FileReader reader = new FileReader(configFile);
        JsonObject cfg = new JsonParser().parse(reader).getAsJsonObject();
        GRANULARITY = cfg.get("colors").getAsInt();
        loaded = cfg.get("loaded").getAsInt();
      } catch (Exception e) {
        SimpleColoredBlocks.LOGGER.fatal(e);
      }
    }
  }


