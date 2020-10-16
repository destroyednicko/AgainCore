package com.nicko.core.profile.staff.command;

import com.google.common.collect.Lists;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.nicko.core.Core;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.staff.events.PlayerVisibilityChangeEvent;
import com.nicko.core.util.string.CC;

import java.util.stream.Collectors;

@CommandMeta(label = "hidestaff", permission = "core.hidestaff")
public class HideStaffCommand {

    public void excute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        boolean toggle = !profile.getStaffOptions().isHideStaff();
        profile.getStaffOptions().setHideStaff(toggle);
        player.sendMessage(profile.getStaffOptions().isHideStaff() ?
            CC.GREEN + " • Você ocultou todos os staffers." : CC.RED + " • Os staffers estão visíveis.");
        toggle(player);
    }

    public void excute(Player player, Boolean toggle) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().setHideStaff(toggle);
        player.sendMessage(profile.getStaffOptions().isHideStaff() ?
                CC.GREEN + " • Você ocultou todos os staffers." : CC.RED + " • Os staffers estão visíveis.");
        toggle(player);
    }

    public void toggle(Player player) {
        if (player.isOnline()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getStaffOptions().isHideStaff()) {
                new PlayerVisibilityChangeEvent(true, profile, Bukkit.getOnlinePlayers().stream()
                    .filter(online -> Profile.getByPlayer(online).getStaffOptions().isVanish() &&
                        Profile.getByPlayer(online).getStaffOptions().isHideStaff())
                    .collect(Collectors.toList()))
                    .call();
                if (Core.get().getMainConfig().getBoolean("STAFF.VISIBILITY_ENGINE")) {
                    Bukkit.getOnlinePlayers().forEach(online -> {
                        Profile onlineProfile = Profile.getByPlayer(online);
                        if (onlineProfile.getStaffOptions().isVanish() || onlineProfile.getStaffOptions().isStaffModeEnabled()) {
                            player.hidePlayer(online);
                        }
                    });
                }
            } else {
                new PlayerVisibilityChangeEvent(false, profile, Lists.newArrayList(Bukkit.getOnlinePlayers()))
                    .call();
                if (Core.get().getMainConfig().getBoolean("STAFF.VISIBILITY_ENGINE")) {
                    Bukkit.getOnlinePlayers().forEach(player::showPlayer);
                }
            }
        }
    }
}
