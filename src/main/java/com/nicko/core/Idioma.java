package com.nicko.core;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public enum Idioma {

    FAILED_TO_LOAD_PROFILE("COMMON_ERRORS.FAILED_TO_LOAD_PROFILE"),
    COULD_NOT_RESOLVE_PLAYER("COMMON_ERRORS.COULD_NOT_RESOLVE_PLAYER"),
    PLAYER_NOT_FOUND("COMMON_ERRORS.PLAYER_NOT_FOUND"),
    RANK_NOT_FOUND("COMMON_ERRORS.RANK_NOT_FOUND"),
    UNEXPECTED_ERROR("COMMON_ERRORS.UNEXPECTED_ERROR"),
    KICK_CLOSED_TESTING("COMMON.KICK_CLOSED_TESTING"),
    STAFF_AUTH_SUCCESS("STAFF.AUTH_SUCCESS"),
    STAFF_AUTH_FAIL("STAFF.AUTH_FAIL"),
    STAFF_CHAT("STAFF.CHAT"),
    STAFF_JOIN_NETWORK("STAFF.JOIN_NETWORK"),
    STAFF_SWITCH_SERVER("STAFF.SWITCH_SERVER"),
    STAFF_LEAVE_NETWORK("STAFF.LEAVE_NETWORK"),
    STAFF_REPORT_BROADCAST("STAFF.REPORT_BROADCAST"),
    STAFF_REQUEST_BROADCAST("STAFF.REQUEST_BROADCAST"),
    STAFF_REQUEST_SUBMITTED("STAFF.REQUEST_SUBMITTED"),
    STAFF_FREEZE("STAFF.FREEZE"),
    STAFF_FREEZE_MESSAGE("STAFF.FREEZE_MESSAGE"),
    STAFF_UNFREEZE_MESSAGE("STAFF.UNFREEZE_MESSAGE"),
    STAFF_SOCIAL_SPY("STAFF.SOCIAL_SPY"),
    CLEAR_CHAT_BROADCAST("CHAT.CLEAR_CHAT_BROADCAST"),
    MUTE_CHAT_BROADCAST("CHAT.MUTE_CHAT_BROADCAST"),
    DELAY_CHAT_ENABLED_BROADCAST("CHAT.DELAY_CHAT_ENABLED_BROADCAST"),
    DELAY_CHAT_DISABLED_BROADCAST("CHAT.DELAY_CHAT_DISABLED_BROADCAST"),
    CHAT_DELAYED("CHAT.CHAT_DELAYED"),

    CONVERSATION_SEND_MESSAGE("CONVERSATION.SEND_MESSAGE"),
    CONVERSATION_RECEIVE_MESSAGE("CONVERSATION.RECEIVE_MESSAGE"),

    OPTIONS_PRIVATE_MESSAGES_ENABLED("OPTIONS.PRIVATE_MESSAGES_ENABLED"),
    OPTIONS_PRIVATE_MESSAGES_DISABLED("OPTIONS.PRIVATE_MESSAGES_DISABLED"),

    OPTIONS_PRIVATE_MESSAGE_SOUND_ENABLED("OPTIONS.PRIVATE_MESSAGE_SOUNDS_ENABLED"),
    OPTIONS_PRIVATE_MESSAGE_SOUND_DISABLED("OPTIONS.PRIVATE_MESSAGE_SOUNDS_DISABLED"),

    OPTIONS_GLOBAL_CHAT_ENABLED("OPTIONS.GLOBAL_CHAT_ENABLED"),
    OPTIONS_GLOBAL_CHAT_DISABLED("OPTIONS.GLOBAL_CHAT_DISABLED"),

    OPTIONS_TIPS_ENABLED("OPTIONS.TIPS_ENABLED"),
    OPTIONS_TIPS_DISABLE("OPTIONS.TIPS_DISABLE"),

    OPTIONS_SCOREBOARD_ENABLE("OPTIONS.SCOREBOARD_ENABLE"),
    OPTIONS_SCOREBOARD_DISABLE("OPTIONS.SCOREBOARD_DISABLE"),

    SECURITY_SETUP_NEEDED("SECURITY.SETUP_NEEDED"),
    SECURITY_SETUP_EXISTS("SECURITY.SETUP_EXISTS"),
    SECURITY_SETUP_DONE("SECURITY.SETUP_DONE"),
    SECURITY_AUTH_RESET("SECURITY.AUTH_RESET"),
    SECURITY_AUTH_INVALIDATE("SECURITY.AUTH_INVALIDATE"),
    SECURITY_AUTH_INVALID("SECURITY.AUTH_INVALID"),
    SECURITY_AUTH_VALID("SECURITY.AUTH_VALID"),
    SECURITY_AUTH_REQUIRED("SECURITY.AUTH_REQUIRED"),
    SECURITY_ALREADY_AUTH("SECURITY.ALREADY_AUTH");

    private String path;

    public String format(Object... objects) {
        return new MessageFormat(ChatColor.translateAlternateColorCodes('&',
            Core.get().getMainConfig().getString(path))).format(objects);
    }

    public List<String> formatLines(Object... objects) {
        List<String> lines = new ArrayList<>();

        if (Core.get().getMainConfig().get(path) instanceof String) {
            lines.add(new MessageFormat(ChatColor.translateAlternateColorCodes('&',
                Core.get().getMainConfig().getString(path))).format(objects));
        } else {
            for (String string : Core.get().getMainConfig().getStringList(path)) {
                lines.add(new MessageFormat(ChatColor.translateAlternateColorCodes('&', string))
                    .format(objects));
            }
        }

        return lines;
    }

    //    public static final String PUBLIC_CHAT_MUTE_APPLIED = "&b • O chat público foi silenciado por {actor}";
    //    public static final String PUBLIC_CHAT_DELAY_APPLIED = "&b • O chat público teve o delay definido por {actor}";
    //    public static final String CHAT_ATTEMPT_FILTERED = "&c • Sua mensagem foi filtrada.";
    //    public static final String CHAT_ATTEMPT_PLAYER_MUTED = "&c • Você está silenciado por mais {time-remaining}.";
    //    public static final String CHAT_ATTEMPT_PUBLIC_CHAT_MUTED = "&c • O chat público está atualmente silenciado.";
    //    public static final String CHAT_ATTEMPT_PUBLIC_CHAT_DELAYED = "&c • Você poderá falar novamente em {time-remaining}.";
    //    public static final String OPTIONS_GLOBAL_CHAT_DISABLED = "&c • Seu chat global está desabilitado.";
    //    public static final String OPTIONS_PRIVATE_CHAT_DISABLED = "&c • Suas mensagens privadas estão desabilitadas.";
    //    public static final String OPTIONS_PRIVATE_CHAT_DISABLED_OTHER = "&c • Este jogador está com as mensagens privadas desabilitadas.";

}
