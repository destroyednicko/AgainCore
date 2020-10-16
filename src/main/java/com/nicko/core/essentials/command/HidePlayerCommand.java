package com.nicko.core.essentials.command;

import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "hideplayer", permission = "core.hideplayer")
public class HidePlayerCommand {

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        player.hidePlayer(target);
        player.sendMessage(CC.GOLD + " • Ocultando " + target.getName() + " da sua visão");
    }

    public void execute(Player player, Player target1, Player target2) {
        if (target1 == null || target2 == null) {
            player.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        target1.hidePlayer(target2);
        player.sendMessage(CC.GOLD + " • Ocultando " + target2.getName() + " da visão de " + target1.getName() + ".");
    }

}
