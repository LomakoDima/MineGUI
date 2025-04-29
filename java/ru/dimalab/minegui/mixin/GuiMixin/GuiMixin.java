package ru.dimalab.minegui.mixin.GuiMixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.dimalab.minegui.loloshka.DialogOverlay;


@Mixin(Gui.class)
public abstract class GuiMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(GuiGraphics p_282884_, DeltaTracker p_342095_, CallbackInfo ci) {
        DialogOverlay.renderOverlay(p_282884_);
    }
}
