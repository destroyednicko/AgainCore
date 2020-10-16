package com.nicko.core.profile.option.command;

import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;

@CommandMeta(label = {"togglesounds", "sounds"})
public class ToggleSoundsCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().playingMessageSounds(!profile.getOptions().playingMessageSounds());

        if (profile.getOptions().playingMessageSounds()) {
            player.sendMessage(Idioma.OPTIONS_PRIVATE_MESSAGE_SOUND_ENABLED.format());
        } else {
            player.sendMessage(Idioma.OPTIONS_PRIVATE_MESSAGE_SOUND_DISABLED.format());
        }
    }

}
