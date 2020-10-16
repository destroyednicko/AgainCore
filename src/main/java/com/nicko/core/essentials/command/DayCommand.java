package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"day", "dia"})
public class DayCommand {

    public void execute(Player player) {
        player.setPlayerTime(6000L, false);
        player.sendMessage(CC.GREEN + " • Tempo alterado para: " + CC.BLUE + "dia " + CC.GREEN + ".");
    }

}
