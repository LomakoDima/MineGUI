package ru.dimalab.minegui.register;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import ru.dimalab.minegui.MineGUI;
import ru.dimalab.minegui.api.MineGUIAPI;

import java.util.List;
import java.util.stream.Collectors;

public class MineGUIAnnotationProcessor {
    public static void scanForMineGUIAPI() {
        List<ModInfo> mods = FMLLoader.getLoadingModList().getMods();
        boolean hasIntegrations = false;

        for (ModInfo mod : mods) {
            if (mod.getModId().equals(MineGUI.MODID)) {
                continue;
            }

            ModFileScanData scanData = mod.getOwningFile().getFile().getScanResult();
            if (scanData == null) {
                MineGUI.LOGGER.warn("No scan data available for mod: {}", mod.getModId());
                continue;
            }

            List<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations().stream()
                    .filter(annotationData -> annotationData.annotationType().getClassName().equals(MineGUIAPI.class.getName()))
                    .collect(Collectors.toList());

            for (ModFileScanData.AnnotationData annotation : annotations) {
                String className = annotation.clazz().getClassName();
                try {
                    Class<?> annotatedClass = Class.forName(className);
                    MineGUIAPI apiAnnotation = annotatedClass.getAnnotation(MineGUIAPI.class);

                    String modId = apiAnnotation.modId().isEmpty() ? mod.getModId() : apiAnnotation.modId();
                    Class<?> mainClass = apiAnnotation.mainClass() == Void.class ? annotatedClass : apiAnnotation.mainClass();

                    MineGUI.LOGGER.info("Found @MineGUIAPI in mod '{}', class: '{}'", modId, mainClass.getName());

                    if (!mainClass.equals(annotatedClass)) {
                        MineGUI.LOGGER.warn("Mod '{}' specifies mainClass '{}', but annotation is on '{}'. This might be a misconfiguration.",
                                modId, mainClass.getName(), annotatedClass.getName());
                    }

                    initializeModIntegration(modId, mainClass);
                    hasIntegrations = true;

                } catch (ClassNotFoundException e) {
                    MineGUI.LOGGER.error("Failed to load class: {}", className, e);
                }
            }
        }

        if (!hasIntegrations) {
            MineGUI.LOGGER.info("No mods found using @MineGUIAPI. MineGUI will run in standalone mode.");
        }
    }

    private static void initializeModIntegration(String modId, Class<?> mainClass) {
        MineGUI.LOGGER.info("Integrating MineGUI with mod '{}', main class: '{}'", modId, mainClass.getSimpleName());
    }
}
