package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.Core;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "setspawn", permission = "core.setspawn")
public class SetSpawnCommand {

    public void execute(Player player) {
        Core.get().getEssentials().setSpawn(player.getLocation());
        player.sendMessage(CC.GREEN + " • Você definiu o spawn do mundo para sua localização.");
    }

}
