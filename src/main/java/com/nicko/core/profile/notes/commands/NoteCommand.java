package com.nicko.core.profile.notes.commands;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"note", "nota"}, permission = "core.note")
public class NoteCommand {
    public void execute(Player player) {
        player.sendMessage(CC.translate("&7&l • Notas - Comandos"));
        player.sendMessage(CC.translate("&e/nota add &7<jogador> <nota>"));
        player.sendMessage(CC.translate("&e/nota atualizar &7<jogador> <id> <nova nota>"));
        player.sendMessage(CC.translate("&e/nota remover &7<jogador> <id>"));
        player.sendMessage(CC.translate("&e/notas"));
    }
}
