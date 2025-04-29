package ru.dimalab.minegui.overlays.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.dimalab.minegui.MineGUI;
import ru.dimalab.minegui.loloshka.Dialog;
import ru.dimalab.minegui.loloshka.DialogManager;
import ru.dimalab.minegui.utils.MineGUIColorPalette;

@Mod.EventBusSubscriber(modid = MineGUI.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof Player player) {
            /*Dialog dialog1 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Смотри... Ты... Я... И другие человеческие существа... Все мы проживаем здесь свою жизнь... Мы рождаемся и умираем..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog2 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Этот мир возник из ниоткуда, и со временем он туда и вернется... Так что все ваши действия по сути своей бессмысленны... Во всем этом нет никакого смысла..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog3 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Представь... Ты умрешь... Тебя забудут... Как и всех остальных... Вся твоя борьба, история твоей жизни, все твои усилия в конце концов ничего не стоят..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog4 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Я умру... Ты умрешь... Все, кого я, возможно, встречу, с кем познакомлюсь, все умрут... Все наши родственники, близкие люди... Возможная любовь... Все друзья... Их дети... И дети их детей... Они все умрут... Исчезнут..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog5 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы").withStyle(ChatFormatting.ITALIC),
                    Component.literal("И со временем мир забудет все, и о них, и о нас... Все созданные воспоминания рано или поздно будут забыты...").withStyle(style -> style.withColor(0x88CCFF)),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog6 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Любое наше возможное влияние тоже... Оно просто раствориться в этом мире, станет не отличимым от того что мы принимаем за изначальное..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog7 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Когда капля воды падает на водную гладь, по воде идет рябь... Это последствия действия... Но неважно насколько большая будет капля, рябь спустя время уйдет, раствориться, и станет такой же водной гладью что и раньше..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog8 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы").withStyle(ChatFormatting.ITALIC),
                    Component.literal("И когда умрет и эта вселенная... Вместе с ней умрут и любые возможные последствия наших действий, все значения...").withStyle(style -> style.withColor(0x88CCFF)),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog9 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Настоящее - это секунда или даже меньше... Оно проходит, пока я разговариваю с тобой, и становится прошлым, которое невозможно изменить..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog10 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("И единственное, что остается, - это будущее... Которое и есть смерть... Ведь жизнь по сути это какой-то процесс, а что нам важно в процессе? Результат, не так ли? И результат нашей жизни это смерть..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog11 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("И ради чего жить? Ради радости и счастья? Но разве счастье не иллюзорно, мимолетно и сиюминутно? К тому же после и до него всегда идут боль и печаль..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog12 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Хмм... Но... Тогда где же обещанное истинное счастье? Существует ли оно вообще? ... Должны ли мы терпеть все эти моменты отчаяния ради этих маленьких мгновений счастья в вашей жизни, которые в конечном итоге принесут нам только больше боли в будущем? ..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog13 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Это как любовь... Она кажется такой прекрасной, красивой, воздушной и приносящей истинное счастье как некоторые говорят... Но мы очень часто забываем что любовь очень похожа на сделку... Она и есть по сути источник и нашего счастья и несчастья..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog14 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы").withStyle(ChatFormatting.ITALIC),
                    Component.literal("Ненависть, печать, сожаления, страдания рождаются из утраты того что мы любим... Тем или иным образом...").withStyle(style -> style.withColor(0x88CCFF)),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog15 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("И если все конечно, ибо имеет начало то тогда... Все что мы любим, мы можем потерять, и теряем..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog16 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Простой пример... Любовь к близкому тебе человеку... И его потеря, будь то уход из конкретно твоей жизни или смерть... Все ценные и как казалось раньше счастливые моменты связанные с ним превращаются в напоминание об этом, которые теперь как ножи вонзаются прямо таки в душу... Чем сильнее любовь, тем сильнее боль от потери... И следовательно тем сильнее ярость и печаль которые она порождает..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog17 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы").withStyle(ChatFormatting.ITALIC),
                    Component.literal("И в этом мире так много печали... Так много отчаяния и тоски... Почему? ... Неужели в этом всем нет смысла...").withStyle(style -> style.withColor(0x88CCFF)),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog18 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("Все значения были придуманы людьми... Все это ложь и правда одновременно..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog19 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("И давайте будем честны... Вы не можете полноценно доказать мне существование жизни после смерти... Это все то во что мы верим, хотя по сути вера отличается от факта тем что факт считается не опровержимым, а в вере есть выбор, верить или нет... Но откуда мы знаем что факт абсолютно точно истинен? Вопрос \"ПОЧЕМУ?\" может разрушить все во что мы верим, и факты тоже вера только очень сильная, кто-то или что-то убедило вас что-то непоколебимо..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog20 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы").withStyle(ChatFormatting.ITALIC),
                    Component.literal("Но задавая вопрос \"ПОЧЕМУ?\", неважно где, мы рано или поздно упремся в то что ответа просто нет... По сути сегодня никакая наука не может объяснить все и доказать свое объяснение... Мы не знаем... Просто думаем что знаем... Тот кто думает что он прав, думает что он прав пока не узнает что он был неправ...").withStyle(style -> style.withColor(0x88CCFF)),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );

            Dialog dialog21 = new Dialog(
                    ResourceLocation.fromNamespaceAndPath(MineGUI.MODID, "textures/gui/user1.png"),
                    Component.literal("Голос из Тьмы"),
                    Component.literal("И так что... Был ли кто-нибудь когда-нибудь реально, счастлив? ... Абсолютно доволен всем? ... Без единой мысли об обратном? Без беспокойства о том что это закончиться? ..."),
                    MineGUIColorPalette.MEDIUM_PURPLE,
                    MineGUIColorPalette.WHITE,
                    10000
            );



            // Добавление диалогов в очередь
            DialogManager.showDialog(dialog1);
            DialogManager.showDialog(dialog2);
            DialogManager.showDialog(dialog3);
            DialogManager.showDialog(dialog4);
            DialogManager.showDialog(dialog5);
            DialogManager.showDialog(dialog6);
            DialogManager.showDialog(dialog7);
            DialogManager.showDialog(dialog8);
            DialogManager.showDialog(dialog9);
            DialogManager.showDialog(dialog10);
            DialogManager.showDialog(dialog11);
            DialogManager.showDialog(dialog12);
            DialogManager.showDialog(dialog13);
            DialogManager.showDialog(dialog14);
            DialogManager.showDialog(dialog15);
            DialogManager.showDialog(dialog16);
            DialogManager.showDialog(dialog17);
            DialogManager.showDialog(dialog18);
            DialogManager.showDialog(dialog19);
            DialogManager.showDialog(dialog20);
            DialogManager.showDialog(dialog21);

            DialogManager.getAllDialogs();



             */

            //assert Minecraft.getInstance().player != null;
           // Minecraft.getInstance().player.sendSystemMessage(Component.literal("Добро пожаловать в MineGUI!").withStyle(ChatFormatting.GOLD));


        }


    }
}
