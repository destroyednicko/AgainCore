package com.nicko.core.profile.staff.command;

import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.nicko.core.network.packet.PacketStaffAuth;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.TOTP;

import java.security.GeneralSecurityException;

@CommandMeta(label = "auth", async = true, permission = "core.staff.auth")
public class AuthCommand {

    public void execute(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.isAuthenticated()) {
            player.sendMessage(Idioma.SECURITY_ALREADY_AUTH.format());
            return;
        }

        if (profile.getSecretToken() != null) {
            player.sendMessage(Idioma.SECURITY_SETUP_EXISTS.format());
            return;
        }

        String secretToken = TOTP.generateBase32Secret();
        profile.setSecretToken(secretToken);
        profile.save();

        player.sendMessage(Idioma.SECURITY_SETUP_DONE.format(secretToken));
    }

    public void execute(Player player, @CPL("código") String code) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.isAuthenticated()) {
            player.sendMessage(Idioma.SECURITY_ALREADY_AUTH.format());
            return;
        }

        if (profile.getSecretToken() == null) {
            player.sendMessage(Idioma.SECURITY_SETUP_NEEDED.format());
            return;
        }

        String expected_code;
        try {
            expected_code = TOTP.generateCurrentNumberString(profile.getSecretToken());
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            player.sendMessage(Idioma.UNEXPECTED_ERROR.format(ex.getCause().getMessage()));
            return;
        }

        if (!code.equals(expected_code)) {
            player.sendMessage(Idioma.SECURITY_AUTH_INVALID.format());

            Core.get().getHollow().sendPacket(new PacketStaffAuth(profile.getColoredUsername(),
                Bukkit.getServerName(), false));

            return;
        }

        profile.setAuthenticated(true);
        profile.save();
        player.sendMessage(Idioma.SECURITY_AUTH_VALID.format());

        Core.get().getHollow().sendPacket(new PacketStaffAuth(profile.getColoredUsername(),
            Bukkit.getServerName(), true));
    }

}
