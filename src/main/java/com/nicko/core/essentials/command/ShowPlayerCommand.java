package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import com.nicko.core.Idioma;

@CommandMeta(label = "showplayer", permission = "core.showplayer")
public class ShowPlayerCommand {

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        player.showPlayer(target);
        player.sendMessage(ChatColor.GOLD + " • Exibindo " + target.getName());
    }

    public void execute(Player player, Player target1, Player target2) {
        if (target1 == null || target2 == null) {
            player.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        target1.showPlayer(target2);
        player.sendMessage(ChatColor.GOLD + " • Exibindo " + target2.getName() + " to " + target1.getName());
    }

}