package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.Idioma;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"heal", "curar"}, permission = "core.heal")
public class HealCommand {

    public void execute(Player player) {
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.updateInventory();
        player.sendMessage(CC.GOLD + " • Você se curou.");
    }

    public void execute(CommandSender sender, Player player) {
        if (player == null) {
            sender.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.updateInventory();
        player.sendMessage(CC.GOLD + " • Você foi curado por " + sender.getName());
    }

}
