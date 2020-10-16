package com.nicko.core.profile.staff.command;

import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.profile.Profile;

@CommandMeta(label = "resetauth", async = true, permission = "core.staff.resetauth")
public class ResetAuth {

    public void execute(CommandSender sender, @CPL("jogador") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        profile.setAuthenticated(false);
        profile.setSecretToken(null);
        profile.save();

        sender.sendMessage(Idioma.SECURITY_AUTH_RESET.format(profile.getPlayer()));
    }

}
