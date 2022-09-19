package com.awakenedredstone.simpletech.client;

import com.awakenedredstone.simpletech.SimpleTech;
import com.awakenedredstone.simpletech.client.rendering.QuarryScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SimpleTechClient implements ClientModInitializer {
    public static final Map<Long, Pair<Box, Settings>> renderOutline = new HashMap<>();

    @Override
    public void onInitializeClient() {
        HandledScreens.register(SimpleTech.QUARRY_SCREEN_HANDLER, QuarryScreen::new);

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            renderSquare(context.matrixStack(), context.camera(), context.tickDelta());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused()) {
                List<Long> toRemove = new ArrayList<>();
                renderOutline.forEach((id, pair) -> {
                    if (pair.getRight().time-- == 0) {
                        toRemove.add(id);
                    }
                });
                toRemove.forEach(renderOutline::remove);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(new Identifier(SimpleTech.MOD_ID, "render"), (client, handler, buf, responseSender) -> {
            NbtCompound data = buf.readNbt();
            client.execute(() -> {
                long id = data.getLong("id");
                int time = data.getInt("time");
                int color = data.getInt("color");
                boolean disableDepthTest = data.getBoolean("disableDepthTest");
                NbtList from = data.getList("from", NbtElement.DOUBLE_TYPE);
                NbtList to = data.getList("to", NbtElement.DOUBLE_TYPE);
                double x1 = from.getDouble(0);
                double y1 = from.getDouble(1);
                double z1 = from.getDouble(2);
                double x2 = to.getDouble(0);
                double z2 = to.getDouble(2);
                double y2 = to.getDouble(1);
                renderOutline.put(id, Pair.of(new Box(x1, y1, z1, x2, y2, z2), new Settings(time).deathTest(disableDepthTest).color(color)));
            });
        });
    }

    private void renderSquare(MatrixStack matrices, Camera camera, float partialTick) {
        renderOutline.forEach((id, pair) -> {
            Settings settings = pair.getRight();
            matrices.push();
            RenderSystem.disableTexture();
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.depthFunc(515);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);
            if (settings.disableDepthTest) RenderSystem.disableDepthTest();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();

            RenderSystem.applyModelViewMatrix();


            Box box = pair.getLeft();

            double cameraX = camera.getPos().x;
            double cameraY = camera.getPos().y;
            double cameraZ = camera.getPos().z;

            RenderSystem.lineWidth(0.1F);
            drawBoxFaces(tessellator, bufferBuilder,
                    box.minX - cameraX, box.minY - cameraY, box.minZ - cameraZ,
                    box.maxX - cameraX, box.maxY - cameraY, box.maxZ - cameraZ,
                    true, true, true, settings.argb);

            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableTexture();
            matrices.pop();
        });
    }

    public static void drawBoxFaces(Tessellator tessellator, BufferBuilder builder, double x1, double y1, double z1, double x2, double y2, double z2,
                                    boolean xThick, boolean yThick, boolean zThick, int argb) {
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        if (xThick && yThick) {
            builder.vertex(x1, y1, z1).color(argb).next();
            builder.vertex(x2, y1, z1).color(argb).next();
            builder.vertex(x2, y2, z1).color(argb).next();
            builder.vertex(x1, y2, z1).color(argb).next();
            if (zThick) {
                builder.vertex(x1, y1, z2).color(argb).next();
                builder.vertex(x1, y2, z2).color(argb).next();
                builder.vertex(x2, y2, z2).color(argb).next();
                builder.vertex(x2, y1, z2).color(argb).next();
            }
        }


        if (zThick && yThick) {
            builder.vertex(x1, y1, z1).color(argb).next();
            builder.vertex(x1, y2, z1).color(argb).next();
            builder.vertex(x1, y2, z2).color(argb).next();
            builder.vertex(x1, y1, z2).color(argb).next();

            if (xThick) {
                builder.vertex(x2, y1, z1).color(argb).next();
                builder.vertex(x2, y1, z2).color(argb).next();
                builder.vertex(x2, y2, z2).color(argb).next();
                builder.vertex(x2, y2, z1).color(argb).next();
            }
        }

        // now at least drawing one
        if (zThick && xThick) {
            builder.vertex(x1, y1, z1).color(argb).next();
            builder.vertex(x2, y1, z1).color(argb).next();
            builder.vertex(x2, y1, z2).color(argb).next();
            builder.vertex(x1, y1, z2).color(argb).next();


            if (yThick) {
                builder.vertex(x1, y2, z1).color(argb).next();
                builder.vertex(x2, y2, z1).color(argb).next();
                builder.vertex(x2, y2, z2).color(argb).next();
                builder.vertex(x1, y2, z2).color(argb).next();
            }
        }
        tessellator.draw();
    }

    public static class Settings {
        public int time;
        public boolean disableDepthTest = false;
        public int argb = 0x80FF0000;

        public Settings(int time) {
            this.time = time;
        }

        public Settings disableDeathTest() {
            this.disableDepthTest = true;
            return this;
        }

        public Settings deathTest(boolean disable) {
            this.disableDepthTest = disable;
            return this;
        }

        public Settings color(int argb) {
            this.argb = argb;
            return this;
        }
    }
}
