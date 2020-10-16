package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.CoreAPI;
import com.nicko.core.util.BukkitReflection;
import com.nicko.core.util.string.CC;
import com.nicko.core.util.string.StyleUtil;

@CommandMeta(label = {"ping", "latencia"})
public class PingCommand {

    public void execute(Player player) {
        player.sendMessage(CC.YELLOW + " • Sua latência: " + StyleUtil.colorPing(BukkitReflection.getPing(player)));
    }

    public void execute(CommandSender sender, @CPL("jogador") Player target) {
        if (target == null) {
            sender.sendMessage(CC.RED + " • Nenhum jogador foi encontrado com este nome.");
        } else {
            sender.sendMessage(CC.YELLOW + " • Latência de " + CoreAPI.getColoredName(target) + CC.YELLOW + ": " +
                StyleUtil.colorPing(BukkitReflection.getPing(target)));
        }
    }

}
