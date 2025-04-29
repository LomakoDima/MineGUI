package ru.dimalab.minegui.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.dimalab.minegui.MineGUI;
import ru.dimalab.minegui.manager.ScreenManager;
import ru.dimalab.minegui.screens.ExampleScreen;
import ru.dimalab.minegui.screens.MineGUIScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = MineGUI.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MineGUICommand {
    private static final Map<String, Class<? extends MineGUIScreen>> SCREENS = new HashMap<>();

    static {
        SCREENS.put("example_screen", ExampleScreen.class);
        //SCREENS.put("quest_screen", QuestScreen.class);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("mine_gui")
                        .then(Commands.literal("open")
                                .then(Commands.argument("screen", StringArgumentType.string())
                                        .suggests(SCREEN_SUGGESTIONS)
                                        .executes(MineGUICommand::openScreen)
                                )
                        )
        );
    }

    private static int openScreen(CommandContext<CommandSourceStack> context) {
        String screenName = StringArgumentType.getString(context, "screen");

        if (SCREENS.containsKey(screenName)) {
            Minecraft.getInstance().execute(() -> {
                try {
                    ScreenManager.openScreen(SCREENS.get(screenName)
                            .getConstructor(Minecraft.class, String.class)
                            .newInstance(Minecraft.getInstance(), screenName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return 1;
        } else {
            context.getSource().sendFailure(net.minecraft.network.chat.Component.literal("Экран не найден!"));
            return 0;
        }
    }

    private static final SuggestionProvider<CommandSourceStack> SCREEN_SUGGESTIONS = (context, builder) -> {
        for (String name : SCREENS.keySet()) {
            builder.suggest(name);
        }
        return CompletableFuture.supplyAsync(builder::build);
    };
}
