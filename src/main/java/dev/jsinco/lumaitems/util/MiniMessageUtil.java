package dev.jsinco.lumaitems.util;

import dev.jsinco.lumaitems.LumaItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.List;

import static dev.jsinco.lumaitems.util.Util.WITH_DELIMITER;

public class MiniMessageUtil {

    public static Component PREFIX = mm(LumaItems.getInstance().getConfig().getString("prefix", ""));

    public static void msg(CommandSender sender, String m) {
        sender.sendMessage(PREFIX.append(mm(m)));
    }

    public static Component mm(String m) {
        return MiniMessage.miniMessage().deserialize("<!i>" + m);
    }

    public static List<Component> mml(String m) {
        return List.of(mm(m));
    }

    public static List<Component> mml(List<String> m) {
        return m.stream().map(MiniMessageUtil::mm).toList();
    }

    public static List<Component> mml(String... m) {
        return List.of(m).stream().map(MiniMessageUtil::mm).toList();
    }


    public static String convertLegacyToMiniMesssageString(String legacy) {
        String[] texts = legacy.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append("<").append(texts[i].substring(0, 7)).append(texts[i].substring(7) + ">");
                } else {
                    finalText.append(getMiniMessageNamedColor('&' + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }
        return finalText.toString();
    }


    public static String getMiniMessageNamedColor(String namedColor) {
        return switch (namedColor) {
            case "&0" -> "<black>";
            case "&1" -> "<dark_blue>";
            case "&2" -> "<dark_green>";
            case "&3" -> "<dark_aqua>";
            case "&4" -> "<dark_red>";
            case "&5" -> "<dark_purple>";
            case "&6" -> "<gold>";
            case "&7" -> "<gray>";
            case "&8" -> "<dark_gray>";
            case "&9" -> "<blue>";
            case "&a" -> "<green>";
            case "&b" -> "<aqua>";
            case "&c" -> "<red>";
            case "&d" -> "<light_purple>";
            case "&e" -> "<yellow>";
            case "&f" -> "<white>";
            case "&k" -> "<obf>";
            case "&l" -> "<b>";
            case "&m" -> "<st>";
            case "&n" -> "<u>";
            case "&o" -> "<i>";
            case "&r" -> "<reset>";
            default -> throw new IllegalStateException("Unexpected value: " + namedColor);
        };
    }
}
