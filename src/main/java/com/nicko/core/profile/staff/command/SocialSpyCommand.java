package com.nicko.core.profile.staff.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "socialspy", permission = "core.staff.socialspy")
public class SocialSpyCommand {

    public void excute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().setSocialSpy(!profile.getStaffOptions().isSocialSpy());
        player.sendMessage(CC.translate("&e • O 'socialSpy' foi " +
            (profile.getStaffOptions().isSocialSpy() ? "&ahabilitado" : "&cdesabilitado") + "&e."));
    }

}
