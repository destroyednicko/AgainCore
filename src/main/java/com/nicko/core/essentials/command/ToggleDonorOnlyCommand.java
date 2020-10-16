package com.nicko.core.essentials.command;

import com.nicko.core.Core;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "toggledonoronly", permission = "core.donoronly")
public class ToggleDonorOnlyCommand {

    public void execute(CommandSender sender) {
        Core.get().getEssentials().setDonorOnly(!Core.get().getEssentials().isDonorOnly());
        boolean donorOnly = Core.get().getEssentials().isDonorOnly();
        sender.sendMessage(CC.translate("&e • Você " +
            (donorOnly ? "&ahabilitou" : "&cdesabilitou") + "&e o modo de apenas doadores."));
        Core.get().getMainConfig().getConfiguration().set("ESSENTIAL.DONOR_ONY", donorOnly);
    }

}
