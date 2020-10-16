package com.nicko.core.profile.staff.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.Idioma;
import com.nicko.core.profile.Profile;

@CommandMeta(label = {"invalidateauth", "invalidarauth", "invalidar auth"}, async = true, permission = "core.staff.invalidateauth")
public class InvalidateAuth {

    public void execute(CommandSender sender, @CPL("jogador") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        profile.setAuthenticated(false);
        profile.save();

        sender.sendMessage(Idioma.SECURITY_AUTH_INVALIDATE.format(profile.getPlayer().getName()));
    }

}
