package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandMeta(label = "showallplayers", permission = "core.showallplayers")
public class ShowAllPlayersCommand {

    public void execute(Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            player.showPlayer(otherPlayer);
        }
    }

}
