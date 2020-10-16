package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"sunset", "evening"})
public class SunsetCommand {

    public void execute(Player player) {
        player.setPlayerTime(12000, false);
        player.sendMessage(CC.GREEN + " • Tempo alterado para 'por do sol'.");
    }

}
