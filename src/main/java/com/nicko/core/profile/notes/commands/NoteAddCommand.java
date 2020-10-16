package com.nicko.core.profile.notes.commands;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.notes.Note;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"note add", "nota add"}, permission = "core.note.add")
public class NoteAddCommand {

    public void execute(Player player, @CPL("jogador") Profile profile, String noteString) {

        int id = profile.getNotes().size() + 1;

        profile.getNotes().add(new Note(id, noteString, player));

        player.sendMessage(CC.translate("&a • Nota adicionada para &f" + profile.getName()));
    }


}
