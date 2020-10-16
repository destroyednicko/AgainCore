package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "night")
public class NightCommand {

    public void execute(Player player) {
        player.setPlayerTime(18000L, false);
        player.sendMessage(CC.GREEN + " • Tempo alterado para " + CC.DARK_PURPLE +
                "noite" + CC.GREEN + ".");
    }

}
