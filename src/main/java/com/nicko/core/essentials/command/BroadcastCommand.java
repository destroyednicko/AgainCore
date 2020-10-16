package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"broadcast", "bc"}, permission = "core.broadcast")
public class BroadcastCommand {

    public void execute(CommandSender sender, @CPL("mensagem") String broadcast) {
        String message = broadcast.replaceAll("(&([a-f0-9l-or]))", "\u00A7$2");
        Bukkit.broadcastMessage(CC.translate(" • " + message));
    }

}
