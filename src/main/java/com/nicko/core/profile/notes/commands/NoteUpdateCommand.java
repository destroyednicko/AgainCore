package com.nicko.core.profile.notes.commands;


import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.notes.Note;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"note update", "nota atualizar"}, permission = "core.note.update")
public class NoteUpdateCommand {

    public void execute(Player player, @CPL("jogador") Profile profile, Number noteID, @CPL("nova nota") String noteupdate) {
        if (profile.getNote(noteID.intValue()) == null) {
            player.sendMessage(CC.translate("&7 • Essa nota não existe."));
            return;
        }
        Note note = profile.getNote(noteID.intValue());
        note.setNote(noteupdate);
        note.setUpdateAt(System.currentTimeMillis());
        note.setUpdateBy(player.getName());
        player.sendMessage(CC.translate("&e • Nota #" + noteID + " de &f"  + profile.getName() + "&e atualizada&f."));
    }
}
