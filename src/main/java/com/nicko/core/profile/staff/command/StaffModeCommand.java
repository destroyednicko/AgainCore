package com.nicko.core.profile.staff.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"staffmode", "mod", "sm"}, permission = "core.staff.staffmode")
public class StaffModeCommand {
    public void execute(Player player, boolean toggle) {
        Profile profile = Profile.getByPlayer(player);
        profile.getStaffOptions().setStaffModeEnabled(toggle);

        player.sendMessage(profile.getStaffOptions().isStaffModeEnabled() ?
            CC.GREEN + " • Você está no modo de moderação." : CC.RED + " • Você saiu do modo de moderação.");
        profile.toggleStaffMode();
    }

    public void execute(Player player) {
        Profile profile = Profile.getByPlayer(player);
        profile.getStaffOptions().setStaffModeEnabled(!profile.getStaffOptions().isStaffModeEnabled());

        profile.getPlayer().sendMessage(profile.getStaffOptions().isStaffModeEnabled() ?
            CC.GREEN + " • Você está no modo de moderação." : CC.RED + " • Você saiu do modo de moderação.");
        profile.toggleStaffMode();
    }
}
