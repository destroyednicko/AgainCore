package com.nicko.core.profile.staff.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"togglestaffchat", "tsc"}, permission = "core.staff")
public class ToggleStaffChat {
    public void execute(Player player) {
        Profile profile = Profile.getByPlayer(player);
        profile.getStaffOptions().setReceiveStaffChat(!profile.getStaffOptions().isReceiveStaffChat());

        profile.getPlayer().sendMessage(profile.getStaffOptions().isStaffModeEnabled() ?
            CC.GREEN + " • Recebendo mensagens do canal de moderação." : CC.RED + " • Canal de moderação oculto.");
    }
}
