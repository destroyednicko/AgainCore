package com.nicko.core.essentials.command;

import com.nicko.core.Core;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.BungeeUtil;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"hub", "lobby"}, async = true)
public class HubCommand {

    public void execute(Player player) {
        player.sendMessage(CC.GREEN + " • Enviando para o " + CC.WHITE + Core.get().getMainConfig().getString("SETTINGS.HUB_NAME"));
        BungeeUtil.connect(player, Core.get().getMainConfig().getString("SETTINGS.HUB_NAME"));
    }

}
