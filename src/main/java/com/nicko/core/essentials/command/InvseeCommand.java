package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.essentials.menus.InvseeMenu;

@CommandMeta(label = "invsee", permission = "core.invsee")
public class InvseeCommand {

    public void excute(Player player, Player target) {
        new InvseeMenu(target).openMenu(player);
    }

}
