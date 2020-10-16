package com.nicko.core.profile.staff.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.Idioma;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.BungeeUtil;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"staffswitch", "srv"}, async = true, permission = "core.staff.join")
public class JoinCommand {

    public void execute(Player player, @CPL("servidor") String serverName) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        /*if (!profile.isAuthenticated()) {
            player.sendMessage(Idioma.SECURITY_AUTH_REQUIRED.format());
            return;
        }*/

        player.sendMessage(CC.GREEN + " • Conectando a: " + CC.WHITE + serverName);
        BungeeUtil.connect(player, serverName);
    }

}
