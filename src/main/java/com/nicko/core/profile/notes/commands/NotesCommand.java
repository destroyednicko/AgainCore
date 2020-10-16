package com.nicko.core.profile.notes.commands;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.notes.menu.NotesMenu;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"notes", "notas"}, permission = "core.notes")
public class NotesCommand {

    public void execute(Player player, @CPL("jogador") Profile profile) {
        if (profile.getNotes().isEmpty()) {
            player.sendMessage(CC.translate("&c • " + profile.getName() + " não tem notas."));
            return;
        }
        new NotesMenu(profile).openMenu(player);
    }

}
