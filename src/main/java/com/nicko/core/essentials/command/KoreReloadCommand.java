package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.Core;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"core reload", "core reload"}, permission = "core.reload")
public class KoreReloadCommand {

    public void execute(CommandSender sender) {
        Core.get().getEssentials().reload();
        sender.sendMessage(CC.translate("&a • 'Server Core' recarregado."));
    }

}
