package com.nicko.core.essentials.command;

import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"gmc"}, permission = "core.gamemode")
public class GMCCommand {

    public void execute(Player player) {
        player.setGameMode(GameMode.CREATIVE);
        player.updateInventory();
        player.sendMessage(CC.GOLD + " • Seu modo de jogo foi atualizado.");
    }

    public void execute(CommandSender sender, Player target) {
        if (target == null) {
            sender.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        target.setGameMode(GameMode.CREATIVE);
        target.updateInventory();
        target.sendMessage(CC.GOLD + " • Seu modo de jogo foi atualizado por " + sender.getName());
    }

}
