package com.nicko.core.essentials.command;

import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"feed", "eat", "saciar"}, permission = "core.feed")
public class FeedCommand {

    public void execute(Player player) {
        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.updateInventory();
        player.sendMessage(CC.GOLD + " • Você saciou sua fome.");
    }

    public void execute(CommandSender sender, Player player) {
        if (player == null) {
            sender.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.updateInventory();
        player.sendMessage(CC.GOLD + " • Sua fome foi saciada por " + sender.getName());
    }

}
