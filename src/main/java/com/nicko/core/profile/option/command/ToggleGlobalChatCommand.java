package com.nicko.core.profile.option.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.Idioma;
import com.nicko.core.profile.Profile;

@CommandMeta(label = {"toggleglobalchat", "tgc", "togglepublicchat", "tpc"})
public class ToggleGlobalChatCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());

        if (profile.getOptions().publicChatEnabled()) {
            player.sendMessage(Idioma.OPTIONS_GLOBAL_CHAT_ENABLED.format());
        } else {
            player.sendMessage(Idioma.OPTIONS_GLOBAL_CHAT_DISABLED.format());
        }
    }

}
