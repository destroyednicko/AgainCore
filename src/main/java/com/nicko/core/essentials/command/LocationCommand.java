package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.LocationUtil;

@CommandMeta(label = "loc", permission = "core.loc")
public class LocationCommand {

    public void execute(Player player) {
        player.sendMessage(LocationUtil.serialize(player.getLocation()));
        System.out.println(LocationUtil.serialize(player.getLocation()));
    }

}
