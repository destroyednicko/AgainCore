package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.Idioma;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"gamemode", "gm"}, permission = "core.gamemode")
public class GameModeCommand {

    public void execute(Player player, @CPL("gamemode") GameMode gameMode) {
        if (gameMode == null) {
            player.sendMessage(CC.RED + " • Esse modo de jogo é inválido.");
            return;
        }

        player.setGameMode(gameMode);
        player.updateInventory();
        player.sendMessage(CC.GOLD + " • Você atualizou seu modo de jogo.");
    }

    public void execute(CommandSender sender, @CPL("gamemode") GameMode gameMode, @CPL("jogador") Player target) {
        if (target == null) {
            sender.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        if (gameMode == null) {
            sender.sendMessage(CC.RED + " • Esse modo de jogo é inválido.");
            return;
        }

        target.setGameMode(gameMode);
        target.updateInventory();
        target.sendMessage(CC.GOLD + " • Seu modo de jogo foi atualizado por " + sender.getName());
    }

}
