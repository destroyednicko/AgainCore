package com.nicko.shinigami.command.adapter.impl;

import com.nicko.shinigami.command.adapter.CommandTypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;

import java.util.ArrayList;
import java.util.List;

public class PlayerTypeAdapter implements CommandTypeAdapter {
    @Override
    public <T> T convert(final String string, final Class<T> type) {
        return type.cast(Bukkit.getPlayer(string));
    }

    @Override
    public <T> List<String> tabComplete(final String string, final Class<T> type) {
        final List<String> completed = new ArrayList<>();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = Profile.getByPlayer(player);
            if (player.getName().toLowerCase().startsWith(string.toLowerCase()) &&
                !profile.getStaffOptions().isStaffModeEnabled()
                && !profile.getStaffOptions().isVanish()) {
                completed.add(player.getName());
            }
        }
        return completed;
    }
}
