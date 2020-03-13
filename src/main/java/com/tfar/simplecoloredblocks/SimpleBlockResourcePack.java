package com.tfar.simplecoloredblocks;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class SimpleBlockResourcePack implements IResourcePack {

  @Override
  public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {
    if (!resourceExists(type,location)) {
      return null;
    }
    if (type == ResourcePackType.CLIENT_RESOURCES) {


        File file = new File(new File(Minecraft.getInstance().gameDir, "scb_resources/" + location.getNamespace()), location.getPath());

        String realFileName = file.getCanonicalFile().getName();
        if (!realFileName.equals(file.getName()))
        {
        //  SimpleColoredBlocks.LOGGER.warn("[SimpleColoredBlocks] Resource Location " + location.toString() + " only matches the file " + realFileName + " because RL is running in an environment that isn't case sensitive in regards to file names. This will not work properly on for example Linux.");
        }

        return new FileInputStream(file);
          }
    throw new FileNotFoundException(location.toString());
  }

  @Override
  public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType p_225637_1_, String p_225637_2_, String p_225637_3_, int p_225637_4_, Predicate<String> p_225637_5_) {
    File folder = new File(Minecraft.getInstance().gameDir, "scb_resources/");
    if (!folder.exists()) {
      folder.mkdir();
    }
    HashSet<ResourceLocation> folders = new HashSet<>();

    //SimpleColoredBlocks.LOGGER.log(Level.DEBUG, "Resource Loader Domains: ");

    File[] resourceDomains = folder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

    for (File resourceFolder : resourceDomains)
    {
      //SimpleColoredBlocks.LOGGER.info( "[NormalResourceLoader]  - " + resourceFolder.getName() + " | " + resourceFolder.getAbsolutePath());
      folders.add(new ResourceLocation(SimpleColoredBlocks.MODID,resourceFolder.getName()));
    }

    return folders;
  }

  @Override
  public boolean resourceExists(ResourcePackType type, ResourceLocation location) {
      File fileRequested = new File(new File( "scb_resources/" + location.getNamespace()), location.getPath());

      if (!fileRequested.isFile())
      {
       // SimpleColoredBlocks.LOGGER.warn( "[SimpleColoredBlocks] Asked for resource " + location.toString() + " but can't find a file at " + fileRequested.getAbsolutePath());
      }
      return fileRequested.isFile();
    }

  @Override
  public Set<String> getResourceNamespaces(ResourcePackType type) {
    return type == ResourcePackType.CLIENT_RESOURCES ? ImmutableSet.of(SimpleColoredBlocks.MODID) : Collections.emptySet();
  }

  @SuppressWarnings({ "unchecked", "null" })
  @Override
  public <T> T getMetadata(IMetadataSectionSerializer<T> arg0) {
    return "pack".equals(arg0.getSectionName()) ? (T) new PackMetadataSection(new StringTextComponent("SimpleColoredBlocks resources"), 4) : null;
  }

  @Override
  public String getName() {
    return "SimpleColoredBlocks resource pack";
  }

 // @Override
  public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType arg0, String arg1, int arg2, Predicate<String> arg3) {
    File folder = new File(Minecraft.getInstance().gameDir, "scb_resources/");
    if (!folder.exists())
    {
      folder.mkdir();
    }
    HashSet<ResourceLocation> folders = new HashSet<>();

    //SimpleColoredBlocks.LOGGER.log(Level.DEBUG, "Resource Loader Domains: ");

    File[] resourceDomains = folder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

    for (File resourceFolder : resourceDomains)
    {
     // SimpleColoredBlocks.LOGGER.info( "[NormalResourceLoader]  - " + resourceFolder.getName() + " | " + resourceFolder.getAbsolutePath());
      folders.add(new ResourceLocation(SimpleColoredBlocks.MODID,resourceFolder.getName()));
    }

    return folders;
  }

  @Override
  public InputStream getRootResourceStream(String location) throws IOException {
    File file = new File(Minecraft.getInstance().gameDir, "scb_resources/");

    String realFileName = file.getCanonicalFile().getName();
    if (!realFileName.equals(file.getName()))
    {
   //   SimpleColoredBlocks.LOGGER.log(Level.WARN, "[SimpleColoredBlocks] Resource Location " + location.toString() + " only matches the file " + realFileName + " because RL is running in an environment that isn't case sensitive in regards to file names. This will not work properly on for example Linux.");
    }

    return new FileInputStream(file);
  }

  @Override
  public void close() {}
}