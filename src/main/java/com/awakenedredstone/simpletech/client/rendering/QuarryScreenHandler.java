package com.awakenedredstone.simpletech.client.rendering;

import com.awakenedredstone.simpletech.SimpleTech;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class QuarryScreenHandler extends ScreenHandler {

    public QuarryScreenHandler(int syncId, PlayerInventory inventory) {
        super(SimpleTech.QUARRY_SCREEN_HANDLER, syncId);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
