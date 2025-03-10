package ru.dimalab.minegui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import ru.dimalab.minegui.manager.ScreenManager;

public class TestOpen extends Item {

    public TestOpen(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().isClientSide()) {
            Minecraft.getInstance().execute(() -> {
                ScreenManager.openScreen(new ExampleScreen(Minecraft.getInstance(), "Example"));
                ScreenManager.printStack();
            });
        }
        return InteractionResult.SUCCESS;
    }

}
