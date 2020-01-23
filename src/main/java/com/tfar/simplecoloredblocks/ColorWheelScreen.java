package com.tfar.simplecoloredblocks;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColorWheelScreen extends ContainerScreen<ColorWheelContainer> implements IContainerListener {

  private static final ResourceLocation wheelResource = new ResourceLocation(SimpleColoredBlocks.MODID, "textures/gui/color_wheel.png");

  public ColorWheelScreen(ColorWheelContainer p_i51103_1_, PlayerInventory p_i51103_2_, ITextComponent p_i51103_3_) {
    super(p_i51103_1_, p_i51103_2_, p_i51103_3_);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
      RenderSystem.disableLighting();
    RenderSystem.disableBlend();
      this.font.drawString(this.title.getFormattedText(), 40, 6, 0x404040);
      this.font.drawString(I18n.format("text.simplecoloredblocks.color_wheel_container.preview"),125,6,0x404040);
  }

  /**
   * Draws the background layer of this container (behind the items).
   *
   * @param partialTicks
   * @param mouseX
   * @param mouseY
   */
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    RenderSystem.color4f(1, 1, 1, 1);
    this.minecraft.getTextureManager().bindTexture(wheelResource);
    int i = (this.width - this.xSize) / 2;
    int j = (this.height - this.ySize) / 2;
    this.blit(i, j, 0, 0, this.xSize, this.ySize);

    float r = (float)this.container.getSlot(0).getStack().getCount() / Configs.GRANULARITY;
    float g = (float)this.container.getSlot(1).getStack().getCount()  / Configs.GRANULARITY;
    float b = (float)this.container.getSlot(2).getStack().getCount()  / Configs.GRANULARITY;

    RenderSystem.color3f(r,g,b);
    this.blit(i + 134, j + 20, 134, 20, 16, 16);

  }

  /**
   * update the crafting window inventory with the items in the list
   *
   * @param containerToSend
   * @param itemsList
   */
  @Override
  public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {

  }

  /**
   * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
   * contents of that slot.
   *
   * @param containerToSend
   * @param slotInd
   * @param stack
   */
  @Override
  public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
  }

  /**
   * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
   * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
   * value. Both are truncated to shorts in non-local SMP.
   *
   * @param containerIn
   * @param varToUpdate
   * @param newValue
   */
  @Override
  public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    RenderSystem.disableLighting();
    RenderSystem.disableBlend();
  }
}
