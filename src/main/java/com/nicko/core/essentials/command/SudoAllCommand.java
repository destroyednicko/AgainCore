package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import com.nicko.shinigami.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "sudoall", permission = "core.sudo", options = "s")
public class SudoAllCommand {

    public void execute(CommandSender sender, CommandOption option, @CPL("comando") String command) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (option == null && !player.equals(sender)) {
                continue;
            }

            player.performCommand(command);
        }

        sender.sendMessage(ChatColor.GREEN + " • Todos os jogadores foram forçados a realizar o sudo!");
    }

}
