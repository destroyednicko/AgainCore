package com.nicko.core.essentials.command;

import com.nicko.core.Core;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"killall", "clearmobs"}, permission = "core.staff.killall", async = true)
public class KillallCommand {

    public void execute(CommandSender sender) {
        sender.sendMessage(CC.CHAT_BAR);
        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(CC.GREEN + " • " + CC.WHITE + Core.get().getEssentials().clearEntities(world) + CC.GREEN + " entidades removidas de " + CC.WHITE + world.getName());
        }
        sender.sendMessage(CC.CHAT_BAR);
    }

}

