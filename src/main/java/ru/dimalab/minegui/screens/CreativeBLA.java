package ru.dimalab.minegui.screens;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import ru.dimalab.minegui.MineGUI;


public class CreativeBLA {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MineGUI.MODID);

    public static final RegistryObject<CreativeModeTab> TEST_OPEN_ITEMS_TAB = CREATIVE_MODE_TABS.register("test_open_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TEST_OPEN.get()))
                    .title(Component.translatable("creativetab.minegui.test_open_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.TEST_OPEN.get());

                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
