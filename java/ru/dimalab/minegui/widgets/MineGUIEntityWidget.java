package ru.dimalab.minegui.widgets;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import ru.dimalab.minegui.utils.MineGUIColorPalette;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

public class MineGUIEntityWidget implements MineGUIWidget {

    private int x, y, width, height;
    private int bgX, bgY, bgWidth, bgHeight;
    private LivingEntity entity;
    private float scale = 1.0f;
    private boolean showShadow = false;
    private boolean rotateOnMouse = true;
    private float offsetX = 0;
    private float offsetY = 0;

    private static final Vector3f CUSTOM_LIGHT_0 = new Vector3f(-0.3f, 1f, 1f).normalize();
    private static final Vector3f CUSTOM_LIGHT_1 = new Vector3f(0.3f, -1f, -1f).normalize();

    ResourceLocation resLocation;

    private MineGUIColorPalette borderColor;

    private MineGUIEntityWidget() {}

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (entity == null) return;

        renderEntity(
                graphics,
                resLocation,
                borderColor,
                entity,
                x,
                y,
                bgX,
                bgY,
                bgWidth,
                bgHeight,
                width,
                height,
                scale,
                mouseX,
                mouseY,
                offsetX,
                offsetY,
                rotateOnMouse
        );
    }

    public static void renderEntity(
            GuiGraphics graphics,
            ResourceLocation backgroundTexture,
            MineGUIColorPalette borderColor,
            LivingEntity entity,
            float x, float y,
            int bgX, int bgY,
            int bgWidth, int bgHeight,
            float width, float height,
            float scale,
            float mouseX, float mouseY,
            float offsetX, float offsetY,
            boolean rotation
    ) {
        if (entity == null) return;

        graphics.blit(backgroundTexture, bgX, bgY, 0, 0, bgWidth, bgHeight, bgWidth, bgHeight);

        graphics.renderOutline((int) x, (int) y, (int) width, (int) height, borderColor.getColor());


        float rotationFactor = rotation ? 1.0f : 0.0f;

        PoseStack stack = new PoseStack();

        float xOffset = x + width / 2.0f + offsetX;
        float yOffset = y + height + offsetY;

        stack.translate(xOffset, yOffset, 0.0f);

        float newScale = Math.min(width / entity.getBbWidth(), height / entity.getBbHeight()) * 0.95f * scale;

        stack.mulPose(new Matrix4f().scaling(newScale, -newScale, newScale));

        float rotationX = (float) Math.atan((xOffset - mouseX) / 150.0f / 3) * rotationFactor;
        float rotationY = (float) Math.atan((yOffset - height / 2.0f - mouseY) / 150.0f / 3) * rotationFactor;

        RenderSystem.setShaderLights(CUSTOM_LIGHT_0, CUSTOM_LIGHT_1);

        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

        stack.mulPose(new Quaternionf().rotateX(rotationY * -20.0f * Mth.DEG_TO_RAD));

        float yBodyRotOld = entity.yBodyRot;
        float yRotOld = entity.getYRot();
        float xRotOld = entity.getXRot();
        float yHeadRotOOld = entity.yHeadRotO;
        float yHeadRotOld = entity.yHeadRot;

        entity.yBodyRot = rotationX * 20.0f;
        entity.setYRot(rotationX * 40.0f);
        entity.setXRot(-rotationY * 20.0f);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();

        boolean oldCustomNameVisible = entity.isCustomNameVisible();
        entity.setCustomNameVisible(false);

        dispatcher.setRenderShadow(false);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        RenderSystem.runAsFancy(() -> {
            dispatcher.render(
                    entity,
                    0.0,
                    0.0,
                    0.0,
                    0.0f,
                    1.0f,
                    stack,
                    bufferSource,
                    15728880
            );
        });

        RenderSystem.disableDepthTest();
        bufferSource.endBatch();
        RenderSystem.enableDepthTest();

        dispatcher.setRenderShadow(true);
        entity.setCustomNameVisible(oldCustomNameVisible);

        entity.yBodyRot = yBodyRotOld;
        entity.setYRot(yRotOld);
        entity.setXRot(xRotOld);
        entity.yHeadRot = yHeadRotOld;
        entity.yHeadRotO = yHeadRotOOld;

        Lighting.setupFor3DItems();
    }

    public static class Builder {

        private final MineGUIEntityWidget widget = new MineGUIEntityWidget();

        public Builder position(int x, int y) {
            widget.x = x;
            widget.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            widget.width = width;
            widget.height = height;
            return this;
        }

        public Builder scale(float scale) {
            widget.scale = scale;
            return this;
        }

        public Builder shadow(boolean shadow) {
            widget.showShadow = shadow;
            return this;
        }

        public Builder rotateOnMouse(boolean rotate) {
            widget.rotateOnMouse = rotate;
            return this;
        }

        public Builder offset(float offsetX, float offsetY) {
            widget.offsetX = offsetX;
            widget.offsetY = offsetY;
            return this;
        }

        public Builder entity(LivingEntity entity) {
            widget.entity = entity;
            return this;
        }

        public Builder background(ResourceLocation background) {
            widget.resLocation = background;
            return this;
        }

        public Builder backgroundSize(int bgWidth, int bgHeight) {
            widget.bgWidth = bgWidth;
            widget.bgHeight = bgHeight;
            return this;
        }

        public Builder backgroundPosition(int bgX, int bgY) {
            widget.bgX = bgX;
            widget.bgY = bgY;
            return this;
        }

        public Builder borderColor(MineGUIColorPalette color) {
            widget.borderColor = color;
            return this;
        }

        public MineGUIEntityWidget build() {
            if (widget.entity == null) {
                throw new IllegalStateException("Entity is not set for MineGUIEntityWidget!");
            }
            if (widget.resLocation == null) {
                throw new IllegalStateException("Background texture is not set for MineGUIEntityWidget!");
            }
            if (widget.borderColor == null) {
                widget.borderColor = MineGUIColorPalette.TRANSPARENT; // Дефолтный цвет рамки
            }
            return widget;
        }
    }
}
