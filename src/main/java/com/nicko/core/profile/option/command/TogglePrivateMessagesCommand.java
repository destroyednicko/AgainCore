package com.nicko.core.profile.option.command;

import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;

@CommandMeta(label = {"togglepm", "togglepms", "tpm", "tpms"})
public class TogglePrivateMessagesCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().receivingNewConversations(!profile.getOptions().receivingNewConversations());
        profile.getConversations().expireAllConversations();

        if (profile.getOptions().receivingNewConversations()) {
            player.sendMessage(Idioma.OPTIONS_PRIVATE_MESSAGES_ENABLED.format());
        } else {
            player.sendMessage(Idioma.OPTIONS_PRIVATE_MESSAGES_DISABLED.format());
        }
    }

}
