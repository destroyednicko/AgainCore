package com.nicko.core.profile.staff.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"vanish", "v"}, permission = "core.vanish")
public class VanishCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        boolean toggle = !profile.getStaffOptions().isVanish();
        profile.getStaffOptions().setVanish(toggle);
        player.sendMessage(profile.getStaffOptions().isVanish() ?
            CC.GREEN + " • Agora você está oculto." : CC.RED + " • Você não está mais oculto.");
        profile.toggleVanish();
    }

    public void execute(Player player, Boolean toggle) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().setVanish(toggle);
        player.sendMessage(profile.getStaffOptions().isVanish() ?
            CC.GREEN + " • Agora você está oculto." : CC.RED + " • Você não está mais oculto.");
        profile.toggleVanish();
    }

}
