package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"more", "mais", "handgive"}, permission = "core.more")
public class MoreCommand {

    public void execute(Player player) {
        if (player.getItemInHand() == null) {
            player.sendMessage(CC.RED + " • Você não está segurando nada em sua mão.");
            return;
        }

        player.getItemInHand().setAmount(64);
        player.updateInventory();
        player.sendMessage(CC.GREEN + " • Você 'givou' mais um item que estava em sua mão.");
    }

}
