package com.example.examplemod;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenColorWheelInventory {
  public OpenColorWheelInventory()
  {
  }

  public OpenColorWheelInventory(PacketBuffer buf)
  {
  }

  public void encode(PacketBuffer buf)
  {
  }

  public void handle(Supplier<NetworkEvent.Context> context)
  {
    context.get().enqueueWork(() -> {
      Screens.openSlotScreen(context.get().getSender());
    });
  }
}
