package com.nicko.shinigami.command.adapter.impl;

import com.nicko.shinigami.command.adapter.CommandTypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class OfflinePlayerTypeAdapter implements CommandTypeAdapter {
    @Override
    public <T> T convert(final String string, final Class<T> type) {
        return type.cast(Bukkit.getOfflinePlayer(string));
    }

    @Override
    public <T> List<String> tabComplete(final String string, final Class<T> type) {
        final List<String> completed = new ArrayList<String>();
        for (final OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (player.getName().toLowerCase().startsWith(string.toLowerCase())) {
                completed.add(player.getName());
            }
        }
        return completed;
    }
}
