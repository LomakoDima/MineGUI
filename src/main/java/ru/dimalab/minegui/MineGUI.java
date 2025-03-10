package ru.dimalab.minegui;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import ru.dimalab.minegui.screens.CreativeBLA;
import ru.dimalab.minegui.screens.ModItems;

@Mod(MineGUI.MODID)
public class MineGUI {
    public static final String MODID = "minegui";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MineGUI() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        CreativeBLA.register(modEventBus);
        ModItems.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
