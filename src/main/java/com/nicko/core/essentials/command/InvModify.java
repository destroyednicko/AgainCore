package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "invmodify", permission = "core.invmodify")
public class InvModify {

    public void excute(Player player, Player target) {
        player.openInventory(target.getInventory());
    }

}
