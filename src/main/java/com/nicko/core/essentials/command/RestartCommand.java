package com.nicko.core.essentials.command;

import com.nicko.core.Core;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandMeta(label = {"restart", "reiniciar", "reinicio"}, permission = "core.restart")
public class RestartCommand {

    public void execute(Player player) {
        player.sendMessage(ChatColor.YELLOW + "/reinicio iniciar <segundos>");
        player.sendMessage(ChatColor.YELLOW + "/reinicio cancelar");
    }

    @CommandMeta(label = "iniciar", permission = "core.start")
    public class start extends RestartCommand {
        public void execute(Player player, @CPL("seconds") Number seconds) {
            Core.get().getEssentials().restart(seconds.intValue());
        }
    }

    @CommandMeta(label = "cancelar", permission = "core.start")
    public class cancel extends RestartCommand {
        public void execute(Player player) {
            Core.get().getEssentials().cancelRestart();
        }
    }

}
