package com.nicko.core.profile.conversation.command.ignore;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"ignore", "ignorar"})
public class IgnoreCommand {
    public void execute(Player player) {
        player.sendMessage(CC.translate("&7&l • Ignorar - Ajuda"));
        player.sendMessage(CC.translate("&e/ignorar add &7<jogador>"));
        player.sendMessage(CC.translate("&e/ignorar remover &7<jogador>"));
        player.sendMessage(CC.translate("&e/ignorar listar"));
    }
}
