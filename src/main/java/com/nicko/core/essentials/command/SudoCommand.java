package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.Idioma;

@CommandMeta(label = "sudo", permission = "core.sudo")
public class SudoCommand {

    public void execute(CommandSender sender, @CPL("jogador") Player target, @CPL("comando") String command) {
        if (target == null) {
            sender.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        target.performCommand(command);
        sender.sendMessage(ChatColor.GREEN + " • " + target + " foi forçado a realizar o sudo!");
    }

}
