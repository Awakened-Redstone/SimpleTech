package com.awakenedredstone.simpletech.client.rendering;

import io.wispforest.owo.ui.base.BaseUIModelHandledScreen;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class QuarryScreen extends BaseUIModelHandledScreen<FlowLayout, QuarryScreenHandler> {

    public QuarryScreen(QuarryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, FlowLayout.class, BaseUIModelScreen.DataSource.file("quarry.xml"));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.childById(ButtonWidget.class, "the-button").onPress(button -> {
            System.out.println("click");
        });
    }
}
