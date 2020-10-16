package com.nicko.core.essentials.command;

import com.nicko.core.Core;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"help", "ts", "discord", "?", "store", "links", "twitter"}, async = true)
public class HelpCommand {

    public void execute(Player player) {
        for (String message : Core.get().getMainConfig().getStringList("SETTINGS.HELP_MESSAGE")) {
            player.sendMessage(CC.translate(message));
        }
    }

}

