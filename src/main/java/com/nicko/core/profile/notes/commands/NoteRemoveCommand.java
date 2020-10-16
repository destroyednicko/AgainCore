package com.nicko.core.profile.notes.commands;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"note remove", "nota remover"}, permission = "core.note.remove")
public class NoteRemoveCommand {

    public void execute(Player player, @CPL("jogador") Profile profile, Number noteID) {
        if (!profile.removeNote(noteID.intValue())) {
            player.sendMessage(CC.translate("&7 • Essa nota não existe."));
            return;
        }
        player.sendMessage(CC.translate("&c • Nota #" + noteID + "&c de &f" + profile.getName() + "&c removida."));
    }
}
