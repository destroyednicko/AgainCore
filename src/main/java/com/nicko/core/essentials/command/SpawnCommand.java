package com.nicko.core.essentials.command;

import com.nicko.core.Core;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "spawn", permission = "core.spawn")
public class SpawnCommand {

    public void execute(Player player) {
        Core.get().getEssentials().teleportToSpawn(player);
        player.sendMessage(CC.GREEN + " • Teleportado para o spawn.");
    }

}
