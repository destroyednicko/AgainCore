package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.Core;
import com.nicko.core.util.BukkitReflection;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "setslots", async = true, permission = "core.setslots")
public class SetSlotsCommand {

    // TODO: Alterar a var. 'slots' pra int
    public void execute(CommandSender sender, @CPL("slots") String slots) {
        BukkitReflection.setMaxPlayers(Core.get().getServer(), Integer.parseInt(slots));
        sender.sendMessage(CC.GOLD + " • Você definiu o máximo de slots para " + slots + ".");
    }

}
