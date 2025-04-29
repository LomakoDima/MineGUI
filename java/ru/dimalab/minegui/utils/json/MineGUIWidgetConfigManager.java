package ru.dimalab.minegui.utils.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.loading.FMLPaths;
import ru.dimalab.minegui.MineGUI;
import ru.dimalab.minegui.widgets.MineGUIText;
import ru.dimalab.minegui.widgets.custom.MineGUIWidget;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MineGUIWidgetConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_DIR = FMLPaths.CONFIGDIR.get().toString();
    //private static final String MODID = "minegui";

    public static void saveConfig(ScreenConfig config, String fileName) {
        Path path = Path.of(CONFIG_DIR, MineGUI.MODID, fileName + ".json");
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ScreenConfig loadConfig(String fileName) {
        Path path = Path.of(CONFIG_DIR, MineGUI.MODID, fileName + ".json");
        try {
            if (!Files.exists(path)) return new ScreenConfig();
            return GSON.fromJson(Files.readString(path), ScreenConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ScreenConfig();
        }
    }

}
